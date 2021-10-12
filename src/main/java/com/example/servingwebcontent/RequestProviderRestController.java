package com.example.servingwebcontent;

import com.example.message.BotNetRequest;
import com.example.servingwebcontent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestProviderRestController {
    private final RequestService requestService;

    @Autowired
    RequestProviderRestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloGET() {
        System.out.println("Got Hello request");
        return "greeting";
    }


    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    @ResponseBody
    public List<BotNetRequest> getAllRemainingRequests(@RequestParam(name = "size", required = false, defaultValue = "10") int pageSize) {
        System.out.println("Got GET");
        return requestService.getAndDeleteAllNotProcessedRequests();
    }
}

