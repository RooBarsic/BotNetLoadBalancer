package com.example.servingwebcontent.service.logic;

import com.example.message.BotNetRequest;
import com.example.servingwebcontent.service.logic.data.User;

public interface AdminService {

    void processAdminMessage(BotNetRequest request, User user);
}
