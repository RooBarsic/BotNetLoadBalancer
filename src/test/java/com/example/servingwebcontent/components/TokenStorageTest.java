//package com.example.servingwebcontent.components;
//
//import org.junit.jupiter.api.Test;
//
//import javax.validation.constraints.NotNull;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Scanner;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class TokenStorageTest {
//    private String SERVER_PORT = "8080";
//    private String PRODUCTION_BOT_TOKEN = "A";
//    private String PRODUCTION_BOT_NAME = "B";
//    private String TESTING_TELEGRAM_BOT_TOKEN = "C";
//    private String TESTING_TELEGRAM_BOT_NAME = "D";
//    private String TESTING_MAIL_RU_AGENT_BOT_TOKEN = "E";
//    private String TESTING_MAIL_RU_AGENT_BOT_NAME = "F";
//    private String TESTING_VK_BOT_TOKEN = "G";
//    private String TESTING_VK_BOT_ID = "H";
//    private String TESTING_FACEBOOK_BOT_VERIFY_TOKEN = "I";
//    private String TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN = "J";
//    private String TESTING_FACEBOOK_BOT_BASE_URL = "K";
//
//
//    public void compareTokens(@NotNull final TokenStorage tokensStorage) {
//
//        // compare
//        assertEquals(PRODUCTION_BOT_NAME, tokensStorage.getTokens("PRODUCTION_BOT_NAME"));
//        assertEquals(PRODUCTION_BOT_TOKEN, tokensStorage.getTokens("PRODUCTION_BOT_TOKEN"));
//
//        assertEquals(TESTING_TELEGRAM_BOT_NAME, tokensStorage.getTokens("TESTING_TELEGRAM_BOT_NAME"));
//        assertEquals(TESTING_TELEGRAM_BOT_TOKEN, tokensStorage.getTokens("TESTING_TELEGRAM_BOT_TOKEN"));
//
//        assertEquals(TESTING_MAIL_RU_AGENT_BOT_NAME, tokensStorage.getTokens("TESTING_MAIL_RU_AGENT_BOT_NAME"));
//        assertEquals(TESTING_MAIL_RU_AGENT_BOT_TOKEN, tokensStorage.getTokens("TESTING_MAIL_RU_AGENT_BOT_TOKEN"));
//
//        assertEquals(TESTING_VK_BOT_ID, tokensStorage.getTokens("TESTING_VK_BOT_ID"));
//        assertEquals(TESTING_VK_BOT_TOKEN, tokensStorage.getTokens("TESTING_VK_BOT_TOKEN"));
//
//        assertEquals(TESTING_FACEBOOK_BOT_VERIFY_TOKEN, tokensStorage.getTokens("TESTING_FACEBOOK_BOT_VERIFY_TOKEN"));
//        assertEquals(TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN, tokensStorage.getTokens("TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN"));
//        assertEquals(TESTING_FACEBOOK_BOT_BASE_URL, tokensStorage.getTokens("TESTING_FACEBOOK_BOT_BASE_URL"));
//
//        assertEquals(SERVER_PORT, tokensStorage.getTokens("SERVER_PORT"));
//
//    }
//
//    @Test
//    public void canReadTokensFromFile() {
//
//        try {
//            File file = new File(".env");
//            if (file.createNewFile()) {
//
//                FileWriter myWriter = new FileWriter(".env");
//                String text = "PRODUCTION_BOT_TOKEN=" + PRODUCTION_BOT_TOKEN + "\n"
//                        + "PRODUCTION_BOT_NAME=" + PRODUCTION_BOT_NAME + "\n"
//                        + "TESTING_TELEGRAM_BOT_TOKEN=" + TESTING_TELEGRAM_BOT_TOKEN + "\n"
//                        + "TESTING_TELEGRAM_BOT_NAME=" + TESTING_TELEGRAM_BOT_NAME + "\n"
//                        + "TESTING_MAIL_RU_AGENT_BOT_TOKEN=" + TESTING_MAIL_RU_AGENT_BOT_TOKEN + "\n"
//                        + "TESTING_MAIL_RU_AGENT_BOT_NAME=" + TESTING_MAIL_RU_AGENT_BOT_NAME + "\n"
//                        + "TESTING_VK_BOT_TOKEN=" + TESTING_VK_BOT_TOKEN + "\n"
//                        + "TESTING_VK_BOT_ID=" + TESTING_VK_BOT_ID + "\n"
//                        + "TESTING_FACEBOOK_BOT_VERIFY_TOKEN=" + TESTING_FACEBOOK_BOT_VERIFY_TOKEN + "\n"
//                        + "TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN=" + TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN + "\n"
//                        + "TESTING_FACEBOOK_BOT_BASE_URL=" + TESTING_FACEBOOK_BOT_BASE_URL + "\n"
//                        + "SERVER_PORT=" + SERVER_PORT + "\n";
//                myWriter.write(text);
//                myWriter.close();
//
//
//                try {
//                    // compare
//                    final TokenStorage tokensStorage = new TokenStorage();
//                    tokensStorage.addTokens();
//                    compareTokens(tokensStorage);
//                } finally {
//                    file.delete();
//                }
//
//            } else {
//                Scanner scanner = new Scanner(new File(".env"));
//
//                while (scanner.hasNextLine()) {
//                    String str = scanner.nextLine();
//                    String buff[] = str.split("=", 2);
//                    if (buff.length == 2)
//                        setToken(buff[0], buff[1]);
//                    else
//                        System.out.println("Error with .env file, wrong token format : " + str);
//                }
//
//                // compare
//                final TokenStorage tokensStorage = new TokenStorage();
//                tokensStorage.addTokens();
//                compareTokens(tokensStorage);
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//
//    }
//
//    private void setToken(@NotNull final String tokenName, @NotNull final String tokenValue) {
//        switch (tokenName) {
//            case "PORT": // because heroku gives available port by calling "PORT", not "SERVER_PORT"
//            case "SERVER_PORT":
//                SERVER_PORT = tokenValue;
//                break;
//            case "PRODUCTION_BOT_TOKEN":
//                PRODUCTION_BOT_TOKEN = tokenValue;
//                break;
//            case "PRODUCTION_BOT_NAME":
//                PRODUCTION_BOT_NAME = tokenValue;
//                break;
//            case "TESTING_TELEGRAM_BOT_TOKEN":
//                TESTING_TELEGRAM_BOT_TOKEN = tokenValue;
//                break;
//            case "TESTING_TELEGRAM_BOT_NAME":
//                TESTING_TELEGRAM_BOT_NAME = tokenValue;
//                break;
//            case "TESTING_MAIL_RU_AGENT_BOT_TOKEN":
//                TESTING_MAIL_RU_AGENT_BOT_TOKEN = tokenValue;
//                break;
//            case "TESTING_MAIL_RU_AGENT_BOT_NAME":
//                TESTING_MAIL_RU_AGENT_BOT_NAME = tokenValue;
//                break;
//            case "TESTING_VK_BOT_TOKEN":
//                TESTING_VK_BOT_TOKEN = tokenValue;
//                break;
//            case "TESTING_VK_BOT_ID":
//                TESTING_VK_BOT_ID = tokenValue;
//                break;
//            case "TESTING_FACEBOOK_BOT_VERIFY_TOKEN":
//                TESTING_FACEBOOK_BOT_VERIFY_TOKEN = tokenValue;
//                break;
//            case "TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN":
//                TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN = tokenValue;
//                break;
//            case "TESTING_FACEBOOK_BOT_BASE_URL":
//                TESTING_FACEBOOK_BOT_BASE_URL = tokenValue;
//                break;
//        }
//    }
//
//    //@Test
//    public void canReadTokensFromEnvironmentVariables() {
//        SERVER_PORT = System.getenv("PORT"); // heroku gives available port by calling "PORT", not "SERVER_PORT"
//
//        PRODUCTION_BOT_TOKEN = System.getenv("PRODUCTION_BOT_TOKEN");
//        PRODUCTION_BOT_NAME = System.getenv("PRODUCTION_BOT_NAME");
//
//        TESTING_TELEGRAM_BOT_TOKEN = System.getenv("TESTING_TELEGRAM_BOT_TOKEN");
//        TESTING_TELEGRAM_BOT_NAME = System.getenv("TESTING_TELEGRAM_BOT_NAME");
//
//        TESTING_MAIL_RU_AGENT_BOT_TOKEN = System.getenv("TESTING_MAIL_RU_AGENT_BOT_TOKEN");
//        TESTING_MAIL_RU_AGENT_BOT_NAME = System.getenv("TESTING_MAIL_RU_AGENT_BOT_NAME");
//
//        TESTING_VK_BOT_ID = System.getenv("TESTING_VK_BOT_ID");
//        TESTING_VK_BOT_TOKEN = System.getenv("TESTING_VK_BOT_TOKEN");
//
//        TESTING_FACEBOOK_BOT_VERIFY_TOKEN = System.getenv("TESTING_FACEBOOK_BOT_VERIFY_TOKEN");
//        TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN = System.getenv("TESTING_FACEBOOK_BOT_PAGE_ACCESS_TOKEN");
//        TESTING_FACEBOOK_BOT_BASE_URL = System.getenv("TESTING_FACEBOOK_BOT_BASE_URL");
//
//        // compare
//        final TokenStorage tokensStorage = new TokenStorage();
//        tokensStorage.addTokens();
//        compareTokens(tokensStorage);
//
//    }
//}
