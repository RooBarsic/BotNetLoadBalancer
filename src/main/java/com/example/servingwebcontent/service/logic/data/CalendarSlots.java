package com.example.servingwebcontent.service.logic.data;

import java.util.*;

public class CalendarSlots {
    private Set<String> defaultSlots;
    private Map<String, Set<String>> slotsByDay;

    CalendarSlots() {
        defaultSlots = new HashSet<>();
        slotsByDay = new HashMap<>();
    }
/*
Дана строка (возможно, пустая), состоящая из букв A-Z:
AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB
Нужно написать функцию RLE, которая на выходе даст строку вида:
A4B3C2XYZD4E3F3A6B28
И сгенерирует ошибку, если на вход пришла невалидная строка.
Пояснения:
Если символ встречается 1 раз, он остается без изменений;
Если символ повторяется более 1 раза, к нему добавляется количество повторений.
 */
    public void setDefaultSlots(List<String> timeSlots) {
        defaultSlots.clear();
        defaultSlots.addAll(timeSlots);
    }

    public Set<String> getTimeSlotsByDay(final String date) {
        Set<String> timeSlots = slotsByDay.getOrDefault(date, null);
        if (timeSlots != null) {
            slotsByDay.put(date, timeSlots = new HashSet<>(defaultSlots));
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
