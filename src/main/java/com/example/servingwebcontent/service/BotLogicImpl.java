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
        response.setMessage("Hello " + request.getMessage());

        response.addButton(new BotNetButton("help", "help"));
        response.addButton(new BotNetButton("start", "start"));
        response.addButton(new BotNetButton("info", "info"));

        response.setNewButtonsLine();
        response.addButton(new BotNetButton("locations", "locations"));
        response.addButton(new BotNetButton("masters", "masters"));
        response.addButton(new BotNetButton("free slots", "free slots"));

        sendResponse(response);
        return true;
    }

    private void sendResponse(BotNetResponse response) {
        responseService.addResponseToProcessingQueue(response);
    }
}
