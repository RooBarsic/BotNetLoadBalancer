package com.example.servingwebcontent.service.logic;

public class MathUtils {

    public static String addMinutes20(String time) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(time.charAt(0))
                .append(time.charAt(1));
        int h = Integer.parseInt(stringBuilder.toString());

        stringBuilder
                .delete(0, 2)
                .append(time.charAt(3))
                .append(time.charAt(4));
        int m = Integer.parseInt(stringBuilder.toString());

        m += 20;
        h += (m / 60);
        m = (m % 60);
        stringBuilder
                .delete(0, 2)
                .append(numberToString(h))
                .append(":")
                .append(numberToString(m));
        return stringBuilder.toString();
    }

    public static String numberToString(int num) {
        if (num < 10) {
            return "0" + (char)((int)'0' + num);
        }
        else {
            return Integer.toString(num);
        }
    }
}
