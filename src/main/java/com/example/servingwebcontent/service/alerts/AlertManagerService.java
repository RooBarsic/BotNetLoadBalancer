package com.example.servingwebcontent.service.alerts;

import java.util.List;

public interface AlertManagerService {
    void addMessageToProcessingQueue(String alertMessage);
    void addMessagesToProcessingQueue(List<String> alertMessage);
}
