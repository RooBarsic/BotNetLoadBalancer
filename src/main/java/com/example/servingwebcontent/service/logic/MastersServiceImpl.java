package com.example.servingwebcontent.service.logic;

import com.example.servingwebcontent.service.logic.data.BarberShopServise;
import com.example.servingwebcontent.service.logic.data.Master;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MastersServiceImpl implements MastersService {
    private final List<Master> masters;

    MastersServiceImpl() {
        masters = new ArrayList<>();

        addDefaultMasters();
    }

    @Override
    public List<Master> getAllMasters() {
        return new ArrayList<>(masters);
    }

    @Override
    public List<Master> getAllMastersByService(BarberShopServise barberShopServise) {
        List<Master> mastersByService = new ArrayList<>();
        for (Master master : masters) {
            if (master.hasService(barberShopServise)) {
                mastersByService.add(master);
            }
        }
        return mastersByService;
    }

    @Override
    public Master getMasterByName(String masterName) {
        return getMasterByNameFomList(masters, masterName);
    }

    @Override
    public Master getMasterByNameFomList(List<Master> mastersList, String masterName) {
        for (Master master : mastersList) {
            if (master.getName().equals(masterName)) {
                return master;
            }
        }
        return null;
    }

    @Override
    public List<String> getAvailableTimeSlots(List<Master> masters) {
        Set<String> availableTimeSlots = new HashSet<>();
        for (Master master1 : masters) {
            availableTimeSlots.addAll(master1.getTimeSlots());
        }
        List<String> openTimeSlots = new ArrayList<>(availableTimeSlots);
        openTimeSlots.sort(String::compareTo);
        return openTimeSlots;
    }

    @Override
    public Master blockTimeSlotForMaster(Master master, String timeSlot) {
        master.blockTimeSlot(timeSlot);
        return master;
    }

    @Override
    public Master getMasterByServiceAndTimeSlot(BarberShopServise barberShopServise, String timeSlot) {
        List<Master> mastersByService = getAllMastersByService(barberShopServise);
        for (Master master : mastersByService) {
            if (master.hasTimeSlot(timeSlot)) {
                return master;
            }
        }
        return null;
    }

    @Override
    public List<Master> getMastersByMultipleServices(List<BarberShopServise> servises) {
        List<Master> availableMasters = new ArrayList<>();
        for (Master master : masters) {
            if (master.hasServices(servises)) {
                availableMasters.add(master);
            }
        }
        return availableMasters;
    }

    private void addDefaultMasters() {
        Master abdullo = new Master();
        abdullo.setName("Абдулло");
        abdullo.addServicesAndTimeSlots(
                Arrays.asList(BarberShopServise.STRIZKA),
                Arrays.asList("12:00", "13:00", "14:00")
        );
        masters.add(abdullo);

        Master abdullo2 = new Master();
        abdullo2.setName("Коля");
        abdullo2.addServicesAndTimeSlots(
                Arrays.asList(BarberShopServise.STRIZKA),
                Arrays.asList("13:00", "14:00", "18:00")
        );
        masters.add(abdullo2);

        Master abdullo3 = new Master();
        abdullo3.setName("Костя");
        abdullo3.addServicesAndTimeSlots(
                Arrays.asList(BarberShopServise.STRIZKA),
                Arrays.asList("09:00", "15:00", "16:00")
        );
        masters.add(abdullo3);


        Master sasha = new Master();
        sasha.setName("Саша");
        sasha.addServicesAndTimeSlots(
                Arrays.asList(BarberShopServise.BORODA),
                Arrays.asList("09:00", "10:00", "11:00")
        );
        masters.add(sasha);

        Master sasha2 = new Master();
        sasha2.setName("Махмуд");
        sasha2.addServicesAndTimeSlots(
                Arrays.asList(BarberShopServise.BORODA),
                Arrays.asList("08:00", "13:00", "17:00")
        );
        masters.add(sasha2);

        Master sasha3 = new Master();
        sasha3.setName("Ахмаджан");
        sasha3.addServicesAndTimeSlots(
                Arrays.asList(BarberShopServise.BORODA),
                Arrays.asList("15:00", "16:00", "18:00")
        );
        masters.add(sasha3);
    }

}
