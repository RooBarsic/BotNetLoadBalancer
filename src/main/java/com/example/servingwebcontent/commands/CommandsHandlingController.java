package com.example.servingwebcontent.commands;


import com.example.BotNetUtils;
import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import com.example.message.data.ExpectedData;
import com.example.message.data.UserMemoryCard;
import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommandsHandlingController {
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;

    @Autowired
    CommandsHandlingController(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
    }

    @RequestMapping(value = "/command/start", method = RequestMethod.POST)
    @ResponseBody
    public String startCommand(@RequestBody BotNetRequest request) {
        System.out.println("Got START command");
        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("Salam. My name is Vious, I will help you to learn words, exam questions, anything you like.\n" +
                "Use command /add to add new word");
        response.addButton(new BotNetButton("add words", "/add-words"));
        response.addButton(new BotNetButton("help", "/help"));
        response.addButton(new BotNetButton("feedback", "/feedback"));
        response.setInlineButtons(true);

        final Gson jsonConverter = new Gson();
        System.out.println("TELEGRAM_RESPONSE_CONTROLLER = " + TELEGRAM_RESPONSE_CONTROLLER);
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/help", method = RequestMethod.POST)
    @ResponseBody
    public String helpCommand(@RequestBody BotNetRequest request) {
        System.out.println("GOT HELP COMMAND");
        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("/start - it's start command\n" +
                "/help - use to get some help\n" +
                "/feedback - use to write feedback");


        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/feedback", method = RequestMethod.POST)
    @ResponseBody
    public String feedbackCommand(@RequestBody BotNetRequest request) {
        System.out.println("GOT FEEDBACK COMMAND");
        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("Thank you for your feedback. Your feedback was received. We will revue it soon.");

        //TODO send feedback to admins

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/empty", method = RequestMethod.POST)
    @ResponseBody
    public String emptyCommand(@RequestBody BotNetRequest request) {
        System.out.println("GOT EMPTY COMMAND");
        final String userTelegramId = request.getUserChatId();
        final String receivedMessage = request.getMessage();

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        if (hierarchy.containsThatTelegramId(request.getUserChatId())) {

            ExpectedData expectedData = hierarchy.getUserByTelegramId(request.getUserChatId()).getExpectedData();
            switch (expectedData) {
                case RATE:
                    final UserMemoryCard userMemoryCard = hierarchy.getUserToUpdateByTelegramId(userTelegramId).poolTopMemoryCard();
                    if (userMemoryCard.updateNextAskingDate(receivedMessage)) {
                        hierarchy.getUserToUpdateByTelegramId(userTelegramId)
                                .addMemoryCard(userMemoryCard);
                        response.setMessage("Your response is saved");
                    } else {
                        response.setMessage("Unexpected reply. Please send me : \n" +
                                "'yes' if you learned this word or \n" +
                                "'no' if you didn't learned this word");
                        response.addButton(new BotNetButton("yes", "yes"));
                        response.addButton(new BotNetButton("no", "no"));
                    }
                    break;
                case MEMORY_CARD:
                    final String buff[] = request.getMessage().split("-|\n");
                    StringBuilder responseMessage = new StringBuilder("Identified word :\n");
                    for (int i = 1; i < buff.length; i += 2) {
                        final String question = buff[i - 1];
                        final String answer = buff[i];
                        final UserMemoryCard memoryCard = new UserMemoryCard(question, answer);
                        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                                .addMemoryCard(memoryCard)
                                .setExpectedData(ExpectedData.NONE);
                        responseMessage.append("word : ").append(question)
                                .append("\n").append("answer : ").append(answer)
                                .append("\n");
                    }
                    response.setMessage(responseMessage.toString());
                    break;
                default:
                    response.setMessage("No data expected :(");
                    break;
            }
        }


        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }


}
