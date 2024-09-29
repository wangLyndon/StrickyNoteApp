package com.example.stickynotesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stickynotesapp.R;
import com.example.stickynotesapp.entities.MyNoteEntities;
import com.example.stickynotesapp.listeners.MyNoteListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Handler;

public class MyNoteAdapter extends RecyclerView.Adapter<MyNoteAdapter.ViewHolder> {

    List<MyNoteEntities> noteEntities;
    private Context context;
    MyNoteListeners myNoteListeners;

    private List<MyNoteEntities> noteSearch;
    private Timer timer;
    private Activity activity;

    public MyNoteAdapter(Activity activity, Context context, List<MyNoteEntities> noteEntities, MyNoteListeners myNoteListeners) {
        this.activity = activity;
        this.context = context;
        this.myNoteListeners = myNoteListeners;
        this.noteEntities = noteEntities;
        this.noteSearch = noteEntities;
    }

    @NonNull
    @Override
    public MyNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyNoteAdapter.ViewHolder holder, int position) {
        holder.setNote(noteEntities.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();  // 动态获取位置
                if (currentPosition != RecyclerView.NO_POSITION) {
                    myNoteListeners.myNoteClick(noteEntities.get(currentPosition), currentPosition);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return noteEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView text;
        private TextView dateTime;

        private LinearLayout linearLayout;

        RoundedImageView roundedImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textTitle);
            text = itemView.findViewById(R.id.textNote);
            dateTime = itemView.findViewById(R.id.textDateTime);
            linearLayout = itemView.findViewById(R.id.layoutNote);
            roundedImageView = itemView.findViewById(R.id.imageNote_item);
        }

        public void setNote(MyNoteEntities myNoteEntities) {
            title.setText(myNoteEntities.getTitle());
            text.setText(myNoteEntities.getNoteText());
            dateTime.setText(myNoteEntities.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) linearLayout.getBackground();
            if (myNoteEntities.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(myNoteEntities.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#FF937B"));
            }

            if (myNoteEntities.getImagePath() != null){
                String imageUriString = myNoteEntities.getImagePath();
                if (imageUriString != null && !imageUriString.trim().isEmpty()) {
                    Uri imageUri = Uri.parse(imageUriString);
                    Glide.with(context)
                            .load(imageUri)
                            .placeholder(R.drawable.baseline_image_24)
                            .error(R.drawable.baseline_image_24)
                            .into(roundedImageView);
                }


//                roundedImageView.setImageBitmap(BitmapFactory.decodeFile(myNoteEntities.getImagePath()));
                roundedImageView.setVisibility(View.VISIBLE);
            }else{
                roundedImageView.setVisibility(View.GONE);
            }
        }
    }

    public void searchNote(String searchNote){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchNote.trim().isEmpty()){
                    noteEntities = noteSearch;
                }else{
                    ArrayList<MyNoteEntities> temp = new ArrayList<>();
                    for (MyNoteEntities entities : noteSearch) {
                        if (entities.getTitle().toLowerCase().contains(searchNote.toLowerCase()) ||
                        entities.getNoteText().toLowerCase().contains(searchNote.toLowerCase())){
                            temp.add(entities);
                        }
                    }
                    noteEntities = temp;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();  // 更新 UI 的操作
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }
}
