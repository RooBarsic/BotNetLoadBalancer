package com.example.servingwebcontent.service;

import com.example.message.BotNetRequest;

import java.util.List;

public interface RequestService {

    void addRequestToProcessingQueue(BotNetRequest request);

    boolean processRequest(BotNetRequest request);

    List<BotNetRequest> getAndDeleteAllNotProcessedRequests();
}
