package com.example.servingwebcontent.service;


import com.example.message.BotNetRequest;

public interface BotLogic {

    boolean processIncomingRequest(BotNetRequest request);
}
