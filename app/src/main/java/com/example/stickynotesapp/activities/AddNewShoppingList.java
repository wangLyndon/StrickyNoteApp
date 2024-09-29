package com.example.stickynotesapp.activities;

import static com.example.stickynotesapp.fragments.MyNotesFragment.REQUEST_CODE_ADD_NOTE;
import static com.example.stickynotesapp.fragments.MyNotesFragment.UPDATE;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.example.stickynotesapp.entities.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewShoppingList extends AppCompatActivity {

    private EditText inputTitle;
    private EditText inputText;
    private TextView dateTime;
    private View saveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_shopping_list);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        inputTitle = findViewById(R.id.input_note_title);
        saveNote = findViewById(R.id.save_note);
        inputText = findViewById(R.id.input_note_text);
        dateTime = findViewById(R.id.textDateTime);

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });

        dateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );
    }

    private void saveNotes() {
        if (inputTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note text can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final ShoppingList myNoteEntities = new ShoppingList();
        myNoteEntities.setTitle(inputTitle.getText().toString());
        myNoteEntities.setNoteText(inputText.getText().toString());
        myNoteEntities.setDateTime(dateTime.getText().toString());

        class  SaveNotes extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                MyNoteDatabase.getMyNoteDatabase(getApplicationContext()).notesDao().insertShoppingList(myNoteEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
//                intent.putExtra("REQUEST_CODE", REQUEST_CODE_ADD_NOTE); // 传递自定义的请求代码
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNotes().execute();
    }
}