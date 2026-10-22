package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.logic.RecipeMatcher;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.util.NavHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvRecipes, rvAlmost;
    private TextView tvNoMatches, tvAlmostHeader;
    private RecipeAdapter adapter, almostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);
        rvRecipes = findViewById(R.id.rvRecipes);
        rvAlmost = findViewById(R.id.rvAlmost);
        tvNoMatches = findViewById(R.id.tvNoMatches);
        tvAlmostHeader = findViewById(R.id.tvAlmostHeader);

        RecipeAdapter.OnRecipeClickListener openDetail = recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        };

        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(new ArrayList<>(), openDetail);
        rvRecipes.setAdapter(adapter);

        rvAlmost.setLayoutManager(new LinearLayoutManager(this));
        almostAdapter = new RecipeAdapter(new ArrayList<>(), openDetail);
        rvAlmost.setAdapter(almostAdapter);

        NavHelper.setup(this, R.id.nav_recipes);
    }
    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.setup(this, R.id.nav_recipes);
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        List<Recipe> allRecipes = dbHelper.getAllRecipesWithIngredients();

        // Strict list (the core rule)
        List<Recipe> matches = RecipeMatcher.findStrictMatches(pantry, allRecipes);
        if (matches.isEmpty()) {
            rvRecipes.setVisibility(View.GONE);
            tvNoMatches.setVisibility(View.VISIBLE);
        } else {
            rvRecipes.setVisibility(View.VISIBLE);
            tvNoMatches.setVisibility(View.GONE);
            adapter.updateRecipes(matches);
        }

        // Bonus list, kept completely separate
        List<RecipeMatcher.AlmostMatch> almost = RecipeMatcher.findAlmostThere(pantry, allRecipes);
        List<Recipe> almostRecipes = new ArrayList<>();
        Map<Integer, String> missingMap = new HashMap<>();
        for (RecipeMatcher.AlmostMatch m : almost) {
            almostRecipes.add(m.recipe);
            missingMap.put(m.recipe.getId(), m.missingIngredient);
        }
        almostAdapter.setMissingMap(missingMap);
        almostAdapter.updateRecipes(almostRecipes);

        int visibility = almostRecipes.isEmpty() ? View.GONE : View.VISIBLE;
        tvAlmostHeader.setVisibility(visibility);
        rvAlmost.setVisibility(visibility);
    }
}