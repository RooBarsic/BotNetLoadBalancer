package com.example.servingwebcontent.service.logic;

import com.example.servingwebcontent.service.logic.data.Master;
import com.example.servingwebcontent.service.logic.data.BarberShopServise;

import java.util.List;

public interface MastersService {

    List<Master> getAllMasters();
    List<Master> getAllMastersByService(BarberShopServise barberShopServise);
    Master getMasterByName(String masterName);
    Master getMasterByNameFomList(List<Master> masters, String masterName);
    List<String> getAvailableTimeSlots(String date, List<Master> masters, BarberShopServise servise);
    List<String> getAvailableTimeSlots(String date, Master master, BarberShopServise servise);
    Master blockTimeSlotForMaster(String date, Master master, String timeSlot, BarberShopServise servise);
    Master getMasterByServiceAndTimeSlot(String date, BarberShopServise barberShopServise, String timeSlot);
    List<Master> getMastersByMultipleServices(List<BarberShopServise> servises);
    void addMaster(Master master);
}
