package com.example.servingwebcontent.service.logic.data;

import com.example.message.data.UiPlatform;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private UiPlatform platform;
    private Status status;
    private Order defaultOrder;
    private List<Order> activeOrders = new ArrayList<>();

    public User(UiPlatform uiPlatform, String id) {
        this.platform = uiPlatform;
        this.id = id;
        defaultOrder = new Order();
        status = Status.NEW_USER;
    }

    public void saveOrder() {
        if (status == Status.ORDER_COMPLETED) {
            activeOrders.add(defaultOrder);
            defaultOrder = new Order();
            status = Status.NEW_USER;
        }
    }

    public String getMyActiveOrders() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < activeOrders.size(); i++) {
            Order order = activeOrders.get(i);
            stringBuilder.append((i + 1)).append(". ")
                    .append(order.getOrderInfo())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UiPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(UiPlatform platform) {
        this.platform = platform;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Order getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Order defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
}
