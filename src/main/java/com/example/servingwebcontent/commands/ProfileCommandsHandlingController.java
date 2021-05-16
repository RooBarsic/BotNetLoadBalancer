package com.example.servingwebcontent.commands;

import com.example.BotNetUtils;
import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import com.example.message.data.BotNetUser;
import com.example.message.data.ExpectedData;
import com.example.message.data.UserMemoryCard;
import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileCommandsHandlingController{
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;
    private final String TELEGRAM_FEEDBACK_GROUP_ID;

    @Autowired
    ProfileCommandsHandlingController(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
        TELEGRAM_FEEDBACK_GROUP_ID = tokenStorage.getTokens("TELEGRAM_FEEDBACK_GROUP_ID");
    }

    @RequestMapping(value = "/command/profile/add-words", method = RequestMethod.POST)
    @ResponseBody
    public String addWordsCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /add-words form : " + request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.MEMORY_CARD) {
            final String[] buff = request.getMessage().split("-|\n");
            StringBuilder responseMessage = new StringBuilder("Identified words :\n");
            for (int i = 1; i < buff.length; i += 2) {
                final String question = buff[i - 1];
                final String answer = buff[i];
                final UserMemoryCard memoryCard = new UserMemoryCard(question, answer);
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .addMemoryCard(memoryCard);
                responseMessage.append("word : ")
                        .append(question)
                        .append("\n")
                        .append("answer : ")
                        .append(answer)
                        .append("\n")
                        .append("\n");
            }
            response.setMessage(responseMessage.toString());
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.NONE);
        } else {
            if (hierarchy.getUserByTelegramId(request.getUserChatId()).hasSpaceForNewMemoryCard()) {
                response.setMessage("Send me word and it's definition. \n\n" +
                        "Separate the question from the answer using dash '-'.\n" +
                        "Example: Capital of Russia - Moscow;\n\n" +
                        "You can send as many words in one message as you want!");
            } else {
                response.setMessage("Sorry. You already reached your limits.\nPlease remove some words.");
            }

            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.MEMORY_CARD);
        }

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/profile/show", method = RequestMethod.POST)
    @ResponseBody
    public String showAskedWordAnswerCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /show form : " + request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        if (hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).hasMemoryCards()) {
            final UserMemoryCard memoryCard = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).peekTopMemoryCard();
            response.setMessage("Answer: " + memoryCard.getAnswer() +
                    "\nDid you learned that answer ?");
            response.addButton(new BotNetButton("yes", "yes"));
            response.addButton(new BotNetButton("no", "no"));
            response.setInlineButtons(true);

            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.RATE);
        } else {
            response.setMessage("You has no saved words.");
        }

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/profile/rate", method = RequestMethod.POST)
    @ResponseBody
    public String rateAskedQuestionCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /rate form : " + request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        final UserMemoryCard userMemoryCard = hierarchy.getUserToUpdateByTelegramId(request.getUserChatId()).poolTopMemoryCard();
        if (userMemoryCard.updateNextAskingDate(request.getMessage())) {
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .addMemoryCard(userMemoryCard)
                    .setExpectedData(ExpectedData.NONE);
            response.setMessage("Your response is saved");
        } else {
            response.setMessage("Unexpected reply. Please send me : \n" +
                    "'yes' if you learned this word or \n" +
                    "'no' if you didn't learned this word");
            response.addButton(new BotNetButton("yes", "yes"));
            response.addButton(new BotNetButton("no", "no"));
        }

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }


    @RequestMapping(value = "/command/profile/statistics")
    @ResponseBody
    public String statisticsCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /statistics form : " + request.getUserChatId());

        final BotNetUser user = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());
        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.NONE);
        final BotNetResponse response = new BotNetResponse();
        final Gson jsonConverter = new Gson();
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage("Total saved words : " + user.statNumberOfSavedWords() + "\n" +
                "Total answered questions : " + user.statNumberOfAnsweredQuestions() + "\n" +
                "Total learned words : " + user.statNumberOfLearnedWords());

        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        return "OK";
    }

    @RequestMapping(value = "/command/profile/feedback", method = RequestMethod.POST)
    @ResponseBody
    public String feedbackCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /feedback form : " + request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        final Gson jsonConverter = new Gson();


        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.FEEDBACK) {
            response.setMessage("Thank you for your feedback.\nYour feedback was received.\nWe will revue it soon.");
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.NONE);

            //TODO send feedback to admins
            final BotNetResponse feedback = new BotNetResponse();
            feedback.setReceiverChatId(TELEGRAM_FEEDBACK_GROUP_ID);
            feedback.setMessage("#vois_feedback\nFrom " + request.getUserChatId() + ":\n" + request.getMessage());
            BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(feedback).getBytes());
        } else {
            response.setMessage("We appreciate your feedback.\nPlease send your feedback.");
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.FEEDBACK);
        }

        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        return "OK";
    }
}
