package com.example.servingwebcontent.service.logic.data;

public enum BarberShopServise {
    STRIZKA,
    BORODA,
    BORODA_AND_STRIZKA,
    NONE;

    public String toString() {
        switch (this) {
            case STRIZKA: return "стрижка";
            case BORODA: return "борода";
            case NONE: return "не выбрано";
            case BORODA_AND_STRIZKA: return "борода и стрижка";
            default: return "none";
        }
    }

    public static BarberShopServise getServiceOrNull(String text) {
        switch (text) {
            case "стрижка": return STRIZKA;
            case "борода": return BORODA;
            case "не выбрано": return NONE;
            case "борода и стрижка": return BORODA_AND_STRIZKA;
            default: return null;
        }
    }
}
