package com.example.servingwebcontent.service.logic;

import com.example.message.BotNetRequest;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton2;
import com.example.servingwebcontent.components.TokenStorage;
import com.example.servingwebcontent.service.ResponseService;
import com.example.servingwebcontent.service.logic.data.Master;
import com.example.servingwebcontent.service.logic.data.BarberShopServise;
import com.example.servingwebcontent.service.logic.data.Status;
import com.example.servingwebcontent.service.logic.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BotLogicImpl implements BotLogic {
    private String GREETING = "Добрый день. Я чат бот для бронирования сеансов в ITMO Barbershop";
    private final ResponseService responseService;
    private final Map<String, User> userById;
    private final MastersService mastersService;
    private final AdminService adminService;
    private final String ADMIN_PASSWORD;

    @Autowired
    BotLogicImpl(final ResponseService responseService,
                 final MastersService mastersService,
                 final AdminService adminService,
                 final TokenStorage tokenStorage) {
        this.responseService = responseService;
        this.userById = new HashMap<>();
        this.mastersService = mastersService;
        this.adminService = adminService;
        ADMIN_PASSWORD = "ADMIN-" + tokenStorage.getTokens("ADMIN_PASSWORD");
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

        if (message.equals("USER")) {
            user.setAdmin(false);
            response.setMessage("Вы вышли из режима администратора.");
        }
        else if (user.isAdmin()) {
            adminService.processAdminMessage(request, user);
            return true;
        }

        if (message.equals(ADMIN_PASSWORD)) {
            user.setAdmin(true);
            response.setMessage("Вы перешли в режим администратора.");
        }
        else if (message.equals("USER")) {
            user.setAdmin(false);
            response.setMessage("Вы вышли из режима администратора.");
        }
        else if (message.equals("HOME")) {
            response.setMessage(GREETING);
            response.addButton(new BotNetButton2("Выбор услуги"));
            response.addButton(new BotNetButton2("Мои записи"));
            response.addButton(new BotNetButton2("Инфо"));
            user.setStatus(Status.REQUIRED_ACTION);
        }
        else {

            switch (user.getStatus()) {
                case NEW_USER:
                    response.setMessage(GREETING);
                    response.addButton(new BotNetButton2("Выбор услуги"));
                    response.addButton(new BotNetButton2("Мои записи"));
                    response.addButton(new BotNetButton2("Инфо"));
                    user.setStatus(Status.REQUIRED_ACTION);
                    break;
                case REQUIRED_ACTION:
                    switch (message) {
                        case "Выбор услуги":
                            response.setMessage("Выберите услугу");
                            response.addButton(new BotNetButton2(BarberShopServise.BORODA_AND_STRIZKA.toString()));
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2(BarberShopServise.BORODA.toString()));
                            response.addButton(new BotNetButton2(BarberShopServise.STRIZKA.toString()));
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2("HOME"));
                            user.setStatus(Status.REQUIRED_SERVICE);
                            break;
                        case "Мои записи":
                            response.setMessage("Ваши записи :\n" + user.getMyActiveOrders());
                            response.addButton(new BotNetButton2("Выбор услуги"));
                            response.addButton(new BotNetButton2("Мои записи"));
                            response.addButton(new BotNetButton2("Инфо"));
                            user.setStatus(Status.REQUIRED_ACTION);
                            break;
                        case "Инфо":
                            response.setMessage(GREETING);
                            response.addButton(new BotNetButton2("Выбор услуги"));
                            response.addButton(new BotNetButton2("Мои записи"));
                            response.addButton(new BotNetButton2("Инфо"));
                            user.setStatus(Status.REQUIRED_ACTION);
                            break;
                        default:
                            response.setMessage("Я вас не понял. \nПожалуйста выберите одно из этих кнопок:");
                            response.addButton(new BotNetButton2("Выбор услуги"));
                            response.addButton(new BotNetButton2("Мои записи"));
                            response.addButton(new BotNetButton2("Инфо"));
                            user.setStatus(Status.REQUIRED_ACTION);
                            break;
                    }
                    break;
                case REQUIRED_SERVICE:
                    BarberShopServise service = BarberShopServise.getServiceOrNull(message);
                    if (service == null) {
                        response.setMessage("Я вас не понял.\n" +
                                "\nПожалуйста выберите одно из услугу:");
                        response.addButton(new BotNetButton2(BarberShopServise.BORODA.toString()));
                        response.addButton(new BotNetButton2(BarberShopServise.STRIZKA.toString()));
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_SERVICE);
                    } else {
                        user.getDefaultOrder().setServices(service);
                        response.setMessage("Вы выбрали услугу " + service.toString() + "\n" +
                                        "\nПожалуйста введите дату посешения салона, в формате dd.mm.yyyy\n" +
                                "Например 18.12.2021");
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_DATE);
                    }
                    break;
                case REQUIRED_DATE:
                    if (isCorrectDate(request.getMessage())) {
                        user.getDefaultOrder().setDate(request.getMessage());
                        response.setMessage("Вы ввели дату " + request.getMessage()+ "\n" +
                                "\nПожалуйста выберите мастера:");
                        response.addButton(new BotNetButton2("Любой мастер"));
                        mastersService.getAllMastersByService(user.getDefaultOrder().getServices())
                                .forEach(master -> {
                                    response.addButton(new BotNetButton2(master.getName()));
                                });
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_MASTER);
                    }
                    else {
                        response.setMessage("Я вас не понял. " +
                                "\nПожалуйста выберите корректную дату в формате dd.mm.yyyy\n" +
                                "Например 18.12.2021");
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_DATE);
                    }
                    break;

                case REQUIRED_MASTER:
                    Master master = mastersService.getMasterByName(message);
                    if (master == null) {
                        if (message.equals("Любой мастер")) {
                            user.getDefaultOrder().setMaster(null);
                            response.setMessage("Мастер выберится автоматичеки в зависмости от выбранного мастера." +
                                    "\nПожалуйста выберите время:");

                            List<String> avaiilableTimeSlots = mastersService.getAvailableTimeSlots(user.getDefaultOrder().getDate(), mastersService.getAllMastersByService(user.getDefaultOrder().getServices()), user.getDefaultOrder().getServices());
                            for (String time : avaiilableTimeSlots) {
                                response.addButton(new BotNetButton2(time));
                            }
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2("HOME"));
                            user.setStatus(Status.REQUIRED_TIME_SLOT);
                        }
                        else {
                            response.setMessage("Я вас не понимаю.\n" +
                                    "\nПожалуйста выберите одно из мастеров:");
                            response.addButton(new BotNetButton2("Любой мастер"));
                            for (Master m : mastersService.getAllMastersByService(user.getDefaultOrder().getServices())) {
                                response.addButton(new BotNetButton2(m.getName()));
                            }
                            response.setNewButtonsLine();
                            response.addButton(new BotNetButton2("HOME"));
                            user.setStatus(Status.REQUIRED_MASTER);
                        }
                    } else {
                        user.getDefaultOrder().setMaster(master);
                        response.setMessage("Вы выбрали мастера " + master.getName() + "\n" +
                                "\nПожалуйста выберите время:");
                        for (String time : mastersService.getAvailableTimeSlots(user.getDefaultOrder().getDate(), master, user.getDefaultOrder().getServices())) {
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
                        masterTimeSlots = mastersService.getAvailableTimeSlots(user.getDefaultOrder().getDate(), mastersService.getAllMastersByService(user.getDefaultOrder().getServices()), user.getDefaultOrder().getServices());
                    } else {
                        masterTimeSlots.addAll(mastersService.getAvailableTimeSlots(
                                user.getDefaultOrder().getDate(),
                                user.getDefaultOrder().getMaster(),
                                user.getDefaultOrder().getServices())
                        );
                    }
                    for (String time : masterTimeSlots) {
                        if (time.equals(message)) {
                            timeSlot = time;
                            break;
                        }
                    }
                    if (timeSlot == null) {
                        response.setMessage("Я вас не понял.\n" +
                                "\nПожалуйста выберите время:");
                        for (String time : masterTimeSlots) {
                            response.addButton(new BotNetButton2(time));
                        }
                        response.setNewButtonsLine();
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_TIME_SLOT);
                    } else {
                        if (user.getDefaultOrder().getMaster() == null) { // мы смами выбираем мастера
                            Master master1 = mastersService.getMasterByServiceAndTimeSlot(user.getDefaultOrder().getDate(), user.getDefaultOrder().getServices(), message);
                            user.getDefaultOrder().setMaster(master1);
                            response.setMessage("Вы выбрали время " + timeSlot + "\n" +
                                    "Вашем мастером будет : " + master1.getName() + "\n" +
                                    "\nПожалуйста отправьте нам ваше имя для оформления брони");
                        }
                        else {
                            response.setMessage("Вы выбрали время " + timeSlot + "\n" +
                                    "\nПожалуйста отправьте нам ваше имя для оформления брони");
                        }
                        user.getDefaultOrder().getMaster().blockTimeSlot(user.getDefaultOrder().getDate(), timeSlot, user.getDefaultOrder().getServices());
                        user.getDefaultOrder().setTimeSlot(timeSlot);
                        response.addButton(new BotNetButton2("HOME"));
                        user.setStatus(Status.REQUIRED_USER_NAME);
                    }
                    break;
                case REQUIRED_USER_NAME:
                    user.getDefaultOrder().setUserName(message);
                    response.setMessage("Приятно познакомиться " + message + "!\n" +
                            "\nПожалуйста отправьте нам номер телефона для оформления брони");
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
                    response.addButton(new BotNetButton2("Выбор услуги"));
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

    private boolean isCorrectDate(String date) {
        return date != null && date.length() > 0;
    }

    private String getUSerKey(BotNetRequest request) {
        return request.getUiPlatform() + "#" + request.getUserChatId();
    }

    private void sendResponse(BotNetResponse response) {
        responseService.addResponseToProcessingQueue(response);
    }
}
