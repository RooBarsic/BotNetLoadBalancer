package com.example.servingwebcontent.service.logic;


import com.example.message.BotNetRequest;

public interface BotLogic {

    boolean processIncomingRequest(BotNetRequest request);
}
