package com.example.servingwebcontent.service.logic.data;

import com.example.servingwebcontent.service.logic.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Master {
    private String name = "null";
    private Set<String> timeSlots = new HashSet<>();
    private String curDate;
    private CalendarSlots calendarSlots = new CalendarSlots();
    private Set<BarberShopServise> services = new HashSet<>();

    public String getName() {
        curDate = "19.11.2021";
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
        this.calendarSlots.setDefaultSlots(timeSlots);
    }

    public boolean hasTimeSlot(String timeSlot) {
        return calendarSlots.hasTimeSlot(curDate, timeSlot);
    }

    public void addServicesAndTimeSlots(List<BarberShopServise> services, List<String> timeSlots) {
        this.services.addAll(services);
        this.calendarSlots.setDefaultSlots(timeSlots);
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
    public boolean blockTimeSlot(String timeslot, BarberShopServise servise) {
        if (calendarSlots.hasTimeSlot(curDate, timeslot)) {
            calendarSlots.blockTimeSlot(curDate, timeslot);
            if (servise == BarberShopServise.BORODA_AND_STRIZKA) {
                timeslot = MathUtils.addMinutes20(timeslot);
                if (calendarSlots.hasTimeSlot(curDate, timeslot)) {
                    calendarSlots.blockTimeSlot(curDate, timeslot);
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public List<String> getTimeSlots() {
        return calendarSlots
                .getTimeSlotsByDay(curDate)
                .stream()
                .sorted(String::compareTo)
                .collect(Collectors.toList());
    }
}
