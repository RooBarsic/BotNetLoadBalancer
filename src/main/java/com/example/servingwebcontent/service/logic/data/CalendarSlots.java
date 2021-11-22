package com.example.servingwebcontent.service.logic.data;

import java.util.*;

public class CalendarSlots {
    private Set<String> defaultSlots;
    private Map<String, Set<String>> slotsByDay;

    CalendarSlots() {
        defaultSlots = new HashSet<>();
        slotsByDay = new HashMap<>();
    }

    public void setDefaultSlots(List<String> timeSlots) {
        defaultSlots.clear();
        defaultSlots.addAll(timeSlots);
    }

    public Set<String> getTimeSlotsByDay(final String date) {
        Set<String> timeSlots = slotsByDay.getOrDefault(date, null);
        if (timeSlots == null) {
            slotsByDay.put(date, timeSlots = new HashSet<>());
            for (String slot : defaultSlots) {
                timeSlots.add(slot);
            }
        }
        return timeSlots;
    }

    public void blockTimeSlot(final String date, final String timeSlot) {
        Set<String> timeSlots = getTimeSlotsByDay(date);
        timeSlots.remove(timeSlot);
    }


    public boolean hasTimeSlot(final String date, final String timeSlot) {
        Set<String> timeSlots = getTimeSlotsByDay(date);
        return timeSlots.contains(timeSlot);
    }
}
