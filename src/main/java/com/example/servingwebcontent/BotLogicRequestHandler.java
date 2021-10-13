package com.example.servingwebcontent;


import com.example.message.BotNetRequest;
import com.example.servingwebcontent.service.BotLogic;
import com.example.servingwebcontent.service.BotLogicImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BotLogicRequestHandler {

    private final BotLogic botLogic;

    BotLogicRequestHandler(final BotLogic botLogic) {
        this.botLogic = botLogic;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public boolean getAllRemainingRequests(@RequestBody BotNetRequest request) {
        return botLogic.processIncomingRequest(request);
    }

}
