package com.example.smartpantrymanager.model;

public class RecipeIngredient {
    //setting variables aka columns that db will store
    private int id;
    private int recipeId;
    private String name;
    private double requiredQuantity;
    private String unit;

    public RecipeIngredient(int id, int recipeId, String name, double requiredQuantity, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }//constructor

    //getter and setter methods for values
    public int getId() { return id; }
    public int getRecipeId() { return recipeId; }
    public String getName() { return name; }
    public double getRequiredQuantity() { return requiredQuantity; }
    public String getUnit() { return unit; }
}