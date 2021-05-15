package com.example.api.bots;

import com.example.message.BotNetResponse;
import com.example.message.data.BotNetFile;
import org.jetbrains.annotations.NotNull;

public interface BotResponseSender {

    boolean sendBotNetResponse(@NotNull final BotNetResponse botNetResponse);

    boolean sendBotNetFile(@NotNull final String chatId, @NotNull final BotNetFile botNetFile);
}
