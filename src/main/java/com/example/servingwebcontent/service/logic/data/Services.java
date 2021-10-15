package com.example.servingwebcontent.service.logic.data;

public enum Services {
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

    public static Services getServiceOrNull(String text) {
        switch (text) {
            case "стрижка": return STRIZKA;
            case "борода": return BORODA;
            case "не выбрано": return NONE;
            default: return null;
        }
    }
}
