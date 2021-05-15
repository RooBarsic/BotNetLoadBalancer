package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Main {

    public static void main(String args[]) {
        final String helloUrl = "https://vious.herokuapp.com/hello";
        while (true) {
            System.out.println("Doing health check from additional worker");
            httpsGETRequest(helloUrl);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Method to make quick GET request and get the JSON response data*/
    public static String httpsGETRequest(final String urlPath) {
        try {
            HttpURLConnection con;
            URL myUrl = new URL(urlPath);
            con = (HttpURLConnection) myUrl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            StringBuilder content = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                br.lines().forEach( line -> {
                    content.append(line);
                    content.append(System.lineSeparator());
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return content.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
