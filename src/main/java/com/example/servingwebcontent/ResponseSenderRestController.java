package com.example.servingwebcontent;


import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import com.example.servingwebcontent.components.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResponseSenderRestController {
    private final TelegramBot telegramBot;

    @Autowired
    ResponseSenderRestController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/send/telegram", method = RequestMethod.GET)
    public String sendTelegramMessage(@RequestParam(name = "chatId", required = false, defaultValue = "") String chatId,
                                      @RequestParam(name = "message", required = false, defaultValue = "") String message) {
        String response = "";
        if (chatId.equals("")) {
            response += "Error: No chatId\n";
        }
        if (message.equals("")) {
            response += "Error: No message to send\n";
        }
        if (response.length() == 0) {
            final BotNetResponse botNetResponse = new BotNetResponse();
            botNetResponse.setReceiverChatId(chatId);
            botNetResponse.setMessage(message);
            telegramBot.getResponseSender()
                    .sendBotNetResponse(botNetResponse);
            response += "OK";
        }
        return response;
    }

    @RequestMapping(value = "/send/telegram", method = RequestMethod.POST)
    public String sendTelegramMessage(@RequestBody BotNetResponse response) {
        System.out.println("----- Telegram sender : got new request from POST");
        String responseStatus = "";
        if (response.getReceiverChatId().equals("")) {
            responseStatus += "Error: No chatId\n";
        }
        if (response.getMessage().equals("")) {
            responseStatus += "Error: No message to send\n";
        }
        if (responseStatus.length() == 0) {
            if (!response.hasButtons()) {
                response.addButton(new BotNetButton("add words", "/add-words"));
                response.addButton(new BotNetButton("help", "/help"));
                response.addButton(new BotNetButton("feedback", "/feedback"));
                response.setNewButtonsLine();
                response.addButton(new BotNetButton("statistics", "/statistics"));
                response.setInlineButtons(true);
            }

            telegramBot.getResponseSender()
                    .sendBotNetResponse(response);
            responseStatus += "OK";
        }
        return responseStatus;
    }
}
