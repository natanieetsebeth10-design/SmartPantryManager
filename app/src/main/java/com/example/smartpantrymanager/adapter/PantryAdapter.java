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
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);

        holder.tvItemName.setText(item.getName());

        String unit = item.getUnit() != null // get the unit, if unit is null we use an empty String instead
                ? item.getUnit()
                : "";

        String details = item.getQuantity() + " " + unit; //adds unit to quantity

        if (item.getExpiryDate() != null //validate expiry date
                && !item.getExpiryDate().isEmpty()) {

            details += " • expires " + item.getExpiryDate();
        }


        // display all details in textview
        holder.tvItemDetails.setText(details);


        // When the item is clicked, call the listener and pass the selected PantryItem
        holder.itemView.setOnClickListener(
                v -> listener.onItemClick(item)
        );
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
