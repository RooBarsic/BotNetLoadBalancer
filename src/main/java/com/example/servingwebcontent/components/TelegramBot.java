package com.example.servingwebcontent.components;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.api.bots.telegram.TelegramBotRequestListener;
import com.example.api.bots.telegram.TelegramBotResponseSender;
import com.example.message.BotNetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Telegram bot components
 * Has BotRequestListener and BotResponseSender for telegram bot
 *
 * @author Farrukh Karimov
 */
@Component
public class TelegramBot {
    private final String BOT_NAME_HANDLE = "TESTING_TELEGRAM_BOT_NAME";
    private final String BOT_TOKEN_HANDLE = "TESTING_TELEGRAM_BOT_TOKEN";
    private final TelegramBotRequestListener telegramBotRequestListener;
    private final TelegramBotResponseSender telegramBotResponseSender;
    private final ConcurrentLinkedDeque<BotNetRequest> receivedRequestsQueue;

    @Autowired
    TelegramBot(TokenStorage tokenStorage, BotNetRequestParser botNetRequestParser) {
        System.out.println("##### Starting Telegram bot ....... ");
        receivedRequestsQueue = botNetRequestParser.getReceivedRequestsQueue();
        ApiContextInitializer.init();

        final String BOT_NAME = tokenStorage.getTokens(BOT_NAME_HANDLE);
        final String BOT_TOKEN = tokenStorage.getTokens(BOT_TOKEN_HANDLE);
        telegramBotRequestListener = new TelegramBotRequestListener(BOT_NAME, BOT_TOKEN, receivedRequestsQueue);
        telegramBotRequestListener.botConnect();

        telegramBotResponseSender = (TelegramBotResponseSender) telegramBotRequestListener.getBotResponseSender();

        System.out.println("##### Telegram bot - started ....... ");
    }

    public BotRequestListener getRequestListener() {
        return telegramBotRequestListener;
    }

    public BotResponseSender getResponseSender() {
        return telegramBotResponseSender;
    }

    public List<BotNetRequest> getAndClearReceivedRequestsList(int responseSize) {
        List<BotNetRequest> requestList = new LinkedList<>();
        synchronized (receivedRequestsQueue) {
            for (BotNetRequest request : receivedRequestsQueue) {
                requestList.add(request);
                responseSize--;
                if (responseSize == 0) {
                    break;
                }
            }
        }
        return requestList;
    }
}
