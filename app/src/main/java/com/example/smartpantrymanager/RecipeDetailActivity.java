package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.util.FormatUtils;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = dbHelper.getRecipeById(recipeId);

        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(recipe.getName());
        }

        ((TextView) findViewById(R.id.tvDetailName)).setText(recipe.getName());
        ((TextView) findViewById(R.id.tvDetailSteps)).setText(recipe.getSteps());

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            sb.append("• ").append(FormatUtils.formatQuantity(ing.getRequiredQuantity()));
            if (ing.getUnit() != null && !ing.getUnit().isEmpty()) {
                sb.append(" ").append(ing.getUnit());
            }
            sb.append(" ").append(ing.getName()).append("\n");
        }
        ((TextView) findViewById(R.id.tvDetailIngredients)).setText(sb.toString().trim());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}