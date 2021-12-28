package com.example.servingwebcontent;


import com.example.message.BotNetRequest;
import com.example.servingwebcontent.service.logic2.CardImpl;
import com.example.servingwebcontent.service.logic2.CardLogicExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BotLogicRequestHandler {

    private final CardLogicExecutorService cardLogic;

    @Autowired
    BotLogicRequestHandler(final CardLogicExecutorService cardLogic) {
        this.cardLogic = cardLogic;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public boolean getAllRemainingRequests(@RequestBody BotNetRequest request) {
        return cardLogic.processIncomingRequest(request);
    }

    @RequestMapping(value = "/set-card-logic", method = RequestMethod.POST)
    @ResponseBody
    public boolean setCardLogic(@RequestBody CardImpl card) {
        cardLogic.setCardLogic(card);
        return true;
    }

    @RequestMapping(value = "/get-root-card-logic", method = RequestMethod.GET)
    @ResponseBody
    public CardImpl getCardLogic() {
        return (CardImpl) cardLogic.getRootLogic();
    }

}
