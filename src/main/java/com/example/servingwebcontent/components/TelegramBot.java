package com.example.servingwebcontent.components;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.api.bots.telegram.TelegramBotRequestListener;
import com.example.api.bots.telegram.TelegramBotResponseSender;
import com.example.servingwebcontent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * Telegram bot components
 * Has BotRequestListener and BotResponseSender for telegram bot
 *
 * @author Farrukh Karimov
 */
@Component
public class TelegramBot implements BotNetBot {
    private final String BOT_NAME_HANDLE = "TESTING_TELEGRAM_BOT_NAME";
    private final String BOT_TOKEN_HANDLE = "TESTING_TELEGRAM_BOT_TOKEN";
    private final TelegramBotRequestListener telegramBotRequestListener;
    private final TelegramBotResponseSender telegramBotResponseSender;

    @Autowired
    TelegramBot(final TokenStorage tokenStorage,
                final RequestService requestService) {
        System.out.println("##### Starting Telegram bot ....... ");
//        ApiContextInitializer.init();
        telegramBotRequestListener = null;
        telegramBotResponseSender = null;
//        final String BOT_NAME = tokenStorage.getTokens(BOT_NAME_HANDLE);
//        final String BOT_TOKEN = tokenStorage.getTokens(BOT_TOKEN_HANDLE);
//        telegramBotRequestListener = new TelegramBotRequestListener(BOT_NAME, BOT_TOKEN, requestService);
//        telegramBotRequestListener.botConnect();
//
//        telegramBotResponseSender = (TelegramBotResponseSender) telegramBotRequestListener.getBotResponseSender();

        System.out.println("##### Telegram bot - started ....... ");
    }

    @Override
    public BotRequestListener getRequestListener() {
        return telegramBotRequestListener;
    }

    @Override
    public BotResponseSender getResponseSender() {
        return telegramBotResponseSender;
    }
}
