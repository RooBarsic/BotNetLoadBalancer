package com.example.servingwebcontent.service.logic.data;

public enum BarberShopServise {
    STRIZKA,
    BORODA,
    NONE;

    public String toString() {
        switch (this) {
            case STRIZKA: return "стрижка";
            case BORODA: return "борода";
            case NONE: return "не выбрано";
            default: return "none";
        }
    }

    public static BarberShopServise getServiceOrNull(String text) {
        switch (text) {
            case "стрижка": return STRIZKA;
            case "борода": return BORODA;
            case "не выбрано": return NONE;
            default: return null;
        }
    }
}
