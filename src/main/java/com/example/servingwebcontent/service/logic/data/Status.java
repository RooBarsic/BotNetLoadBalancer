package com.example.servingwebcontent.service.logic.data;

public enum Status {
    NEW_USER,
//    REQUIRED_LOCATION,
    REQUIRED_ACTION, //выбор услуги
    REQUIRED_SERVICE, //выбор услуги
    REQUIRED_MASTER,  //выбор мастера
    REQUIRED_TIME_SLOT, //выбор времени
    REQUIRED_USER_NAME, //ожидание ИМЕНИ
    REQUIRED_USER_PHONE, //ожидание ТЕЛЕФОНА
    ORDER_COMPLETED, //заказ завершён
    REQUIRED_ABORT, //ожидание ТЕЛЕФОНА


}

/*

[Приветствие] [выбор услуги]

[список услуг с ценами] [кнопки для выбора услуг]

выбрал услугу

[выбрать мастера] [кнопки с именами мастеров + кнопка все мастеры]

выбрал мастера

[вбор времени] [таймслоты]

выбрал таймслот

[запросить Имя - одним сообшением]

прислал ИМЯ

[запросить телефон - одним сообшением]

прислал телефон

[Запись сделана - регистрируем запись]

---------------------------------------------------------------
кнопки
- мои записи
- инфо
- выбор услуги






(message; id}
    -> check status
            -> NEW


[ id vs UserStatus ]





(message, id)






 */