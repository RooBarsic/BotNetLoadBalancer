package com.example.servingwebcontent.service;

import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetResponse;
import com.example.message.data.UiPlatform;
import com.example.servingwebcontent.components.MailRuAgentBot;
import com.example.servingwebcontent.components.TelegramBot;
import com.example.servingwebcontent.components.VkBot;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class ResponseServiceImpl implements ResponseService {
    private final ConcurrentLinkedDeque<BotNetResponse> responsesList;
    private final Map<UiPlatform, BotResponseSender> responseSenderByPlatform;

    @Autowired
    ResponseServiceImpl(TelegramBot telegramBot,
                        VkBot vkBot,
                        MailRuAgentBot mailRuAgentBot) {
        responsesList = new ConcurrentLinkedDeque<>();
        responseSenderByPlatform = new HashMap<>();
        responseSenderByPlatform.put(UiPlatform.TELEGRAM, telegramBot.getResponseSender());
        responseSenderByPlatform.put(UiPlatform.VK, vkBot.getResponseSender());
        responseSenderByPlatform.put(UiPlatform.MAIL_RU_AGENT, mailRuAgentBot.getResponseSender());
    }

    @Override
    public void addResponseToProcessingQueue(BotNetResponse response) {
        responsesList.add(response);
    }

    @Override
    public void processResponse(final @NotNull BotNetResponse response) {
        BotResponseSender responseSender = responseSenderByPlatform.get(response.getUiPlatform());
        responseSender.sendBotNetResponse(response);
    }

    @Scheduled(fixedDelay = 1 * 1 * 10000)
    private void processMessages() {
        while (!responsesList.isEmpty()) {
            BotNetResponse response = responsesList.pollFirst();
            processResponse(response);
        }
    }
}
