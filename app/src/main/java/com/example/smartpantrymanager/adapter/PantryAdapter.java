package com.example.smartpantrymanager.adapter;

// Used to create the layout/view for each RecyclerView item
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import com.example.smartpantrymanager.SettingsActivity;
import com.example.smartpantrymanager.util.FormatUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



// adapter connects the PantryItem data to the RecyclerView
public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {


    // interface is used to handle clicks on a pantry item
    public interface OnItemClickListener {

        // the method that will be called when an item is clicked
        void onItemClick(PantryItem item);
    }


    // a list containing all pantry items that must be displayed
    private List<PantryItem> pantryItems;
    private final OnItemClickListener listener; //intialize listener


    // constructor receives the list of pantry items and the click listener
    public PantryAdapter(
            List<PantryItem> pantryItems,
            OnItemClickListener listener) {

        this.pantryItems = pantryItems;
        this.listener = listener;
    }


    // used when we want to replace/update the data in the RecyclerView
    public void updateItems(List<PantryItem> newItems) {

        // replace the old list with the new list
        this.pantryItems = newItems;

        // tells RecyclerView that the data has been changed/modified
        notifyDataSetChanged();
    }
    public PantryItem getItemAt(int position) {
        return pantryItems.get(position);
    }


    // creates the ViewHolder for each row/item
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        // Convert the XML layout item_pantry into a Java View object
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        // Create and return a ViewHolder containing this view
        return new PantryViewHolder(view);
    }


    // Puts the data into the ViewHolder for a specific position
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);
        holder.tvItemName.setText(item.getName());

        String unit = item.getUnit() != null ? item.getUnit() : "";
        String details = FormatUtils.formatQuantity(item.getQuantity()) + " " + unit;
        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            details += " • expires " + item.getExpiryDate();
        }

        boolean highlightEnabled = holder.itemView.getContext()
                .getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.KEY_EXPIRY_HIGHLIGHT, true);

        if (highlightEnabled && isExpiringSoon(item.getExpiryDate())) {
            details += " (expiring soon)";
            holder.tvItemDetails.setTextColor(Color.parseColor("#C62828"));
        } else {
            holder.tvItemDetails.setTextColor(Color.parseColor("#666666")); // reset: rows get recycled
        }

        holder.tvItemDetails.setText(details.trim());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    private boolean isExpiringSoon(String expiry) {
        if (expiry == null || expiry.isEmpty()) return false;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            format.setLenient(false);
            Date date = format.parse(expiry);
            if (date == null) return false;
            long threeDaysMs = 3L * 24 * 60 * 60 * 1000;
            return date.getTime() < System.currentTimeMillis() + threeDaysMs;
        } catch (ParseException e) {
            return false; // unparseable date: just don't highlight
        }
    }

    @Override
    public int getItemCount() {//nr of ingredients
        return pantryItems.size();
    }


    // ViewHolder stores references to the views in one RecyclerView row
    static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvItemName, tvItemDetails;

        // constructor receives the XML view for one row
        PantryViewHolder(@NonNull View itemView) {

            super(itemView);


            // connect the Java textviews to the XML textviews
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);
        }
    }
}
