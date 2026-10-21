package com.example.smartpantrymanager.util;

public class FormatUtils {

    /** 2.0 -> "2", 0.75 -> "0.75" */
    public static String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}