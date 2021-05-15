package com.example.servingwebcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.ArrayList;

import static com.example.BotNetUtils.httpsGETRequest;

@SpringBootApplication
@EnableScheduling
public class ServingWebContentApplication {

    public static void main(String[] args) {
        if (args[0].equals("-web")) {
            SpringApplication.run(ServingWebContentApplication.class, args);
        } else {
            final String helloUrl = "https://vious.herokuapp.com/hello";
            while (true) {
                System.out.println("Health checking:: Doing health check from additional worker to " + helloUrl);
                String response = httpsGETRequest(helloUrl);
                if (response == null || response.equals("")) {
                    System.out.println("Health checking:: response is null or empty");
                } else {
                    System.out.println("Health checking:: response is = " + response);
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}