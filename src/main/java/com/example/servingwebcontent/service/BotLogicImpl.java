package com.example.servingwebcontent.service;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import org.springframework.stereotype.Service;


@Service
public class BotLogicImpl implements BotLogic {
    private final ResponseService responseService;

    BotLogicImpl(final ResponseService responseService) {
        this.responseService = responseService;
    }

    @Override
    public boolean processIncomingRequest(BotNetRequest request) {
        //configure response
        BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        response.setUiPlatform(request.getUiPlatform());

        // logic
        String message = request.getMessage();


        if (message.startsWith("Записаться")) {
            response.setMessage("Sorry " + request.getMessage() + ". Not implemented yet.");
            response.addButton(new BotNetButton("Записаться", "Записаться"));
            response.addButton(new BotNetButton("info", "info"));
            response.addButton(new BotNetButton("help", "help"));
        }
        else if (message.startsWith("info")) {
            response.setMessage("Our company provides best barbershop services in Saint-Petersburg");
            response.addButton(new BotNetButton("Записаться", "Записаться"));
            response.addButton(new BotNetButton("info", "info"));
            response.addButton(new BotNetButton("help", "help"));
        }
        else if (message.startsWith("help")) {
            response.setMessage("How can I help you?");
        } else {
            response.setMessage("Hello " + request.getMessage());
            response.addButton(new BotNetButton("Записаться", "Записаться"));
            response.addButton(new BotNetButton("info", "info"));
            response.addButton(new BotNetButton("help", "help"));
        }

        sendResponse(response);
        return true;
    }

    private void sendResponse(BotNetResponse response) {
        responseService.addResponseToProcessingQueue(response);
    }
}
