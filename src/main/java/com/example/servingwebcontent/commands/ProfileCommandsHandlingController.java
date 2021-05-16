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

    @Autowired
    ProfileCommandsHandlingController(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
    }

    @RequestMapping(value = "/command/profile/add-words", method = RequestMethod.POST)
    @ResponseBody
    public String addWordsCommand(@RequestBody BotNetRequest request) {
        System.out.println("GOT add word command");
        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage("Send me word and it's definition. \n" +
                "Раздели вопрос от ответе используя символ '-'.\n" +
                "Например: Capital of Russia - Moscow;");
        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.MEMORY_CARD);

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/profile/show", method = RequestMethod.POST)
    @ResponseBody
    public String showAskedWordAnswerCommand(@RequestBody BotNetRequest request) {
        System.out.println("GOT show answer command");
        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        final UserMemoryCard memoryCard = hierarchy.getUserByTelegramId(request.getUserChatId()).peekTopMemoryCard();
        response.setMessage("Answer: " + memoryCard.getAnswer() +
                "\nDid you learned that answer ?");
        response.addButton(new BotNetButton("yes", "yes"));
        response.addButton(new BotNetButton("no", "no"));
        response.setInlineButtons(true);

        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.RATE);

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

}
