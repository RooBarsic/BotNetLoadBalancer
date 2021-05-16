package com.example.servingwebcontent.components;

import com.example.BotNetUtils;
import com.example.message.BotNetRequest;
import com.example.message.data.ExpectedData;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class BotNetRequestParser {
    private final String START_COMMAND_REST_CONTROLLER;
    private final String HELP_COMMAND_REST_CONTROLLER;
    private final String FEEDBACK_COMMAND_REST_CONTROLLER;
    private final String EMPTY_COMMAND_CONTROLLER;
    private final String ADD_WORDS_COMMAND_CONTROLLER;
    private final String SHOW_LAST_ASKED_WORD_COMMAND_CONTROLLER;
    private final String RATE_LAST_ASKED_WORD_COMMAND_CONTROLLER;
    private final String STATISTICS_COMMAND_CONTROLLER;
    private final ConcurrentLinkedDeque<BotNetRequest> receivedRequestsQueue;
    private final Hierarchy hierarchy;

    @Autowired
    BotNetRequestParser(TokenStorage tokenStorage, Hierarchy hierarchy) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        START_COMMAND_REST_CONTROLLER = appUrl + "/command/start";
        HELP_COMMAND_REST_CONTROLLER = appUrl + "/command/help";
        EMPTY_COMMAND_CONTROLLER = appUrl + "/command/empty";
        ADD_WORDS_COMMAND_CONTROLLER = appUrl + "/command/profile/add-words";
        SHOW_LAST_ASKED_WORD_COMMAND_CONTROLLER = appUrl + "/command/profile/show";
        RATE_LAST_ASKED_WORD_COMMAND_CONTROLLER = appUrl + "/command/profile/rate";
        FEEDBACK_COMMAND_REST_CONTROLLER = appUrl + "/command/profile/feedback";
        STATISTICS_COMMAND_CONTROLLER = appUrl + "/command/profile/statistics";
        receivedRequestsQueue = new ConcurrentLinkedDeque<>();
        this.hierarchy = hierarchy;

        startRequestsProcessing();
    }

    /**
     * Process and redirect incoming request to required command REST controllers
     */
    public void startRequestsProcessing() {
        new Thread(() -> {
            final Gson jsonConverter = new Gson();
            while (true) {
                while (receivedRequestsQueue.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //process while has request
                while (!receivedRequestsQueue.isEmpty()) {
                    final BotNetRequest request = receivedRequestsQueue.getFirst();
                    receivedRequestsQueue.removeFirst();

                    //find controller url for requested command
                    final String commandRestController;
                    final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
                    if (request.getMessage().startsWith("/start")) {
                        commandRestController = START_COMMAND_REST_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/help")) {
                        commandRestController = HELP_COMMAND_REST_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/feedback")) {
                        commandRestController = FEEDBACK_COMMAND_REST_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/add-words")) {
                        commandRestController = ADD_WORDS_COMMAND_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/show")) {
                        commandRestController = SHOW_LAST_ASKED_WORD_COMMAND_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/statistics")) {
                        commandRestController = STATISTICS_COMMAND_CONTROLLER;
                    }
                    else {
                        switch (expectedData) {
                            case FEEDBACK:
                                commandRestController = FEEDBACK_COMMAND_REST_CONTROLLER;
                                break;
                            case MEMORY_CARD:
                                commandRestController = ADD_WORDS_COMMAND_CONTROLLER;
                                break;
                            case RATE:
                                commandRestController = RATE_LAST_ASKED_WORD_COMMAND_CONTROLLER;
                                break;
                            case SHOW_ANSWER:
                                commandRestController = SHOW_LAST_ASKED_WORD_COMMAND_CONTROLLER;
                                break;
                            default:
                                commandRestController = EMPTY_COMMAND_CONTROLLER;
                        }
                    }

                    BotNetUtils.httpsPOSTRequest(commandRestController, jsonConverter.toJson(request).getBytes());
                }
            }
        }).start();
    }

    public ConcurrentLinkedDeque<BotNetRequest> getReceivedRequestsQueue() {
        return receivedRequestsQueue;
    }
}
