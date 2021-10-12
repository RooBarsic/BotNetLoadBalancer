package com.example.servingwebcontent.service;

import com.example.message.BotNetResponse;

public interface ResponseService {
    void addResponseToProcessingQueue(BotNetResponse response);

    void processResponse(BotNetResponse response);
}
