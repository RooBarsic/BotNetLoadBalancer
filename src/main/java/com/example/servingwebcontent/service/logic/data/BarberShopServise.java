package com.example.servingwebcontent.service.logic.data;

public enum BarberShopServise {
    STRIZKA,
    BORODA,
    BORODA_AND_STRIZKA,
    NONE;

    public String toString() {
        switch (this) {
            case STRIZKA: return "Стрижка";
            case BORODA: return "Борода";
            case NONE: return "не выбрано";
            case BORODA_AND_STRIZKA: return "Борода и Стрижка";
            default: return "none";
        }
    }

    public static BarberShopServise getServiceOrNull(String text) {
        switch (text) {
            case "Стрижка": return STRIZKA;
            case "Борода": return BORODA;
            case "не выбрано": return NONE;
            case "Борода и Стрижка": return BORODA_AND_STRIZKA;
            default: return null;
        }
    }
}
