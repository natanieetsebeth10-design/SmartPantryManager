package com.example.smartpantrymanager.logic;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.util.IngredientNormalizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecipeMatcher {

    private static final double EPSILON = 0.0001;

    /** A recipe that is missing exactly one ingredient. */
    public static class AlmostMatch {
        public final Recipe recipe;
        public final String missingIngredient;

        public AlmostMatch(Recipe recipe, String missingIngredient) {
            this.recipe = recipe;
            this.missingIngredient = missingIngredient;
        }
    }

    /** Strict rule: every ingredient present in at least the required quantity. */
    public static List<Recipe> findStrictMatches(List<PantryItem> pantry, List<Recipe> recipes) {
        Map<String, Double> totals = buildPantryTotals(pantry);
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (findMissing(recipe, totals).isEmpty()) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    /** Bonus list: recipes missing exactly one ingredient. Never overlaps with the strict list. */
    public static List<AlmostMatch> findAlmostThere(List<PantryItem> pantry, List<Recipe> recipes) {
        Map<String, Double> totals = buildPantryTotals(pantry);
        List<AlmostMatch> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<String> missing = findMissing(recipe, totals);
            if (missing.size() == 1) {
                result.add(new AlmostMatch(recipe, missing.get(0)));
            }
        }
        return result;
    }

    private static Map<String, Double> buildPantryTotals(List<PantryItem> pantry) {
        Map<String, Double> totals = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = makeKey(item.getName(), item.getUnit());
            double base = IngredientNormalizer.toBaseQuantity(item.getQuantity(), item.getUnit());
            Double existing = totals.get(key);
            totals.put(key, (existing == null ? 0 : existing) + base);
        }
        return totals;
    }

    // Returns the display names of every ingredient that is missing or insufficient.
    private static List<String> findMissing(Recipe recipe, Map<String, Double> pantryTotals) {
        Map<String, Double> required = new LinkedHashMap<>();
        Map<String, String> displayNames = new HashMap<>();

        for (RecipeIngredient ing : recipe.getIngredients()) {
            String key = makeKey(ing.getName(), ing.getUnit());
            double base = IngredientNormalizer.toBaseQuantity(ing.getRequiredQuantity(), ing.getUnit());
            Double existing = required.get(key);
            required.put(key, (existing == null ? 0 : existing) + base);
            if (!displayNames.containsKey(key)) {
                displayNames.put(key, ing.getName());
            }
        }

        List<String> missing = new ArrayList<>();
        for (Map.Entry<String, Double> entry : required.entrySet()) {
            Double have = pantryTotals.get(entry.getKey());
            if (have == null || have + EPSILON < entry.getValue()) {
                missing.add(displayNames.get(entry.getKey()));
            }
        }
        return missing;
    }

    private static String makeKey(String name, String unit) {
        return IngredientNormalizer.normalizeName(name) + "|"
                + IngredientNormalizer.getFamily(unit);
    }
}