package com.example.servingwebcontent;

import com.example.message.BotNetRequest;
import com.example.servingwebcontent.components.TelegramBot;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestProviderRestController {
    private final TelegramBot telegramBot;

    @Autowired
    RequestProviderRestController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloGET() {
        System.out.println("Got Hello request");
        return "greeting";
    }


    @RequestMapping(value = "/requests/telegram", method = RequestMethod.GET)
    @ResponseBody
    public String sendTelegramMessageGET(@RequestParam(name = "size", required = false, defaultValue = "10") int pageSize) {
        System.out.println("Got GET");
        final List<BotNetRequest> telegramRequestsList = telegramBot.getAndClearReceivedRequestsList(pageSize);
        final Gson jsonConverter = new Gson();
        final String jsonResponse = jsonConverter.toJson(telegramRequestsList);
        return jsonResponse;
    }

    @RequestMapping(value = "/requests/telegram", method = RequestMethod.POST)
    @ResponseBody
    public String sendTelegramMessagePOST(@RequestPart(name = "size") int pageSize,
                                          @RequestPart(name = "secretKey") String secretKey) {
        System.out.println("Got POST, key = " + secretKey + " pageSize = " + pageSize);
        final List<BotNetRequest> telegramRequestsList = telegramBot.getAndClearReceivedRequestsList(pageSize);
        final Gson jsonConverter = new Gson();
        final String jsonResponse = jsonConverter.toJson(telegramRequestsList);
        return jsonResponse;
    }
}

