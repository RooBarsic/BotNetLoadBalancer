package com.example.servingwebcontent.service.logic.data;

public class Order {
    private Master master;
    private String userName;
    private String phone;
    private BarberShopServise barberShopServise;
    private String timeSlot;
    private String date;

    Order() {
        master = null;
        userName = "null";
        phone = "null";
        barberShopServise = BarberShopServise.NONE;
        timeSlot = "null";
        date = "null";
    }

    public String getOrderInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nУслуга : ").append(getServices().toString())
                .append("\nМастер : ").append(getMaster().getName())
                .append("\nДата :").append(date)
                .append("\nВремя : ").append(getTimeSlot())
                .append("\nИмя клента : ").append(getUserName())
                .append("\nТелефон клиента : ").append(getPhone())
                .append("\n");
        return stringBuilder.toString();
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BarberShopServise getServices() {
        return barberShopServise;
    }

    public void setServices(BarberShopServise barberShopServise) {
        this.barberShopServise = barberShopServise;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
