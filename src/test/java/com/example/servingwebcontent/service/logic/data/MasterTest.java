package com.example.servingwebcontent.service.logic.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MasterTest {
    private Master master;
    @BeforeEach
    public void init() {
        master = new Master();
    }
    @Test
    void canSetAndModifyName() {
        String expected = "Vova";
        master.setName("Vova");
        String actual  = master.getName();
        assertEquals(expected, actual);

        expected = "Egor";
        master.setName("Egor");
        actual  = master.getName();
        assertEquals(expected, actual);
    }
    @Test
    void canProperlySetServise(){
        master.addSerivse(Services.BORODA);
        boolean actual = master.hasSerivce(Services.BORODA);
        assertTrue(actual);

        master.addSerivse(Services.STRIZKA);
        actual = master.hasSerivce(Services.BORODA);
        assertTrue(actual);

        actual = master.hasSerivce(Services.STRIZKA);
        assertTrue(actual);
    }
    @Test
    void canSetAndModifyTimeSlots(){
        List<String> expected = Arrays.asList("09:00", "11:00");
        master.addTimeSlots(Arrays.asList("09:00","11:00"));
        List<String> actual = master.getTimeSlots();
        assertEquals(expected, actual);

        assertTrue(master.hasTimeSlot("09:00"));
        master.blockTimeSlot("09:00");
        assertFalse(master.hasTimeSlot("09:00"));

        expected = Arrays.asList("11:00");
        actual = master.getTimeSlots();
        assertEquals(expected, actual);
    }

    @Test
    void addServicesAndTimeSlots() {
        master.addServicesAndTimeSlots(Arrays.asList(Services.BORODA,Services.STRIZKA),Arrays.asList("09:00","11:00"));
        assertTrue(master.hasSerivce(Services.BORODA));
        assertTrue(master.hasSerivce(Services.STRIZKA));
        assertTrue(master.hasTimeSlot("09:00"));
        assertTrue(master.hasTimeSlot("11:00"));
    }
}