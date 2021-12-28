package com.example.servingwebcontent.service.alerts;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PrometheusAlertManagerServiceImpl implements AlertManagerService {
    private final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(500);
    private final Duration READ_TIMEOUT = Duration.ofSeconds(500);
    private final String TRANSACTION_REQUEST_URL = "http://localhost:9093";
    private final ConcurrentLinkedQueue<String> messagesQueue;
    private final RestTemplate restTemplate;


    PrometheusAlertManagerServiceImpl(final RestTemplateBuilder restTemplateBuilder) {
        messagesQueue = new ConcurrentLinkedQueue<>();
        restTemplate = restTemplateBuilder
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setReadTimeout(READ_TIMEOUT)
            .build();
    }

    @Override
    public void addMessageToProcessingQueue(String alertMessage) {
        messagesQueue.add(alertMessage);
    }

    @Override
    public void addMessagesToProcessingQueue(List<String> alertMessage) {
        messagesQueue.addAll(alertMessage);
    }

    //@Scheduled(fixedDelay = 1000)
    private void processQueuedMessages() {
        while (!messagesQueue.isEmpty()) {
            String message = messagesQueue.peek();
            if (sendMessage(message)) {
                messagesQueue.poll();
            }
        }
    }

    private boolean sendMessage(String message) {
        try {
            // create a bodyMap for post parameters
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("startsAt", "2021-12-02T08:39:15.345Z");
            bodyMap.put("endsAt", "2021-12-02T08:39:15.345Z");
            bodyMap.put("generatorURL", "string");
//            bodyMap.put("startsAt", "startsAt");
//            bodyMap.put("startsAt", "startsAt");
//            bodyMap.put("startsAt", "startsAt");
//            bodyMap.put("startsAt", "startsAt");


            ResponseEntity<Object> response = doPost(bodyMap, Object.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("External service : " + e.getMessage());
            return false;
        }
    }

    private ResponseEntity doPost(Map<String, Object> bodyMap, Class responseObjectClass) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bodyMap, headers);

        // send POST request
        return restTemplate.postForEntity(TRANSACTION_REQUEST_URL, entity, responseObjectClass);

    }
}
