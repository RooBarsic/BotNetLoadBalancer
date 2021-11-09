package com.example.servingwebcontent.service.logic.data;

import java.util.*;
import java.util.stream.Collectors;

public class Master {
    private String name = "null";
    private Set<String> timeSlots = new HashSet<>();
    private Set<BarberShopServise> services = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSerivse(BarberShopServise service) {
        services.add(service);
    }

    public void addSerivse(List<BarberShopServise> service) {
        services.addAll(service);
    }

    public void addTimeSlots(List<String> timeSlots) {
        this.timeSlots.addAll(timeSlots);
    }

    public boolean hasTimeSlot(String timeSlot) {
        return timeSlots.contains(timeSlot);
    }

    public void addServicesAndTimeSlots(List<BarberShopServise> services, List<String> timeSlots) {
        this.services.addAll(services);
        this.timeSlots.addAll(timeSlots);
    }

    public boolean hasService(BarberShopServise service) {
        if (BarberShopServise.BORODA_AND_STRIZKA == service) {
            return services.contains(BarberShopServise.BORODA) &&
                    services.contains(BarberShopServise.STRIZKA);
        }
        return services.contains(service);
    }

    public boolean hasServices(List<BarberShopServise> servises) {
        for (BarberShopServise service : servises) {
            if (hasService(service) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param timeslot
     * @return - true если таймслот найде. false - если такого таймлоста у нас нет.
     */
    public boolean blockTimeSlot(String timeslot) {
        if (timeSlots.contains(timeslot)) {
            timeSlots.remove(timeslot);
            return true;
        }
        return false;
    }

    public List<String> getTimeSlots() {
        return timeSlots.stream().sorted(String::compareTo).collect(Collectors.toList());
    }
}
