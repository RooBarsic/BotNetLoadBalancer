package com.example.servingwebcontent.service;

import com.example.message.BotNetRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class RequestServiceImpl implements RequestService {
    private final ConcurrentLinkedDeque<BotNetRequest> requestsList;

    @Autowired
    RequestServiceImpl() {
        requestsList = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void addRequestToProcessingQueue(final @NotNull BotNetRequest request) {
        requestsList.addLast(request);
        System.out.println("Got new request");
    }

    @Override
    public boolean processRequest(final @NotNull BotNetRequest request) {

        return false;
    }

    @Scheduled(fixedDelay = 1 * 1 * 60000)
    private void processMessages() {
        while (!requestsList.isEmpty()) {
            synchronized (requestsList) {
                final BotNetRequest request = requestsList.getFirst();
                if (processRequest(request)) {
                    requestsList.pollFirst();
                } else {
                    break;
                }
            }
        }
    }


    @Override
    public List<BotNetRequest> getAndDeleteAllNotProcessedRequests() {
        final List<BotNetRequest> remainingRequests = new ArrayList<>();
        synchronized (requestsList) {
            while (!requestsList.isEmpty()) {
                remainingRequests.add(requestsList.pollFirst());
            }
        }
        return remainingRequests;
    }
}
