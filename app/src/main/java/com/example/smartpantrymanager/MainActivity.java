package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.NavHelper;

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
        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        });
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        setupSwipeToDelete();
        NavHelper.setup(this, R.id.nav_pantry);
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
                    Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                    intent.putExtra(AddEditActivity.EXTRA_ITEM_ID, item.getId());
                    intent.putExtra(AddEditActivity.EXTRA_ITEM_NAME, item.getName());
                    intent.putExtra(AddEditActivity.EXTRA_ITEM_QUANTITY, item.getQuantity());
                    intent.putExtra(AddEditActivity.EXTRA_ITEM_UNIT, item.getUnit());
                    intent.putExtra(AddEditActivity.EXTRA_ITEM_EXPIRY, item.getExpiryDate());
                    startActivity(intent);
                });
                rvPantry.setAdapter(adapter);
            } else {
                adapter.updateItems(items);
            }
        }
    }
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        //enabling swiping left and right nu not drag and drop
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false; // because we aren't supporting drag-to-reorder just swipe-to-delete
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();//viewHolder is to get row
                PantryItem item = adapter.getItemAt(position); //gets item by its row id

                new AlertDialog.Builder(MainActivity.this) //confirmation dialogue
                        .setTitle("Delete ingredient")
                        .setMessage("Remove " + item.getName() + " from your pantry?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbHelper.deletePantryItem(item.getId());//delete it
                            loadPantryItems();//reload updated info
                            Toast.makeText(MainActivity.this,
                                    "Ingredient deleted", Toast.LENGTH_SHORT).show(); //alert it was successfull
                        })
                        .setNegativeButton("Cancel", (dialog, which) ->
                                adapter.notifyItemChanged(position)) // restores instead odf deleting ingredient
                        .setOnCancelListener(dialog ->
                                adapter.notifyItemChanged(position)) // same thing happens if tapping outside the toast message
                        .show();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(rvPantry);//Activates swipe handling on the RecyclerView.
    }
}