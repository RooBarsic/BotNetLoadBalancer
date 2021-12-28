package com.example.servingwebcontent.service.logic2;

import com.example.message.BotNetRequest;

public interface CardLogicExecutorService {
    void setCardLogic(CardImpl root);

    CardImpl getRootLogic();

    boolean processIncomingRequest(BotNetRequest request);
}
