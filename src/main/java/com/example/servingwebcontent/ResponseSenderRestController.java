package com.example.servingwebcontent;


import com.example.message.BotNetResponse;
import com.example.message.data.UiPlatform;
import com.example.servingwebcontent.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResponseSenderRestController {
    private final ResponseService responseService;

    @Autowired
    ResponseSenderRestController(final ResponseService responseService) {
        this.responseService = responseService;
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
            botNetResponse.setUiPlatform(UiPlatform.TELEGRAM);
            responseService.addResponseToProcessingQueue(botNetResponse);
            response += "OK";
        }
        return response;
    }

    @RequestMapping(value = "/send/telegram", method = RequestMethod.POST)
    public Boolean sendTelegramMessage(@RequestBody BotNetResponse response) {
        System.out.println("----- Telegram sender : got new request from POST");
        responseService.addResponseToProcessingQueue(response);
        return true;
    }
}
