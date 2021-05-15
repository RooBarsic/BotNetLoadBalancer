package com.example;

import com.example.servingwebcontent.components.TokenStorage;

public class HealthChecker {

    public static void main(String args[]) {
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.addTokens();
        final String helloUrl = tokenStorage.getTokens("APP_HEROKU_URL") + "/hello";
        while (true) {
            System.out.println("Doing health check from additional worker");
            BotNetUtils.httpsGETRequest(helloUrl);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
