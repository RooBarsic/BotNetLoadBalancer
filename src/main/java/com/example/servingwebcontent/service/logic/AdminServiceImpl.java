package com.example.servingwebcontent.service.logic;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.servingwebcontent.service.ResponseService;
import com.example.servingwebcontent.service.logic.data.BarberShopServise;
import com.example.servingwebcontent.service.logic.data.Master;
import com.example.servingwebcontent.service.logic.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final MastersService mastersService;
    private final ResponseService responseService;

    @Autowired
    AdminServiceImpl(final MastersService mastersService,
                     final ResponseService responseService) {
        this.mastersService = mastersService;
        this.responseService = responseService;
    }

    @Override
    public void processAdminMessage(BotNetRequest request, User user) {
        try {
            String mesaage = request.getMessage();
            if (mesaage.startsWith("новый мастер")) {
                mesaage = mesaage.split("новый мастер", 2)[1];
                mesaage = mesaage.split("имя ", 2)[1];
                Master master = new Master();
                String name = mesaage.split("навыки |\n", 2)[0];
                master.setName(name);

                mesaage = mesaage.split("навыки ", 2)[1];
                String service = mesaage.split("время ", 2)[0];
                for (String ss : service.split(",|\n")) {
                    master.addSerivse(BarberShopServise.getServiceOrNull(ss));
                }

                mesaage = mesaage.split("время ", 2)[1];
                List<String> timeSlots = new ArrayList<>();
                for (String time : mesaage.split(",")) {
                    timeSlots.add(time);
                }
                master.addTimeSlots(timeSlots);

                mastersService.addMaster(master);

                BotNetResponse response = new BotNetResponse();
                response.setUiPlatform(request.getUiPlatform());
                response.setReceiverChatId(request.getUserChatId());

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Мастер зарегистрирован. \nИмя ")
                        .append(master.getName())
                        .append("\nнавыки : ");
                if (master.hasService(BarberShopServise.BORODA)) {
                    stringBuilder.append(BarberShopServise.BORODA.toString());
                }
                if (master.hasService(BarberShopServise.STRIZKA)) {
                    stringBuilder.append(BarberShopServise.STRIZKA.toString());
                }
                stringBuilder.append("\nтаймслоты : ");

                for (String timeSlot : master.getDefaultTimeSlots()) {
                    stringBuilder.append(timeSlot)
                            .append(", ");
                }
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                response.setMessage(stringBuilder.toString());
                responseService.addResponseToProcessingQueue(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BotNetResponse response = new BotNetResponse();
            response.setUiPlatform(request.getUiPlatform());
            response.setReceiverChatId(request.getUserChatId());
            response.setMessage("Неожиданная ошибка сервиса. Мы отправим эту ошибку - разработчикам.");
            responseService.addResponseToProcessingQueue(response);
        }
    }
}
