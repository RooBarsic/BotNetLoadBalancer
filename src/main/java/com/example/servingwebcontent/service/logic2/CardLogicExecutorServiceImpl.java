package com.example.servingwebcontent.service.logic2;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton2;
import com.example.servingwebcontent.service.ResponseService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CardLogicExecutorServiceImpl implements CardLogicExecutorService {
    private volatile CardImpl root;
    private final ConcurrentHashMap<String, CardImpl> cardByUserId;
    private final ResponseService responseService;

    @Autowired
    CardLogicExecutorServiceImpl(ResponseService responseService) {
        cardByUserId = new ConcurrentHashMap<>();
        root = null;
        this.responseService = responseService;

        CardImpl card = new CardImpl("Какого цвета шапка?", Arrays.asList(
                new ButtonImpl("белый",
                        new CardImpl("Есть ли пушок?", Arrays.asList(
                                new ButtonImpl("Да", new CardImpl("У вас белая шапка с пушком", Collections.EMPTY_LIST)),
                                new ButtonImpl("Нет", new CardImpl("У тебя в шапке нет пушка", Collections.EMPTY_LIST))
                        ))),
                new ButtonImpl("синий",
                        new CardImpl("Есть узор?", Arrays.asList(
                                new ButtonImpl("Да", new CardImpl("Синяя прица с узором :)))", Collections.EMPTY_LIST)),
                                new ButtonImpl("Нет", new CardImpl("Без узоршина!!!", Collections.EMPTY_LIST))
                        ))),
                new ButtonImpl("зелёный", new CardImpl("Зелёная шапка", Collections.EMPTY_LIST))
        ));

        setCardLogic(card);
        Gson gson = new Gson();
        System.out.println(gson.toJson(card));
    }

    @Override
    public void setCardLogic(CardImpl root) {
        this.root = root;
    }

    @Override
    public CardImpl getRootLogic() {
        return root;
    }

    @Override
    public boolean processIncomingRequest(BotNetRequest request) {
        CardImpl curCard = getOrCreateCard(request);

        CardImpl nextCard;
        String responseMessage;
        if (curCard.isList()) {
            nextCard = root;
            responseMessage = nextCard.getText();
        }
        else {
            ButtonImpl button = curCard.getButtonByText(request.getMessage());
            if (button == null) {
                nextCard = curCard;
                responseMessage = curCard.getErrorText() + "\n" + curCard.getText();
            } else {
                nextCard = button.getCard();
                responseMessage = nextCard.getText();
            }
        }

        generateAndSendResponse(responseMessage, nextCard, request);

        if (nextCard.isList()) {
            generateAndSendResponse(root.getText(), root, request);
        }
        return true;
    }

    private void generateAndSendResponse(String responseText, CardImpl card, BotNetRequest request) {
        BotNetResponse response = new BotNetResponse();
        response.setUiPlatform(request.getUiPlatform());
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage(responseText);
        for (ButtonImpl button1 : card.getButtons()) {
            response.addButton(new BotNetButton2(button1.getText()));
        }
        setCard(request, card);

        sendResponse(response); }

    private void sendResponse(BotNetResponse response) {
        responseService.addResponseToProcessingQueue(response);
    }


    private CardImpl getOrCreateCard(BotNetRequest request) {
        String userKey = getUSerKey(request);
        return cardByUserId.getOrDefault(userKey, root);
    }

    private CardImpl setCard(BotNetRequest request, CardImpl card) {
        String userKey = getUSerKey(request);
        cardByUserId.put(userKey, card);
        return card;
    }

    private String getUSerKey(BotNetRequest request) {
        return request.getUiPlatform() + "#" + request.getUserChatId();
    }
}