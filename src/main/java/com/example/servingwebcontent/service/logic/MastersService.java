package com.example.servingwebcontent.service.logic;

import com.example.servingwebcontent.service.logic.data.Master;
import com.example.servingwebcontent.service.logic.data.BarberShopServise;

import java.util.List;

public interface MastersService {

    List<Master> getAllMasters();
    List<Master> getAllMastersByService(BarberShopServise barberShopServise);
    Master getMasterByName(String masterName);
    Master getMasterByNameFomList(List<Master> masters, String masterName);
    List<String> getAvailableTimeSlots(List<Master> masters, BarberShopServise servise);
    List<String> getAvailableTimeSlots(Master master, BarberShopServise servise);
    Master blockTimeSlotForMaster(Master master, String timeSlot, BarberShopServise servise);
    Master getMasterByServiceAndTimeSlot(BarberShopServise barberShopServise, String timeSlot);
    List<Master> getMastersByMultipleServices(List<BarberShopServise> servises);
}
