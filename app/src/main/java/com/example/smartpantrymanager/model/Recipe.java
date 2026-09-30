package com.example.smartpantrymanager.model;

import java.util.List;

public class Recipe {
    //set vraibles
    private int id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients; // populated when needed, not always from DB row

    public Recipe(int id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }//constructor

    //getter and setter methods for values
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSteps() { return steps; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }
}