package com.example.servingwebcontent.service.logic;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton2;
import com.example.servingwebcontent.service.ResponseService;
import com.example.servingwebcontent.service.logic.data.Master;
import com.example.servingwebcontent.service.logic.data.Services;
import com.example.servingwebcontent.service.logic.data.Status;
import com.example.servingwebcontent.service.logic.data.User;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BotLogicImpl implements BotLogic {
    private String GREETING = "Добрый день. Я чат бот для бронирования сеансов в ITMO Barbershop";
    private final ResponseService responseService;
    private final Map<String, User> userById;
    private final List<Master> masters;

    BotLogicImpl(final ResponseService responseService) {
        this.responseService = responseService;
        this.userById = new HashMap<>();
        masters = new ArrayList<>();

        addDefaultMasters();
    }

    @Override
    public boolean processIncomingRequest(BotNetRequest request) {
        //configure response
        BotNetResponse response = new BotNetResponse();
        response.setReceiverChatId(request.getUserChatId());
        response.setUiPlatform(request.getUiPlatform());

        // logic
        String message = request.getMessage();

        User user = getUserOrDefault(request);

        if (message.equals("HOME")) {
            response.setMessage(GREETING);
            response.addButton(new BotNetButton2("выбор услуги"));
            response.addButton(new BotNetButton2("Мои записи"));
            response.addButton(new BotNetButton2("Инфо"));
            user.setStatus(Status.REQUIRED_ACTION);
        }
        else {

            switch (user.getStatus()) {
                case NEW_USER:
                    response.setMessage(GREETING);
                    response.addButton(new BotNetButton2("выбор услуги"));
                    response.addButton(new BotNetButton2("Мои записи"));
                    response.addButton(new BotNetButton2("Инфо"));
                    user.setStatus(Status.REQUIRED_ACTION);
                    break;
                case REQUIRED_ACTION:
                    switch (message) {
                        case "выбор услуги":
                            response.setMessage("Выберите услугу");
                            response.addButton(new BotNetButton2(Services.BORODA.toString()));
                            response.addButton(new BotNetButton2(Services.STRIZKA.toString()));
                            user.setStatus(Status.REQUIRED_SERVICE);
                            break;
                        case "Мои записи":
                            response.setMessage("Ваши записи :\n" + user.getMyActiveOrders());
                            response.addButton(new BotNetButton2("выбор услуги"));
                            response.addButton(new BotNetButton2("Мои записи"));
                            response.addButton(new BotNetButton2("Инфо"));
                            user.setStatus(Status.REQUIRED_ACTION);
                            break;
                        case "Инфо":
                            response.setMessage(GREETING);
                            response.addButton(new BotNetButton2("выбор услуги"));
                            response.addButton(new BotNetButton2("Мои записи"));
                            response.addButton(new BotNetButton2("Инфо"));
                            user.setStatus(Status.REQUIRED_ACTION);
                            break;
                        default:
                            response.setMessage("Я вас не понял. Пожалуйста выберите одно из этих кнопок:");
                            response.addButton(new BotNetButton2("выбор услуги"));
                            response.addButton(new BotNetButton2("Мои записи"));
                            response.addButton(new BotNetButton2("Инфо"));
                            user.setStatus(Status.REQUIRED_ACTION);
                            break;
                    }
                    break;
                case REQUIRED_SERVICE:
                    Services service = Services.getServiceOrNull(message);
                    if (service == null) {
                        response.setMessage("Я вас не понял.\n" +
                                "Пожалуйста выберите одно из услугу:");
                        response.addButton(new BotNetButton2(Services.BORODA.toString()));
                        response.addButton(new BotNetButton2(Services.STRIZKA.toString()));
                        user.setStatus(Status.REQUIRED_SERVICE);
                    } else {
                        user.getDefaultOrder().setServices(service);
                        response.setMessage("Вы выбрали услугу " + service.toString() + "\n" +
                                "Пожалуйста выберите мастера:");
                        for (Master master : masters) {
                            if (master.hashSerivce(user.getDefaultOrder().getServices())) {
                                response.addButton(new BotNetButton2(master.getName()));
                            }
                        }
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_MASTER);
                    }
                    break;
                case REQUIRED_MASTER:
                    Master master = null;
                    for (Master m : masters) {
                        if (m.getName().equals(message)) {
                            master = m;
                            break;
                        }
                    }
                    if (master == null) {
                        if (message.equals("Random")) {

                        } else {
                            response.setMessage("Я вас не понимаю.\n" +
                                    "Пожалуйста выберите одно из мастеров:");
                            for (Master m : masters) {
                                if (m.hashSerivce(user.getDefaultOrder().getServices())) {
                                    response.addButton(new BotNetButton2(m.getName()));
                                }
                            }
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2("HOME"));
                            user.setStatus(Status.REQUIRED_MASTER);
                        }
                    } else {
                        user.getDefaultOrder().setMaster(master);
                        response.setMessage("Вы выбрали мастера " + master.getName() + "\n" +
                                "Пожалуйста выберите время:");
                        for (String time : master.getServiceAndTimes().get(user.getDefaultOrder().getServices())) {
                            response.addButton(new BotNetButton2(time));
                        }
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_TIME_SLOT);
                    }
                    break;
                case REQUIRED_TIME_SLOT:
                    String timeSlot = null;
                    for (String time : user.getDefaultOrder().getMaster().getServiceAndTimes().get(user.getDefaultOrder().getServices())) {
                        if (time.equals(message)) {
                            timeSlot = time;
                            break;
                        }
                    }
                    if (timeSlot == null) {
                        response.setMessage("Я вас не понял.\n" +
                                "Пожалуйста выберите время:");
                        for (String time : user.getDefaultOrder().getMaster().getServiceAndTimes().get(user.getDefaultOrder().getServices())) {
                            response.addButton(new BotNetButton2(time));
                        }
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_TIME_SLOT);
                    } else {
                        user.getDefaultOrder().setDate(timeSlot);
                        response.setMessage("Вы выбрали время " + timeSlot + "\n" +
                                "Пожалуйста отправьте нам ваше имя для оформления брони");
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_USER_NAME);
                    }
                    break;
                case REQUIRED_USER_NAME:
                    user.getDefaultOrder().setUserName(message);
                    response.setMessage("Приятно познакомиться " + message + "!\n" +
                            "Пожалуйста отправьте нам номер телефона для оформления брони");
                    response.addButton(new BotNetButton2("HOME"));
                    user.setStatus(Status.REQUIRED_USER_PHONE);
                    break;
                case REQUIRED_USER_PHONE:
                    user.getDefaultOrder().setPhone(message);
                    response.setMessage("Вы отправили номер " + message + "\n" +
                            "\n" +
                            "Ваш заказ :\n" + user.getDefaultOrder().getOrderInfo());
                    response.addButton(new BotNetButton2("Подтвердить"));
                    response.setNewButtonsLine();
                    response.addButton(new BotNetButton2("HOME"));
                    user.setStatus(Status.ORDER_COMPLETED);
                    break;
                case ORDER_COMPLETED:
                    if (!message.equals("Подтвердить")) {
                        response.setMessage("Я вас не понял." +
                                "\n" +
                                "Ваш заказ :\n" + user.getDefaultOrder().getOrderInfo());
                        response.addButton(new BotNetButton2("Подтвердить."));
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_USER_PHONE);
                    } else {
                        response.setMessage("Ваш заказ подтверждён." +
                                "\n" +
                                "Ваш заказ :\n" + user.getDefaultOrder().getOrderInfo());
                        user.setStatus(Status.ORDER_COMPLETED);
                        user.saveOrder();
                        user.setStatus(Status.NEW_USER);
                        response.addButton(new BotNetButton2("HOME"));
                    }
                    break;
                case REQUIRED_ABORT:
                    break;
                default:
                    response.setMessage(GREETING);
                    response.addButton(new BotNetButton2("выбор услуги"));
                    response.addButton(new BotNetButton2("Мои записи"));
                    response.addButton(new BotNetButton2("Инфо"));
                    user.setStatus(Status.REQUIRED_ACTION);

            }
        }

        setUser(request, user);

        sendResponse(response);
        return true;
    }

    private User getUserOrDefault(BotNetRequest request) {
        String userKey = request.getUiPlatform() + "#" + request.getUserChatId();
        User user = userById.getOrDefault(userKey, new User(request.getUiPlatform(), request.getUserChatId()));
        return user;
    }

    private User setUser(BotNetRequest request, User user) {
        String userKey = request.getUiPlatform() + "#" + request.getUserChatId();
        userById.put(userKey, user);
        return user;
    }

    private void sendResponse(BotNetResponse response) {
        responseService.addResponseToProcessingQueue(response);
    }

    private void addDefaultMasters() {
        Master abdullo = new Master();
        abdullo.setName("Абдулло");
        abdullo.addSerivse(Services.STRIZKA, Arrays.asList("12:00", "13:00", "14:00"));
        masters.add(abdullo);

        Master abdullo2 = new Master();
        abdullo2.setName("Коля");
        abdullo2.addSerivse(Services.STRIZKA, Arrays.asList("13:00", "14:00", "18:00"));
        masters.add(abdullo2);

        Master abdullo3 = new Master();
        abdullo3.setName("Костя");
        abdullo3.addSerivse(Services.STRIZKA, Arrays.asList("9:00", "15:00", "16:00"));
        masters.add(abdullo3);


        Master sasha = new Master();
        sasha.setName("Саша");
        sasha.addSerivse(Services.BORODA, Arrays.asList("09:00", "10:00", "11:00"));
        masters.add(sasha);

        Master sasha2 = new Master();
        sasha2.setName("Махмуд");
        sasha2.addSerivse(Services.BORODA, Arrays.asList("08:00", "13:00", "17:00"));
        masters.add(sasha2);

        Master sasha3 = new Master();
        sasha3.setName("Ахмаджан");
        sasha3.addSerivse(Services.BORODA, Arrays.asList("15:00", "16:00", "18:00"));
        masters.add(sasha3);
    }
}
