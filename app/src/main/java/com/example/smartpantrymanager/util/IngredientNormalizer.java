package com.example.smartpantrymanager.util;

import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class IngredientNormalizer {

    // Groups units into weight, volume or count
    public enum UnitFamily { WEIGHT, VOLUME, COUNT }

    // Ingredients that should not have their names changed to singular
    private static final Set<String> NO_SINGULAR = new HashSet<>(Arrays.asList(
            "boerewors", "couscous", "hummus", "asparagus", "molasses", "watercress"));

    // Cleans up an ingredient name and changes the last word to singular
    public static String normalizeName(String raw) {
        if (raw == null) return "";

        // Converts the name to lowercase and removes extra spaces
        String s = raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");

        // Keeps the earlier words the same and separates the last word
        int lastSpace = s.lastIndexOf(' ');
        String prefix = lastSpace >= 0 ? s.substring(0, lastSpace + 1) : "";
        String lastWord = s.substring(lastSpace + 1);

        return prefix + singularize(lastWord);
    }

    // Changes common plural words into singular words
    private static String singularize(String w) {
        if (w.length() <= 3 || NO_SINGULAR.contains(w)) return w;

        // Changes words ending in "ies" to end in "y"
        if (w.endsWith("ies")) return w.substring(0, w.length() - 3) + "y";

        // Removes "es" from common plural endings
        if (w.endsWith("oes") || w.endsWith("xes") || w.endsWith("ches")
                || w.endsWith("shes") || w.endsWith("sses")) {
            return w.substring(0, w.length() - 2);
        }

        // Leaves words ending in "ss" or "us" unchanged
        if (w.endsWith("ss") || w.endsWith("us")) return w;

        // Removes the final "s" from other plural words
        if (w.endsWith("s")) return w.substring(0, w.length() - 1);

        return w;
    }

    // Cleans up a unit by removing extra spaces and converting it to lowercase
    public static String normalizeUnit(String raw) {
        return raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
    }

    // Checks whether a unit measures weight, volume or individual items
    public static UnitFamily getFamily(String unit) {
        switch (normalizeUnit(unit)) {
            case "g": case "gram": case "grams":
            case "kg": case "kilogram": case "kilograms":
                return UnitFamily.WEIGHT;

            case "ml": case "l": case "litre": case "litres":
            case "liter": case "liters":
            case "cup": case "cups":
            case "tbsp": case "tablespoon": case "tablespoons":
            case "tsp": case "teaspoon": case "teaspoons":
                return UnitFamily.VOLUME;

            default:
                return UnitFamily.COUNT;
        }
    }

    // Converts quantities into a common base unit for easier comparison
    public static double toBaseQuantity(double quantity, String unit) {
        switch (normalizeUnit(unit)) {
            // Converts kilograms to grams
            case "kg": case "kilogram": case "kilograms":
                return quantity * 1000;

            // Converts litres to millilitres
            case "l": case "litre": case "litres": case "liter": case "liters":
                return quantity * 1000;

            // Converts cups to millilitres
            case "cup": case "cups":
                return quantity * 250;

            // Converts tablespoons to millilitres
            case "tbsp": case "tablespoon": case "tablespoons":
                return quantity * 15;

            // Converts teaspoons to millilitres
            case "tsp": case "teaspoon": case "teaspoons":
                return quantity * 5;

            // Leaves units such as grams and millilitres unchanged
            default:
                return quantity;
        }
    }
}