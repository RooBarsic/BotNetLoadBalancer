package com.example.servingwebcontent.components;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.api.bots.agent.MailRuAgentBotRequestListener;
import com.example.api.bots.agent.MailRuAgentBotRequestSender;
import com.example.servingwebcontent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MailRuAgentBot implements BotNetBot {
    private final String BOT_NAME_HANDLE = "TESTING_MAIL_RU_AGENT_BOT_NAME";
    private final String BOT_TOKEN_HANDLE = "TESTING_MAIL_RU_AGENT_BOT_TOKEN";
    private final MailRuAgentBotRequestListener mailRuAgentBotRequestListener;
    private final MailRuAgentBotRequestSender mailRuBotResponseSender;

    @Autowired
    MailRuAgentBot(final TokenStorage tokenStorage,
                final RequestService requestService) {
        System.out.println("##### Starting MailRuAgent bot ....... ");
        final String BOT_NAME = tokenStorage.getTokens(BOT_NAME_HANDLE);
        final String BOT_TOKEN = tokenStorage.getTokens(BOT_TOKEN_HANDLE);
        mailRuAgentBotRequestListener = new MailRuAgentBotRequestListener(BOT_TOKEN, requestService);
        mailRuAgentBotRequestListener.startBot();

        mailRuBotResponseSender = (MailRuAgentBotRequestSender) mailRuAgentBotRequestListener.getBotResponseSender();

        System.out.println("##### MailRuAgent bot - started ....... ");
    }

    @Override
    public BotRequestListener getRequestListener() {
        return mailRuAgentBotRequestListener;
    }

    @Override
    public BotResponseSender getResponseSender() {
        return mailRuBotResponseSender;
    }
}
