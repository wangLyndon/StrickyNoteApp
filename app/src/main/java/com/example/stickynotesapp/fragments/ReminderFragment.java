package com.example.stickynotesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stickynotesapp.R;
import com.example.stickynotesapp.activities.AddNewNotes;
import com.example.stickynotesapp.activities.AddNewReminders;
import com.example.stickynotesapp.adapters.MyNoteAdapter;
import com.example.stickynotesapp.adapters.ReminderAdapter;
import com.example.stickynotesapp.database.MyNoteDatabase;
import com.example.stickynotesapp.entities.MyNoteEntities;
import com.example.stickynotesapp.entities.MyReminderEntities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ReminderFragment extends Fragment {
    ImageView addShoppingList;
    private ActivityResultLauncher<Intent> addReminderLauncher;

    private RecyclerView noteRec;
    private List<MyReminderEntities> reminderEntities;
    private ReminderAdapter reminderAdapter;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        addReminderLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    // 处理返回的结果
//                    Intent data = o.getData();
                    // 进行相应的操作
                    getAllReminder();
                }
            }
        });

        addShoppingList = view.findViewById(R.id.add_reminder);

        addShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewReminders.class);
                addReminderLauncher.launch(intent);
            }
        });

        noteRec = view.findViewById(R.id.reminder_rec);
        noteRec.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        reminderEntities = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(reminderEntities);
        noteRec.setAdapter(reminderAdapter);

        getAllReminder();

        return view;
    }

    private void getAllReminder() {


        class GetAllReminder extends AsyncTask<Void, Void, List<MyReminderEntities>> {

            @Override
            protected List<MyReminderEntities> doInBackground(Void... voids) {
                return MyNoteDatabase.getMyNoteDatabase(getActivity().getApplicationContext()).notesDao().getAllReminder();
            }

            @Override
            protected void onPostExecute(List<MyReminderEntities> myReminderEntities) {
                super.onPostExecute(myReminderEntities);

                if (reminderEntities.isEmpty()){
                    reminderEntities.addAll(myReminderEntities);
                    reminderAdapter.notifyDataSetChanged();
                }else{
                    reminderEntities.add(0, myReminderEntities.get(0));
                    reminderAdapter.notifyItemInserted(0);
                }

                noteRec.smoothScrollToPosition(0);
            }
        }
        new GetAllReminder().execute();
    }
}