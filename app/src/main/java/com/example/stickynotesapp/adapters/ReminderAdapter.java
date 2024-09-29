package com.example.stickynotesapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stickynotesapp.R;
import com.example.stickynotesapp.entities.MyReminderEntities;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHodler> {
    List<MyReminderEntities> reminderEntities;

    public ReminderAdapter(List<MyReminderEntities> reminderEntities) {
        this.reminderEntities = reminderEntities;
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHodler(LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHodler holder, int position) {

        holder.setReminder(reminderEntities.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return reminderEntities.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dateTime;

        private View view;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reminder_heading);
            dateTime = itemView.findViewById(R.id.date_reminder);
            view = itemView.findViewById(R.id.view_reminder);
        }

        public void setReminder(MyReminderEntities myReminderEntities) {
            title.setText(myReminderEntities.getTitle());
            dateTime.setText(myReminderEntities.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            if (myReminderEntities.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(myReminderEntities.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#FFFB7B"));
            }
        }
    }
}
