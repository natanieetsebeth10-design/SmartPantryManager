package com.example.smartpantrymanager.logic;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.util.IngredientNormalizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMatcher {

    private static final double EPSILON = 0.0001;

    // Finds recipes that can be made using the available pantry ingredients
    public static List<Recipe> findStrictMatches(List<PantryItem> pantry, List<Recipe> recipes) {

        // Adds up the quantities of ingredients in the pantry
        Map<String, Double> pantryTotals = buildPantryTotals(pantry);
        List<Recipe> matches = new ArrayList<>();

        // Checks each recipe and keeps the ones with no missing ingredients
        for (Recipe recipe : recipes) {
            if (countMissing(recipe, pantryTotals) == 0) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    // Calculates the total quantity available for each pantry ingredient
    private static Map<String, Double> buildPantryTotals(List<PantryItem> pantry) {
        Map<String, Double> totals = new HashMap<>();

        for (PantryItem item : pantry) {

            // Creates a key using the ingredient name and unit type
            String key = makeKey(item.getName(), item.getUnit());

            // Converts the quantity to a common unit before adding it
            double base = IngredientNormalizer.toBaseQuantity(
                    item.getQuantity(), item.getUnit());

            Double existing = totals.get(key);
            totals.put(key, (existing == null ? 0 : existing) + base);
        }

        return totals;
    }

    // Counts how many required ingredients are missing or have insufficient quantities
    private static int countMissing(Recipe recipe, Map<String, Double> pantryTotals) {

        // Combines repeated ingredients in the recipe before comparing quantities
        Map<String, Double> required = new HashMap<>();

        for (RecipeIngredient ing : recipe.getIngredients()) {

            // Creates a key so matching ingredients can be grouped together
            String key = makeKey(ing.getName(), ing.getUnit());

            // Converts the required quantity to the same base unit
            double base = IngredientNormalizer.toBaseQuantity(
                    ing.getRequiredQuantity(), ing.getUnit());

            Double existing = required.get(key);
            required.put(key, (existing == null ? 0 : existing) + base);
        }

        int missing = 0;

        // Compares the required quantities with what is available in the pantry
        for (Map.Entry<String, Double> entry : required.entrySet()) {
            Double have = pantryTotals.get(entry.getKey());

            // Counts an ingredient as missing if there is not enough available
            if (have == null || have + EPSILON < entry.getValue()) {
                missing++;
            }
        }

        return missing;
    }

    // Creates a standard key so ingredients can be matched by name and unit type
    private static String makeKey(String name, String unit) {
        return IngredientNormalizer.normalizeName(name) + "|"
                + IngredientNormalizer.getFamily(unit);
    }
}
