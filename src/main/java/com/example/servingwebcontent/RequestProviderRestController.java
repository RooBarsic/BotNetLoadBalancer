package com.example.servingwebcontent;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import com.example.message.data.UiPlatform;
import com.example.servingwebcontent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

@RestController
public class RequestProviderRestController {
    private final RequestService requestService;
    private final RestTemplate restTemplate;

    @Autowired
    RequestProviderRestController(RequestService requestService,
                                  final RestTemplateBuilder restTemplateBuilder) {
        this.requestService = requestService;
        restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(500))
                .setReadTimeout(Duration.ofSeconds(500))
                .build();
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloGET() {
        System.out.println("Got Hello request");
        String ans = openPresentation();
        System.out.println(ans);
        return "hello";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file2")
    public ResponseEntity<byte[]> getPdf() throws IOException {
//        final String filePath = pdfFilePathFinder.find(id);

        Path pp = (new File("present_2.pptx")).toPath();

        final byte[] pdfBytes = Files.readAllBytes(pp);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData("attachment", null);
        headers.setCacheControl("no-cache");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    @ResponseBody
    public String helloFile() throws FileNotFoundException {
//        Resource file = new ;
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//
        System.out.println("Got Hello request");
        String ans = openPresentation();
        System.out.println(ans);
        StringBuilder stringBuilder = new StringBuilder("");
        Scanner scaner = new Scanner(new File("present_2.pptx"));
        while (scaner.hasNextLine()) {
            String line = scaner.nextLine();
            if (line.length() > 0) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }


    public String openPresentation() {
        String url = "https://docs.google.com/presentation/d/17fH2o1Yv5MwqU5kYI69yo8GRmcDZ_U1l/edit#slide=1";
        try {
            final ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Some dockument opened");
                return response.getBody();
            } else {
                System.out.println("Some dockument error");
            }
        } catch (Exception e) {
            System.out.println("External service : " + e.getMessage());
        }
        return "some error";
    }



    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    @ResponseBody
    public List<BotNetRequest> getAllRemainingRequests(@RequestParam(name = "size", required = false, defaultValue = "10") int pageSize) {
        System.out.println("Got GET");
        return requestService.getAndDeleteAllNotProcessedRequests();
    }

    @RequestMapping(value = "/geta", method = RequestMethod.GET)
    @ResponseBody
    public BotNetResponse getAllRemdddainingRequests(@RequestParam(name = "size", required = false, defaultValue = "10") int pageSize) {
        System.out.println("Got GET");
        BotNetRequest request = requestService.getAndDeleteAllNotProcessedRequests().get(0);
        BotNetResponse response = new BotNetResponse();
        response.setUiPlatform(UiPlatform.TELEGRAM);
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage("Hello " + request.getMessage());
        response.addButton(new BotNetButton("help", "/help"));
        response.addButton(new BotNetButton("start", "/start"));
        return response;
    }

}

