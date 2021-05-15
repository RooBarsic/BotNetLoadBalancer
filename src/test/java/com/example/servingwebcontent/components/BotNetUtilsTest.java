package com.example.servingwebcontent.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.BotNetUtils;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class BotNetUtilsTest {
    private final int SERVER_PORT = 8081;

    //@Test
    public void canMakeGetAndPostRequest() {
        final String getResponseData = "Hello BotNet World\r\n"; // used '\r\n' because last symbols in response would be like that
        final String postResponseData = "I got POSt request!!!`~~\r\n";
        final String postRequestData = "I'm trying to do Post request #1#@$#%$^%&*()*(&^&%$#@";
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

            new Thread(() -> {
                try {
                    httpServer.createContext("/doGet", (exchange) -> {
                        final int responseCode;
                        final String responseData;

                        if (exchange.getRequestMethod().equals("GET")) {
                            responseData = getResponseData;
                            responseCode = 200;
                        }
                        else if (exchange.getRequestMethod().equals("POST")) {
                            responseData = postResponseData;
                            responseCode = 200;

                            //check data received by POST
                            Scanner scanner = new Scanner(exchange.getRequestBody());
                            String receivedData = scanner.nextLine();
                            assertEquals(receivedData, postRequestData);
                        }
                        else {
                            responseCode = 400;
                            responseData = "";
                        }

                        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                        exchange.sendResponseHeaders(responseCode, responseData.getBytes().length);
                        OutputStream output = exchange.getResponseBody();
                        output.write(responseData.getBytes());
                        output.flush();
                        exchange.close();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            httpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("created handler");

        final String url = "http://localhost:" + SERVER_PORT + "/doGet";

        //testing GET request
        String receivedData = BotNetUtils.httpsGETRequest(url);
        System.out.println("receivedData = " + receivedData);
        System.out.println("getResponseData = " + getResponseData);
        assertEquals(receivedData, getResponseData);

        //testing POST request
        receivedData = BotNetUtils.httpsPOSTRequest(url, postRequestData.getBytes());
        assertEquals(receivedData, postResponseData);
    }
}
