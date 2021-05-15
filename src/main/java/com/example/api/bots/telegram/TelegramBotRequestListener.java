package com.example.api.bots.telegram;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetRequest;
import com.example.message.data.UiPlatform;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Class for sending messages from Telegram bot to Telegram user
 *
 * @author Farrukh Karimov
 */
public class TelegramBotRequestListener extends TelegramLongPollingBot implements BotRequestListener {
    private final int RECONNECT_PAUSE = 10000;
    private final ConcurrentLinkedDeque<BotNetRequest> receivedRequestsQueue;

    @Setter
    @Getter
    private String botName;

    @Setter
    private String botToken;

    public TelegramBotRequestListener(@NotNull final String botName,
                                      @NotNull final String botToken,
                                      @NotNull final ConcurrentLinkedDeque<BotNetRequest> receivedRequestsQueue) {
        this.botName = botName;
        this.botToken = botToken;
        this.receivedRequestsQueue = receivedRequestsQueue;
    }

    @Override
    public void onUpdateReceived(Update update) {
        final BotNetRequest botNetRequest = new BotNetRequest();
        botNetRequest.setUiPlatform(UiPlatform.TELEGRAM);

        if (update.hasMessage()) {
            final Message telegramMessage = update.getMessage();
            botNetRequest.setUserChatId(Long.toString(telegramMessage.getChatId()));
            botNetRequest.setMessage(telegramMessage.getText());
        } else if (update.hasCallbackQuery()) {
            final CallbackQuery callbackQuery = update.getCallbackQuery();
            botNetRequest.setUserChatId(Long.toString(callbackQuery.getMessage().getChatId()));
            botNetRequest.setMessage(callbackQuery.getData());
        } else {
            return;
        }

        // add attachments if has some
        if (update.hasMessage()) {
            final Message telegramMessage = update.getMessage();
        }

        // add filled box to the processing queue
        receivedRequestsQueue.addLast(botNetRequest);
    }

    @Override
    public String getBotUsername() {
        //log.debug("Bot name: " + botName);
        System.out.println(" ### Request for Bot name");
        return botName;
    }

    @Override
    public String getBotToken() {
        //log.debug("Bot token: " + botToken);
        System.out.println(" ### Request for token");
        return botToken;
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            System.out.println(" ### Bot connecting....");
            telegramBotsApi.registerBot(this);
            //log.info("TelegramAPI started. Bot connected and waiting for messages");
        } catch (TelegramApiRequestException e) {
            //log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        }
    }

    @Override
    public BotResponseSender getBotResponseSender() {
        return new TelegramBotResponseSender(getOptions(), botToken);
    }
}

