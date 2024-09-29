package com.example.stickynotesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stickynotesapp.R;
import com.example.stickynotesapp.entities.ShoppingList;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    List<ShoppingList> lists;

    public ShoppingListAdapter(List<ShoppingList> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        holder.setNote(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView text;
        private TextView dateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.shopping_heading);
            text = itemView.findViewById(R.id.shopping_list_text);
            dateTime = itemView.findViewById(R.id.date_shop);
        }

        public void setNote(ShoppingList shoppingList) {
            title.setText(shoppingList.getTitle());
            text.setText(shoppingList.getNoteText());
            dateTime.setText(shoppingList.getDateTime());
        }
    }
}
