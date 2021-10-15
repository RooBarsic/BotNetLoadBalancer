package com.example.servingwebcontent;


import com.example.message.BotNetRequest;
import com.example.servingwebcontent.service.logic.BotLogic;
import org.springframework.web.bind.annotation.*;

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
