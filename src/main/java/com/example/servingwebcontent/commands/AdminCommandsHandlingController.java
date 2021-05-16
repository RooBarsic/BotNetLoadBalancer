package com.example.servingwebcontent.commands;

import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class AdminCommandsHandlingController {
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;
    private final String TELEGRAM_FEEDBACK_GROUP_ID;

    @Autowired
    AdminCommandsHandlingController(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
        TELEGRAM_FEEDBACK_GROUP_ID = tokenStorage.getTokens("TELEGRAM_FEEDBACK_GROUP_ID");
    }


    @RequestMapping(value = "/save-hierarchy", method = RequestMethod.GET)
    @ResponseBody
    public String saveHierarchyData(@RequestParam(name = "key", required = true, defaultValue = "") String key) {
        hierarchy.gotNewRequest();
        if (key.equals("17051998")) {
            final Gson jsonConverter = new Gson();
            return jsonConverter.toJson(hierarchy);
        }
        return "wrong key";
    }

    @RequestMapping(value = "/update-hierarchy", method = RequestMethod.POST)
    @ResponseBody
    public String updateHierarchyData(@RequestBody Hierarchy hierarchy) {
        hierarchy.gotNewRequest();
        this.hierarchy.updateHierarchy(hierarchy);
        return "OK";
    }
}
