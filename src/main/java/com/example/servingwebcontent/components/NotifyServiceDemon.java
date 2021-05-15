package com.example.servingwebcontent.components;

import com.example.BotNetUtils;
import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import com.example.message.data.BotNetUser;
import com.example.message.data.UiPlatform;
import com.example.message.data.UserMemoryCard;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component

public class NotifyServiceDemon {
    private final Hierarchy hierarchy;
    protected final String TELEGRAM_RESPONSE_CONTROLLER = "http://localhost:8080/send/telegram";

    @Autowired
    NotifyServiceDemon(Hierarchy hierarchy) {
        System.out.println("Notify service component created");
        this.hierarchy = hierarchy;
    }

    @Scheduled(fixedDelay = 1 * 60000)
    public void sendRemiders() {
        System.out.println("Started notify demon at " + new Date());
        final List<BotNetUser> users = hierarchy.getAllUsers();
        for (final BotNetUser user : users) {
            if (user.hasMemoryCards()) {
                final UserMemoryCard memoryCard = user.peekTopMemoryCard();
                if (memoryCard.getNextAskingDay().compareTo(new Date()) < 0) {
                    final BotNetResponse response = new BotNetResponse();
                    response.setReceiverChatId(user.getTelegramId());
                    response.setMessage(user.getTelegramId());
                    response.setMessage(memoryCard.getQuestion());
                    response.addButton(new BotNetButton("show answer", "/show"));
                    response.addButton(new BotNetButton("remove word", "/remove"));

                    final Gson jsonConverter = new Gson();
                    BotNetUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
                }
            }
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void doRequestToNotDie() {
        System.out.println("doing request to google.com");
        BotNetUtils.httpsGETRequest("https://www.google.com/");
        System.out.println("finished request to google.com for not dieing");
    }
}
