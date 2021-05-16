package com.example.message.data;

import java.util.*;

/**
 * about user
 * User : botnet_id, telegram_id, telegram_login
 *     words : [question, ans, number_of_repeats, next_asking_day], ......
 */
public class BotNetUser {
    private int botNetId = 0;
    private static int MAX_SAVED_CARDS = 50;
    private int numberOfAnsweredQuestions = 0;
    private String telegramId = "";
    private String telegramLogin = "";
    private PriorityQueue<UserMemoryCard> userMemoryCards;

    private ExpectedData expectedData = ExpectedData.NONE;

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
        copy.setBotNetId(botNetId);
        copy.setTelegramId(telegramId);
        copy.numberOfAnsweredQuestions = numberOfAnsweredQuestions;
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

    public boolean hasSpaceForNewMemoryCard() {
        return userMemoryCards.size() < MAX_SAVED_CARDS;
    }

    public boolean hasThatMemoryChard(final UserMemoryCard memoryCard) {
        for (UserMemoryCard memoryCard1 : userMemoryCards) {
            if (memoryCard.getQuestion().equals(memoryCard1.getQuestion()) &&
                memoryCard.getAnswer().equals(memoryCard1.getAnswer())) {
                return true;
            }
        }
        return false;
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

    /**Statistics: number of total saved words*/
    public int statNumberOfSavedWords() {
        return userMemoryCards.size();
    }

    /**Statistics: number of total answered questions*/
    public int statNumberOfAnsweredQuestions() {
        return numberOfAnsweredQuestions;
    }

    /**Statistics: number of learned words*/
    public int statNumberOfLearnedWords() {
        int numberOfLearnedWords = 0;
        for (final UserMemoryCard memoryCard : userMemoryCards) {
            if (memoryCard.isLearned()) {
                numberOfLearnedWords++;
            }
        }
        return numberOfLearnedWords;
    }

}
