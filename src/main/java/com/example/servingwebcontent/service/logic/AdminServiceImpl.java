package com.example.servingwebcontent.service.logic;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.servingwebcontent.service.ResponseService;
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
            String args[] = request.getMessage().split(".");
            Master master = new Master();
            master.setName(args[0]);
            List<String> timeSlots = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                timeSlots.add(args[i]);
            }
            master.addTimeSlots(timeSlots);
            mastersService.addMaster(master);

            BotNetResponse response = new BotNetResponse();
            response.setUiPlatform(request.getUiPlatform());
            response.setReceiverChatId(request.getUserChatId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Мастер зарегистрирован. Имя ")
                    .append(master.getName())
                    .append("\n")
                    .append("таймслоты : ");
            for (String timeSlot : master.getTimeSlots()) {
                stringBuilder.append(timeSlot)
                        .append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            response.setMessage(stringBuilder.toString());
            responseService.addResponseToProcessingQueue(response);
        } catch (Exception e) {
            BotNetResponse response = new BotNetResponse();
            response.setUiPlatform(request.getUiPlatform());
            response.setReceiverChatId(request.getUserChatId());
            response.setMessage("Неожиданная ошибка сервиса. Мы отправим эту ошибку - разработчикам.");
        }
    }
}
