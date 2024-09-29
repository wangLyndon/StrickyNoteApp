package com.example.stickynotesapp.activities;

import static com.example.stickynotesapp.fragments.MyNotesFragment.REQUEST_CODE_ADD_NOTE;
import static com.example.stickynotesapp.fragments.MyNotesFragment.UPDATE;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.stickynotesapp.R;
import com.example.stickynotesapp.database.MyNoteDatabase;
import com.example.stickynotesapp.entities.MyNoteEntities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNewNotes extends AppCompatActivity {

    private EditText inputNoteTitle;
    private EditText inputNoteText;

    private TextView textDateTime;
    private TextView saveNote;

    private View indicator1;
    private View indicator2;

    private MyNoteEntities alreadyDoneNote;
    private AlertDialog dialog;

    String selectedColor;

    ImageView addImg;
    String selectedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_notes);

        indicator1 = findViewById(R.id.viewIndication);
        indicator2 = findViewById(R.id.viewIndication2);
        saveNote = findViewById(R.id.save_note);
        inputNoteTitle = findViewById(R.id.input_note_title);
        inputNoteText = findViewById(R.id.input_note_text);
        textDateTime = findViewById(R.id.textDateTime);
        addImg = findViewById(R.id.image_note);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectedColor = "#FF937B";
        selectedImg = "";

        if (getIntent().getBooleanExtra("update", false)){
            alreadyDoneNote = (MyNoteEntities) getIntent().getSerializableExtra("myNotes");
            setViewUpdate();
        }

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        findViewById(R.id.img_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImg.setImageBitmap(null);
                addImg.setVisibility(View.GONE);
                findViewById(R.id.img_remove).setVisibility(View.GONE);

                selectedImg = "";
            }
        });

        bottomSheet();
        setViewColor();
    }

    private void setViewUpdate() {
        inputNoteTitle.setText(alreadyDoneNote.getTitle());
        inputNoteText.setText(alreadyDoneNote.getNoteText());
        textDateTime.setText(alreadyDoneNote.getDateTime());

        if (alreadyDoneNote.getImagePath() != null && !alreadyDoneNote.getImagePath().trim().isEmpty()){
            String imageUriString = alreadyDoneNote.getImagePath();
            if (imageUriString != null && !imageUriString.trim().isEmpty()) {
                Uri imageUri = Uri.parse(imageUriString);
//                Glide.with(this)
//                        .load(imageUri)
//                        placeholder(R.drawable.baseline_image_24).
//                        .error(R.drawable.baseline_image_24)
//                        .into(addImg);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Glide.with(this)
                            .load(imageUri)
                            .error(R.drawable.baseline_image_24)
                            .into(addImg);
                });
                addImg.setVisibility(View.VISIBLE);
                findViewById(R.id.img_remove).setVisibility(View.VISIBLE);
                selectedImg = alreadyDoneNote.getImagePath();
            }
        }
    }

    private void setViewColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) indicator1.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedColor));

        GradientDrawable gradientDrawable2 = (GradientDrawable) indicator2.getBackground();
        gradientDrawable2.setColor(Color.parseColor(selectedColor));
    }

    private void saveNotes() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note text can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final MyNoteEntities myNoteEntities = new MyNoteEntities();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setNoteText(inputNoteText.getText().toString());
        myNoteEntities.setDateTime(textDateTime.getText().toString());
        myNoteEntities.setColor(selectedColor);
        myNoteEntities.setImagePath(selectedImg);

        if (alreadyDoneNote != null){
            myNoteEntities.setId(alreadyDoneNote.getId());
        }

        class  SaveNotes extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                MyNoteDatabase.getMyNoteDatabase(getApplicationContext()).notesDao().insertNote(myNoteEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                if (alreadyDoneNote == null){
                    intent.putExtra("REQUEST_CODE", REQUEST_CODE_ADD_NOTE); // 传递自定义的请求代码
                }else{
                    intent.putExtra("REQUEST_CODE", UPDATE); // 传递自定义的请求代码
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNotes().execute();
    }

    private void bottomSheet(){
        final LinearLayout linearLayout = findViewById(R.id.bottom_layout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.bottom_text).setOnClickListener(new View.OnClickListener() {
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
        final ImageView imgColor5 = linearLayout.findViewById(R.id.imageColor5);
        final ImageView imgColor6 = linearLayout.findViewById(R.id.imageColor6);

        linearLayout.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = "#FF937B";

                imgColor1.setImageResource(R.drawable.baseline_done_24);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = "#FFFB7B";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(R.drawable.baseline_done_24);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = "#ADFF7B";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(R.drawable.baseline_done_24);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = "#96FFEA";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(R.drawable.baseline_done_24);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = "#969CFF";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(R.drawable.baseline_done_24);
                imgColor6.setImageResource(0);

                setViewColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = "#FF96F5";

                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(R.drawable.baseline_done_24);

                setViewColor();
            }
        });

        if (alreadyDoneNote != null && alreadyDoneNote.getColor() != null && !alreadyDoneNote.getColor().trim().isEmpty()){
            switch (alreadyDoneNote.getColor()){
                case "#FFFB7B":
                    linearLayout.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#ADFF7B":
                    linearLayout.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#96FFEA":
                    linearLayout.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#969CFF":
                    linearLayout.findViewById(R.id.viewColor5).performClick();
                    break;
                case "#FF96F5":
                    linearLayout.findViewById(R.id.viewColor6).performClick();
                    break;
            }
        }

        linearLayout.findViewById(R.id.add_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openGallery();
            }
        });

        if (alreadyDoneNote != null){
            linearLayout.findViewById(R.id.remove).setVisibility(View.VISIBLE);
            linearLayout.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });
        }

    }

    private void showDeleteDialog() {
        if (dialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddNewNotes.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNote_Container));

            builder.setView(view);
            dialog = builder.create();

            if (dialog.getWindow() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    class DeleteNoteTask extends AsyncTask<Void, Void, Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {

                            MyNoteDatabase.getMyNoteDatabase(getApplicationContext()).notesDao().deleteNodes(alreadyDoneNote);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);

                            Intent intent = new Intent();
                            intent.putExtra("REQUEST_CODE", UPDATE); // 传递自定义的请求代码
                            intent.putExtra("isDeleted", true);
                            setResult(RESULT_OK, intent);
                            dialog.dismiss();
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.textCancelNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();  // 确保对话框关闭
        }
        super.onDestroy();
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private void openGallery() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 及以上使用 ACTION_OPEN_DOCUMENT
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            // 低于 Android 4.4 使用 ACTION_GET_CONTENT
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                getContentResolver().takePersistableUriPermission(
                        selectedImageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );

                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    if (inputStream != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            addImg.setVisibility(View.VISIBLE);
                            addImg.setImageBitmap(bitmap);
                            selectedImg = selectedImageUri.toString();
                            findViewById(R.id.img_remove).setVisibility(View.VISIBLE);
//                            selectedImg = getPathFromUrl(selectedImageUri);
                        } else {
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}