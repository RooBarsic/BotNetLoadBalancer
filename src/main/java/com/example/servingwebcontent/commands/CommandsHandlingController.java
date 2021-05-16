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
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /start form : " + request.getUserChatId());

        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("Salam. My name is Vious, I will help you to learn words, exam questions, anything you like.\n" +
                "Use command /add to add new word");

        final Gson jsonConverter = new Gson();
        System.out.println("TELEGRAM_RESPONSE_CONTROLLER = " + TELEGRAM_RESPONSE_CONTROLLER);
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/help", method = RequestMethod.POST)
    @ResponseBody
    public String helpCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /help form : " + request.getUserChatId());

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

    @RequestMapping(value = "/command/empty", method = RequestMethod.POST)
    @ResponseBody
    public String emptyCommand(@RequestBody BotNetRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: empty command form : " + request.getUserChatId());

        final BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage("No data expected :(");

        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());

        final Gson jsonConverter = new Gson();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }


}
