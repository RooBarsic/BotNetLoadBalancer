package com.example.servingwebcontent.components;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;

public interface BotNetBot {

    BotRequestListener getRequestListener();

    BotResponseSender getResponseSender();

}
