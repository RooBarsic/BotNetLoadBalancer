package com.example.servingwebcontent.components;


import com.example.message.BotNetRequest;
import com.example.message.data.BotNetUser;
import com.example.message.data.UserMemoryCard;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Hierarchy {
    private List<BotNetUser> usersList;
    private AtomicInteger requestsNumber = new AtomicInteger(0);

    Hierarchy() {
        usersList = new LinkedList<>();
    }

    public void updateHierarchy(final Hierarchy hierarchy) {
        for (final BotNetUser user : hierarchy.usersList) {
            if (containsThatTelegramId(user.getTelegramId())) {
                BotNetUser curUser = getUserToUpdateByTelegramId(user.getTelegramId());
                while (user.hasMemoryCards()) {
                    final UserMemoryCard memoryCard = user.poolTopMemoryCard();
                    if (!curUser.hasThatMemoryChard(memoryCard)) {
                        curUser.addMemoryCard(memoryCard);
                    }
                }
            } else {
                usersList.add(user.copyCurUser());
            }
        }
    }

    public BotNetUser getUserByTelegramId(final String userTelegramId) {
        for (BotNetUser user : usersList) {
            if (user.getTelegramId().equals(userTelegramId)) {
                return user.copyCurUser();
            }
        }
        return null;
    }

    public Hierarchy createUserByTelegramId(final String userTelegramId) {
        final BotNetUser botNetUser = new BotNetUser();
        botNetUser.setTelegramId(userTelegramId);
        usersList.add(botNetUser);
        return this;
    }

    public BotNetUser getUserToUpdateByTelegramId(final String userTelegramId) {
        for (BotNetUser user : usersList) {
            if (user.getTelegramId().equals(userTelegramId)) {
                return user;
            }
        }
        return new BotNetUser();
    }

    public boolean containsThatTelegramId(final String userTelegramId) {
        for (BotNetUser user : usersList) {
            if (user.getTelegramId().equals(userTelegramId)) {
                return true;
            }
        }
        return false;
    }

    public BotNetUser getOrCreateUserByTelegramId(@NotNull final String targetTelegramId) {
        for (final BotNetUser user : usersList) {
            if (user.getTelegramId().equals(targetTelegramId)) {
                return user;
            }
        }
        BotNetUser newUser = new BotNetUser();
        newUser.setTelegramId(targetTelegramId);
        usersList.add(newUser);
        return newUser;
    }

    public List<BotNetUser> getAllUsers() {
        return new ArrayList<>(usersList);
    }

    public int getRequestsNumber() {
        return requestsNumber.get();
    }

    public void resetRequestNum() {
        requestsNumber.set(0);
    }

    public void gotNewRequest() {
        requestsNumber.incrementAndGet();
    }
}
