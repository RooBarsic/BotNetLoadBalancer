package com.example.servingwebcontent.components;


import com.example.message.BotNetRequest;
import com.example.message.data.BotNetUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class Hierarchy {
    private List<BotNetUser> usersList;

    Hierarchy() {
        usersList = new LinkedList<>();
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
}
