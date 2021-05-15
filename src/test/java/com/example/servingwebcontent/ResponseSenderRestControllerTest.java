package com.example.servingwebcontent;

import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetResponse;
import com.example.servingwebcontent.components.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ResponseSenderRestControllerTest {

    @Test
    public void doNotSendResponseWithoutCorrectParameters() {
        final String NO_CHAT_ID_NO_MESSAGE = "Error: No chatId\nError: No message to send\n";
        final String NO_CHAT_ID = "Error: No chatId\n";
        final String NO_MESSAGE = "Error: No message to send\n";
        final String OK = "OK";

        final BotResponseSender responseSenderMock = Mockito.mock(BotResponseSender.class);
        when(responseSenderMock.sendBotNetResponse(any(BotNetResponse.class))).thenReturn(true);
        final TelegramBot telegramBotMock = Mockito.mock(TelegramBot.class);
        when(telegramBotMock.getResponseSender()).thenReturn(responseSenderMock);

        final ResponseSenderRestController responseSenderController = new ResponseSenderRestController(telegramBotMock);

        assertEquals(NO_CHAT_ID_NO_MESSAGE, responseSenderController.sendTelegramMessage("", ""));

        assertEquals(NO_CHAT_ID, responseSenderController.sendTelegramMessage("", "Hello"));

        assertEquals(NO_MESSAGE, responseSenderController.sendTelegramMessage("123456", ""));

        assertEquals(OK, responseSenderController.sendTelegramMessage("1234", "Hello"));
    }

    @Test
    public void doNotCallTelegramResponseForIncorrectParameters() {
        final AtomicInteger responseCounter = new AtomicInteger(0);
        final BotResponseSender responseSenderMock = Mockito.mock(BotResponseSender.class);
        when(responseSenderMock.sendBotNetResponse(any(BotNetResponse.class))).then((Answer<Boolean>) invocation -> {
            responseCounter.incrementAndGet();
            return true;
        });

        final TelegramBot telegramBotMock = Mockito.mock(TelegramBot.class);

        when(telegramBotMock.getResponseSender()).thenReturn(responseSenderMock);

        final ResponseSenderRestController responseSenderController = new ResponseSenderRestController(telegramBotMock);

        responseSenderController.sendTelegramMessage("", "");
        assertEquals(0, responseCounter.get());

        responseSenderController.sendTelegramMessage("", "Hello");
        assertEquals(0, responseCounter.get());

        responseSenderController.sendTelegramMessage("123456", "");
        assertEquals(0, responseCounter.get());

        responseSenderController.sendTelegramMessage("1234", "Hello");
        assertEquals(1, responseCounter.get());
    }

    @Test
    public void sendOneRequestPerCall() {
        final AtomicInteger responseCounter = new AtomicInteger(0);
        final BotResponseSender responseSenderMock = Mockito.mock(BotResponseSender.class);
        when(responseSenderMock.sendBotNetResponse(any(BotNetResponse.class))).then((Answer<Boolean>) invocation -> {
            responseCounter.incrementAndGet();
            return true;
        });

        final TelegramBot telegramBotMock = Mockito.mock(TelegramBot.class);
        when(telegramBotMock.getResponseSender()).thenReturn(responseSenderMock);

        final ResponseSenderRestController responseSenderController = new ResponseSenderRestController(telegramBotMock);
        Random random = new Random();
        int numberOfGoodCalls = 0;
        for (int i = 1; i <= 100; i++) {
            if (random.nextInt() % 2 == 0) {
                responseSenderController.sendTelegramMessage("123", "Hello");
                numberOfGoodCalls++;
            }
            else {
                responseSenderController.sendTelegramMessage("", "");
            }
        }
        assertEquals(responseCounter.get(), numberOfGoodCalls);
    }
}
