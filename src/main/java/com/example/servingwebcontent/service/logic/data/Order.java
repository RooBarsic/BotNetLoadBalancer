package com.example.servingwebcontent.service.logic.data;

public class Order {
    private Master master;
    private String userName;
    private String phone;
    private Services services;
    private String date;

    Order() {
        master = null;
        userName = "null";
        phone = "null";
        services = Services.NONE;
        date = "null";
    }

    public String getOrderInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nУслуга : ").append(getServices().toString())
                .append("\nМастер : ").append(getMaster().getName())
                .append("\nВремя : ").append(getDate())
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

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
