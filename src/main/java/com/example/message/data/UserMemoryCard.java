package com.example.message.data;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *  [question, ans, number_of_repeats, next_asking_day]
 */
public class UserMemoryCard {
    private String question;
    private String answer;
    private boolean learned;
    private Date nextAskingDay;
    private int numberOfAsking;

    public UserMemoryCard(final String question, final String answer) {
        this.question = question;
        this.answer = answer;
        nextAskingDay = new Date();
        numberOfAsking = 0;
    }

    public UserMemoryCard() {
        this.question = "";
        this.answer = "";
        nextAskingDay = new Date();
        numberOfAsking = 0;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getNextAskingDay() {
        return nextAskingDay;
    }

    public void setNextAskingDay(Date nextAskingDay) {
        this.nextAskingDay = nextAskingDay;
    }

    public int getNumberOfAsking() {
        return numberOfAsking;
    }

    public boolean isLearned() { return learned; }

    /**
     * @param rate expected to be 'yes' or 'no' if user learned or didn't learned the word
     * @return
     */
    public boolean updateNextAskingDate(final String rate) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextAskingDay);
        if (rate.equals("yes")) {
            numberOfAsking++;
            calendar.add(Calendar.DATE, numberOfAsking * 50);
//            if (learned) {
//                calendar.add(Calendar.DATE, numberOfAsking * 20);
//            } else {
//                learned = true;
//                calendar.add(Calendar.DATE, (numberOfAsking * 20) / 2);
//            }
        } else if (rate.equals("no")) {
            numberOfAsking++;
            Random random = new Random();
            int x = Math.abs(random.nextInt()) % 4 + 1;
            calendar.add(Calendar.DATE, x);
        } else {
            return false;
        }
        System.out.println(" nextAskingDay was = " + nextAskingDay);
        nextAskingDay = calendar.getTime();
        System.out.println(" nextAskingDay becomes = " + nextAskingDay);
        return true;
    }
}
