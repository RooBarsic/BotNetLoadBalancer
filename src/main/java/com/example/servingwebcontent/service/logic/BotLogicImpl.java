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
import java.util.stream.Collectors;


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
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2("HOME"));
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
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_SERVICE);
                    } else {
                        user.getDefaultOrder().setServices(service);
                        response.setMessage("Вы выбрали услугу " + service.toString() + "\n" +
                                "Пожалуйста выберите мастера:");
                        response.addButton(new BotNetButton2("Любой мастер"));
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
                        if (message.equals("Любой мастер")) {
                            user.getDefaultOrder().setMaster(null);
                            response.setMessage("Мастер выберится автоматичеки в зависмости от выбранного мастера." +
                                    "\nПожалуйста выберите время:");

                            Set<String> avaiilableTimeSlots = new HashSet<>();
                            for (Master master1 : masters) {
                                if (master1.hashSerivce(user.getDefaultOrder().getServices())) {
                                    avaiilableTimeSlots.addAll(master1.getTimeSlots());
                                }
                            }
                            for (String time : avaiilableTimeSlots.stream().sorted().collect(Collectors.toList())) {
                                response.addButton(new BotNetButton2(time));
                            }
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2("HOME"));
                            user.setStatus(Status.REQUIRED_TIME_SLOT);
                        }
                        else {
                            response.setMessage("Я вас не понимаю.\n" +
                                    "Пожалуйста выберите одно из мастеров:");
                            response.addButton(new BotNetButton2("Любой мастер"));
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
                        for (String time : master.getTimeSlots()) {
                            response.addButton(new BotNetButton2(time));
                        }
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_TIME_SLOT);
                    }
                    break;
                case REQUIRED_TIME_SLOT:
                    String timeSlot = null;

                    List<String> masterTimeSlots = new ArrayList<>();
                    if (user.getDefaultOrder().getMaster() == null) {
                        Set<String> avaiilableTimeSlots = new HashSet<>();
                        for (Master master1 : masters) {
                            if (master1.hashSerivce(user.getDefaultOrder().getServices())) {
                                avaiilableTimeSlots.addAll(master1.getTimeSlots());
                            }
                        }
                        masterTimeSlots.addAll(avaiilableTimeSlots);
                        masterTimeSlots.sort(String::compareTo);
                    } else {
                        masterTimeSlots.addAll(user.getDefaultOrder().getMaster().getTimeSlots());
                    }
                    for (String time : masterTimeSlots) {
                        if (time.equals(message)) {
                            timeSlot = time;
                            break;
                        }
                    }
                    if (timeSlot == null) {
                        response.setMessage("Я вас не понял.\n" +
                                "Пожалуйста выберите время:");
                        for (String time : masterTimeSlots) {
                            response.addButton(new BotNetButton2(time));
                        }
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_TIME_SLOT);
                    } else {
                        if (user.getDefaultOrder().getMaster() == null) { // мы смами выбираем мастера
                            Master master1 = null;
                            for (Master master2 : masters) {
                                if (master2.hasTimeSlot(message) && master2.hashSerivce(user.getDefaultOrder().getServices())) {
                                    master1 = master2;
                                    break;
                                }
                            }
                            user.getDefaultOrder().setMaster(master1);
                            response.setMessage("Вы выбрали время " + timeSlot + "\n" +
                                    "Вашем мастером будет : " + master1.getName() + "\n" +
                                    "Пожалуйста отправьте нам ваше имя для оформления брони");
                        }
                        else {
                            response.setMessage("Вы выбрали время " + timeSlot + "\n" +
                                    "Пожалуйста отправьте нам ваше имя для оформления брони");
                        }
                        user.getDefaultOrder().getMaster().blockTimeSlot(timeSlot);
                        user.getDefaultOrder().setDate(timeSlot);
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
        String userKey = getUSerKey(request);
        User user = userById.getOrDefault(userKey, new User(request.getUiPlatform(), request.getUserChatId()));
        return user;
    }

    private User setUser(BotNetRequest request, User user) {
        String userKey = getUSerKey(request);
        userById.put(userKey, user);
        return user;
    }

    private String getUSerKey(BotNetRequest request) {
        return request.getUiPlatform() + "#" + request.getUserChatId();
    }

    private void sendResponse(BotNetResponse response) {
        responseService.addResponseToProcessingQueue(response);
    }

    private void addDefaultMasters() {
        Master abdullo = new Master();
        abdullo.setName("Абдулло");
        abdullo.addServicesAndTimeSlots(
                Arrays.asList(Services.STRIZKA),
                Arrays.asList("12:00", "13:00", "14:00")
        );
        masters.add(abdullo);

        Master abdullo2 = new Master();
        abdullo2.setName("Коля");
        abdullo2.addServicesAndTimeSlots(
                Arrays.asList(Services.STRIZKA),
                Arrays.asList("13:00", "14:00", "18:00")
        );
        masters.add(abdullo2);

        Master abdullo3 = new Master();
        abdullo3.setName("Костя");
        abdullo3.addServicesAndTimeSlots(
                Arrays.asList(Services.STRIZKA),
                Arrays.asList("09:00", "15:00", "16:00")
        );
        masters.add(abdullo3);


        Master sasha = new Master();
        sasha.setName("Саша");
        sasha.addServicesAndTimeSlots(
                Arrays.asList(Services.BORODA),
                Arrays.asList("09:00", "10:00", "11:00")
        );
        masters.add(sasha);

        Master sasha2 = new Master();
        sasha2.setName("Махмуд");
        sasha2.addServicesAndTimeSlots(
                Arrays.asList(Services.BORODA),
                Arrays.asList("08:00", "13:00", "17:00")
        );
        masters.add(sasha2);

        Master sasha3 = new Master();
        sasha3.setName("Ахмаджан");
        sasha3.addServicesAndTimeSlots(
                Arrays.asList(Services.BORODA),
                Arrays.asList("15:00", "16:00", "18:00")
        );
        masters.add(sasha3);
    }
}
