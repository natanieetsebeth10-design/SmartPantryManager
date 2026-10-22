package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.smartpantrymanager.util.NavHelper;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "smart_pantry_prefs";
    public static final String KEY_EXPIRY_HIGHLIGHT = "expiry_highlight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SwitchCompat switchExpiry = findViewById(R.id.switchExpiry);

        switchExpiry.setChecked(prefs.getBoolean(KEY_EXPIRY_HIGHLIGHT, true));
        switchExpiry.setOnCheckedChangeListener((button, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_HIGHLIGHT, isChecked).apply());

        NavHelper.setup(this, R.id.nav_settings);
    }
    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.setup(this, R.id.nav_settings);
    }
}