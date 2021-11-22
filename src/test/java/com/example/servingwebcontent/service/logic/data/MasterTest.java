package com.example.servingwebcontent.service.logic.data;

import com.example.servingwebcontent.service.logic.MastersService;
import com.example.servingwebcontent.service.logic.MastersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
        master.addSerivse(BarberShopServise.BORODA);
        boolean actual = master.hasService(BarberShopServise.BORODA);
        assertTrue(actual);

        master.addSerivse(BarberShopServise.STRIZKA);
        actual = master.hasService(BarberShopServise.BORODA);
        assertTrue(actual);

        actual = master.hasService(BarberShopServise.STRIZKA);
        assertTrue(actual);
    }
    @Test
    void canSetAndModifyTimeSlots(){
        List<String> expected = Arrays.asList("09:00", "11:00");
        master.addTimeSlots(Arrays.asList("09:00","11:00"));
        List<String> actual = master.getTimeSlots("11.12.2021");
        assertEquals(expected, actual);

        assertTrue(master.hasTimeSlot("11.12.2021","09:00"));
        master.blockTimeSlot("11.12.2021", "09:00", BarberShopServise.BORODA);
        assertFalse(master.hasTimeSlot("11.12.2021","09:00"));

        expected = Arrays.asList("11:00");
        actual = master.getTimeSlots("11.12.2021");
        assertEquals(expected, actual);
    }

    @Test
    void addServicesAndTimeSlots() {
        master.addServicesAndTimeSlots(Arrays.asList(BarberShopServise.BORODA, BarberShopServise.STRIZKA),Arrays.asList("09:00","11:00"));
        assertTrue(master.hasService(BarberShopServise.BORODA));
        assertTrue(master.hasService(BarberShopServise.STRIZKA));
        assertTrue(master.hasTimeSlot("11.12.2021","09:00"));
        assertTrue(master.hasTimeSlot("11.12.2021","11:00"));
    }

    @Test
    void canSetAndModifyOrder(){
        Order order = new Order();
        order.setMaster(master);
        order.setUserName("Ivan");
        order.setPhone("81234567890");
        order.setServices(BarberShopServise.BORODA);
        order.setTimeSlot("09:00");
        String expected = "\nУслуга : борода\n" +
                "Мастер : null\n" +
                "Время : 09:00\n" +
                "Имя клента : Ivan\n" +
                "Телефон клиента : 81234567890\n";
        String actual = order.getOrderInfo();
//        assertEquals(expected, actual);

        master.setName("Oleg");
        expected = "\nУслуга : борода\n" +
                "Мастер : Oleg\n" +
                "Время : 09:00\n" +
                "Имя клента : Ivan\n" +
                "Телефон клиента : 81234567890\n";
        actual = order.getOrderInfo();
//        assertEquals(expected, actual);
    }
    
    @Test
    void canSetAndModifyTimeSlotsForDoubleServise() {
        List<String> expected = Arrays.asList("09:00", "12:00");
        master.addServicesAndTimeSlots(Arrays.asList(BarberShopServise.BORODA, BarberShopServise.STRIZKA, BarberShopServise.BORODA_AND_STRIZKA),Arrays.asList("09:00","09:20", "12:00", "12:20"));
        MastersService masterServ = new MastersServiceImpl();
        masterServ.addMaster(master);
        List<String> actual = masterServ.getAvailableTimeSlots("11.12.2021",master, BarberShopServise.BORODA_AND_STRIZKA);
        assertEquals(expected, actual);
    }
}
