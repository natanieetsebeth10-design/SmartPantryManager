package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.view.View;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    public static final String EXTRA_ITEM_NAME = "extra_item_name";
    public static final String EXTRA_ITEM_QUANTITY = "extra_item_quantity";
    public static final String EXTRA_ITEM_UNIT = "extra_item_unit";
    public static final String EXTRA_ITEM_EXPIRY = "extra_item_expiry"; //keys for passing data through intent

    private DatabaseHelper dbHelper;
    private EditText etName, etQuantity, etUnit, etExpiry;
    private TextView tvScreenTitle;

    private boolean isEditMode = false; //setDefault to false
    private int editItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // the screen has its own title
        }

        dbHelper = new DatabaseHelper(this);

        tvScreenTitle = findViewById(R.id.tvScreenTitle);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        Button btnSave = findViewById(R.id.btnSave);

        //for edit mode
        Intent incoming = getIntent();//gets intent used to open this activity
        if (incoming.hasExtra(EXTRA_ITEM_ID)) {
            isEditMode = true;
            editItemId = incoming.getIntExtra(EXTRA_ITEM_ID, -1);//get id if passed

            tvScreenTitle.setText("Edit Ingredient");
            etName.setText(incoming.getStringExtra(EXTRA_ITEM_NAME));
            etQuantity.setText(String.valueOf(incoming.getDoubleExtra(EXTRA_ITEM_QUANTITY, 0)));
            etUnit.setText(incoming.getStringExtra(EXTRA_ITEM_UNIT));
            etExpiry.setText(incoming.getStringExtra(EXTRA_ITEM_EXPIRY));//updates info for ingredients
            Button btnDelete = findViewById(R.id.btnDelete);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle("Delete ingredient")
                    .setMessage("Remove " + etName.getText().toString().trim() + " from your pantry?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deletePantryItem(editItemId);
                        Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show());
        }

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        //validation checks
        if (name.isEmpty()) {
            etName.setError("Ingredient name is required");
            etName.requestFocus();
            return;
        }

        if (quantityStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
            if (quantity <= 0) {
                etQuantity.setError("Quantity must be greater than 0");
                etQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        // for optional info  we replace blank strings with null
        String finalUnit = unit.isEmpty() ? null : unit;
        String finalExpiry = expiry.isEmpty() ? null : expiry;
        if (!expiry.isEmpty()) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            fmt.setLenient(false); // rejects impossible dates like 2026-13-45
            try {
                Date parsed = fmt.parse(expiry);
                expiry = fmt.format(parsed); // normalises e.g. 2026-1-5 to 2026-01-05
            } catch (ParseException e) {
                etExpiry.setError("Use format yyyy-MM-dd (e.g. 2026-11-30)");
                etExpiry.requestFocus();
                return;
            }
        }
        if (isEditMode) {
            PantryItem updated = new PantryItem(editItemId, name, quantity, finalUnit, finalExpiry);
            dbHelper.updatePantryItem(updated);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();//toast message for edited items
        } else {
            PantryItem newItem = new PantryItem(name, quantity, finalUnit, finalExpiry);
            dbHelper.insertPantryItem(newItem);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();//toast message for new item added
        }

        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}