package com.example.servingwebcontent.service.alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertGenerator {

    @Autowired
    AlertGenerator(AlertManagerService alertManagerService) {
        alertManagerService.addMessageToProcessingQueue("Hello from Deltix");
        System.out.println("sended hello message");
    }
}
