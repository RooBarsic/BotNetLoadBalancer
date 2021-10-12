package com.example.servingwebcontent.components;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.api.bots.telegram.TelegramBotRequestListener;
import com.example.api.bots.telegram.TelegramBotResponseSender;
import com.example.api.bots.vk.VkBotRequestListener;
import com.example.api.bots.vk.VkBotRequestSender;
import com.example.servingwebcontent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VkBot implements BotNetBot {
    private final String BOT_NAME_HANDLE = "TESTING_VK_BOT_ID";
    private final String BOT_TOKEN_HANDLE = "TESTING_VK_BOT_TOKEN";
    private final VkBotRequestListener vkBotRequestListener = null;
    private final VkBotRequestSender vkBotResponseSender = null;

    @Autowired
    VkBot(final TokenStorage tokenStorage,
                final RequestService requestService) {
//        System.out.println("##### Starting VK bot ....... ");
//        final int BOT_NAME = Integer.parseInt(tokenStorage.getTokens(BOT_NAME_HANDLE));
//        final String BOT_TOKEN = tokenStorage.getTokens(BOT_TOKEN_HANDLE);
//        vkBotRequestListener = new VkBotRequestListener(BOT_NAME, BOT_TOKEN, requestService);
//
//        vkBotResponseSender = (VkBotRequestSender) vkBotRequestListener.getBotResponseSender();
//
//        System.out.println("##### VK bot - started ....... ");

    }

    @Override
    public BotRequestListener getRequestListener() {
        return vkBotRequestListener;
    }

    @Override
    public BotResponseSender getResponseSender() {
        return vkBotResponseSender;
    }
}
