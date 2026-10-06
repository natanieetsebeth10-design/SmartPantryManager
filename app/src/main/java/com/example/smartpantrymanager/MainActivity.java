package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvPantry;
    private TextView tvEmptyState;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        rvPantry = findViewById(R.id.rvPantry);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        rvPantry.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();

        if (items.isEmpty()) {
            rvPantry.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvPantry.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new PantryAdapter(items, item -> {

                });
                rvPantry.setAdapter(adapter);
            } else {
                adapter.updateItems(items);
            }
        }
    }
}