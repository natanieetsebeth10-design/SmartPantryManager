package com.example.smartpantrymanager.util;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.MainActivity;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.SettingsActivity;
import com.example.smartpantrymanager.SuggestedRecipesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavHelper {

    public static void setup(AppCompatActivity activity, int selectedItemId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottomNav);
        nav.setSelectedItemId(selectedItemId);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) return true; // already on this screen

            Class<?> target;
            if (id == R.id.nav_pantry) {
                target = MainActivity.class;
            } else if (id == R.id.nav_recipes) {
                target = SuggestedRecipesActivity.class;
            } else {
                target = SettingsActivity.class;
            }

            Intent intent = new Intent(activity, target);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);
            return true;
        });
    }
}