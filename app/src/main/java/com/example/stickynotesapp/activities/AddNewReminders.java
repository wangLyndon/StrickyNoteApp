package com.example.stickynotesapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stickynotesapp.R;
import com.example.stickynotesapp.database.MyNoteDatabase;
import com.example.stickynotesapp.entities.MyNoteEntities;
import com.example.stickynotesapp.entities.MyReminderEntities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewReminders extends AppCompatActivity {

    private EditText title;
    private TextView textDateTime;
    private TextView saveNote;
    private View view;
    String selectReminderColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_reminders);

        saveNote = findViewById(R.id.save_reminder);
        view = findViewById(R.id.view_reminder);
        title = findViewById(R.id.input_note_title);
        textDateTime = findViewById(R.id.textDateTime);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        selectReminderColor = "#FF937B";

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReminder();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        bottomSheet();
        setViewColor();
    }

    private void setViewColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectReminderColor));
    }

    private void bottomSheet() {
        final LinearLayout linearLayout = findViewById(R.id.reminder_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.bottom_text_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        final ImageView imgColor1 = linearLayout.findViewById(R.id.imageColor1);
        final ImageView imgColor2 = linearLayout.findViewById(R.id.imageColor2);
        final ImageView imgColor3 = linearLayout.findViewById(R.id.imageColor3);
        final ImageView imgColor4 = linearLayout.findViewById(R.id.imageColor4);


        linearLayout.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReminderColor = "#FF937B";

                imgColor1.setImageResource(R.drawable.baseline_done_24);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReminderColor = "#FFFB7B";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(R.drawable.baseline_done_24);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReminderColor = "#ADFF7B";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(R.drawable.baseline_done_24);
                imgColor4.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReminderColor = "#96FFEA";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(R.drawable.baseline_done_24);

                setViewColor();
            }
        });

    }

    private void saveReminder() {
        if (title.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final MyReminderEntities myNoteEntities = new MyReminderEntities();
        myNoteEntities.setTitle(title.getText().toString());
        myNoteEntities.setDateTime(textDateTime.getText().toString());
        myNoteEntities.setColor(selectReminderColor);

        class saveReminder extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                MyNoteDatabase.getMyNoteDatabase(getApplicationContext()).notesDao().insertReminder(myNoteEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new saveReminder().execute();
    }


}