package com.example.message.data;

import java.util.*;

/**
 * about user
 * User : botnet_id, telegram_id, telegram_login
 *     words : [question, ans, number_of_repeats, next_asking_day], ......
 */
public class BotNetUser {
    private int botNetId = 0;
    private String telegramId = "";
    private String telegramLogin = "";
    private PriorityQueue<UserMemoryCard> userMemoryCards;

    private ExpectedData expectedData;

    public BotNetUser() {
        userMemoryCards = new PriorityQueue<>(new Comparator<UserMemoryCard>() {
            @Override
            public int compare(UserMemoryCard o1, UserMemoryCard o2) {
                return o1.getNextAskingDay().compareTo(o2.getNextAskingDay());
            }
        });
    }

    public BotNetUser copyCurUser() {
        final BotNetUser copy = new BotNetUser();
        copy.setTelegramId(telegramId);
        copy.setTelegramLogin(telegramLogin);
        copy.setExpectedData(expectedData);
        copy.setUserMemoryCards(new PriorityQueue<>(userMemoryCards));
        return copy;
    }

    public UserMemoryCard poolTopMemoryCard() {
        if (userMemoryCards.size() == 0) {
            return null;
        }
        return userMemoryCards.poll();
    }

    public UserMemoryCard peekTopMemoryCard() {
        if (userMemoryCards.size() == 0) {
            return null;
        }
        return userMemoryCards.peek();
    }


    public BotNetUser addMemoryCard(final UserMemoryCard memoryCard) {
        userMemoryCards.add(memoryCard);
        return this;
    }

    public boolean hasMemoryCards() {
        return !userMemoryCards.isEmpty();
    }

    public BotNetUser setUserMemoryCards(PriorityQueue<UserMemoryCard> userMemoryCards) {
        this.userMemoryCards.clear();
        this.userMemoryCards.addAll(userMemoryCards);
        return this;
    }


    public ExpectedData getExpectedData() {
        return expectedData;
    }

    public BotNetUser setExpectedData(ExpectedData expectedData) {
        this.expectedData = expectedData;
        return this;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public BotNetUser setTelegramId(String telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getTelegramLogin() {
        return telegramLogin;
    }

    public BotNetUser setTelegramLogin(String telegramLogin) {
        this.telegramLogin = telegramLogin;
        return this;
    }

    public int getBotNetId() {
        return botNetId;
    }

    public void setBotNetId(int botNetId) {
        this.botNetId = botNetId;
    }

}
