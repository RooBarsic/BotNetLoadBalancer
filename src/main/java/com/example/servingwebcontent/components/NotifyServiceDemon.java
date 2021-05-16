package com.example.servingwebcontent.components;

import com.example.BotNetUtils;
import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component

public class NotifyServiceDemon {
    private final Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;
    private final String TELEGRAM_FEEDBACK_GROUP_ID;

    @Autowired
    NotifyServiceDemon(Hierarchy hierarchy, TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
        TELEGRAM_FEEDBACK_GROUP_ID = tokenStorage.getTokens("TELEGRAM_FEEDBACK_GROUP_ID");
        System.out.println("Notify service component created");
        this.hierarchy = hierarchy;
    }

    @Scheduled(fixedDelay = 6 * 60 * 60000)
    public void sendRemiders() {
        final Date notifyStartingDate = new Date();
        final Gson jsonConverter = new Gson();

        System.out.println("Started notify demon at " + notifyStartingDate);
        int numberOfNotifiedUsers = 0;
        int numberOfNotEmptyMemoryCardsList = 0;
        int numberOfSavedWords = 0;
        final List<BotNetUser> users = hierarchy.getAllUsers();
        for (final BotNetUser user : users) {
            if (user.hasMemoryCards()) {
                numberOfNotEmptyMemoryCardsList++;
                numberOfSavedWords += user.statNumberOfSavedWords();
                final UserMemoryCard memoryCard = user.peekTopMemoryCard();
                final ExpectedData expectedData = user.getExpectedData();
                if ((expectedData == ExpectedData.NONE) && (memoryCard.getNextAskingDay().compareTo(notifyStartingDate) < 0)) {
                    numberOfNotifiedUsers++;

                    final BotNetResponse response = new BotNetResponse();
                    response.setReceiverChatId(user.getTelegramId());
                    response.setMessage("It's time to learn word:\n" + memoryCard.getQuestion() + "\n\n" +
                            "/show");
                    response.addButton(new BotNetButton("show answer", "/show"));
                    user.setExpectedData(ExpectedData.SHOW_ANSWER);

                    BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
                }
            }
        }

        final BotNetResponse adminsReport = new BotNetResponse();
        adminsReport.setReceiverChatId(TELEGRAM_FEEDBACK_GROUP_ID);
        adminsReport.setMessage("Notify_report:\n" +
                "Notify service started at\n" + notifyStartingDate + "\n\n" +
                "Users : " + users.size() + "\n" +
                "Notified users : " + numberOfNotifiedUsers + "\n" +
                "Cards lists : " + numberOfNotEmptyMemoryCardsList + "\n" +
                "Total saved words : " + numberOfSavedWords + "\n" +
                "Received requests num : " + hierarchy.getRequestsNumber());
        hierarchy.resetRequestNum();
        BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(adminsReport).getBytes());
    }
}
