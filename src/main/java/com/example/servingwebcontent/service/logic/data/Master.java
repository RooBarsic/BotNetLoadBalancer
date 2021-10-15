package com.example.servingwebcontent.service.logic.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Master {
    private String name = "null";
    private Map<Services, List<String>> serviceAndTimes = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSerivse(Services service, List<String> dates) {
        serviceAndTimes.put(service, dates);
    }

    public boolean hashSerivce(Services service) {
        return serviceAndTimes.containsKey(service);
    }

    public Map<Services, List<String>> getServiceAndTimes() {
        return serviceAndTimes;
    }

    public void setServiceAndTimes(Map<Services, List<String>> serviceAndTimes) {
        this.serviceAndTimes = serviceAndTimes;
    }
}
