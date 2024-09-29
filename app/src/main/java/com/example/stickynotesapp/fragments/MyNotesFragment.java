package com.example.stickynotesapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.stickynotesapp.R;
import com.example.stickynotesapp.activities.AddNewNotes;
import com.example.stickynotesapp.adapters.MyNoteAdapter;
import com.example.stickynotesapp.database.MyNoteDatabase;
import com.example.stickynotesapp.entities.MyNoteEntities;
import com.example.stickynotesapp.listeners.MyNoteListeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyNotesFragment extends Fragment implements MyNoteListeners {

    ImageView addNotes;
    private ActivityResultLauncher<Intent> addNoteLauncher;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public final static int SHOW_NOTE = 10;
    public final static int UPDATE = 11;
    private RecyclerView noteRec;
    private List<MyNoteEntities> noteEntitiesList;
    private MyNoteAdapter myNoteAdapter;
    private int clickedPosition = -1;

    public MyNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);

        addNotes = view.findViewById(R.id.add_notes);

        addNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getData() == null){
                    return;
                }


                int clickedPosition = o.getData().getIntExtra("REQUEST_CODE", -1);
                boolean isNoteDeleted = o.getData().getBooleanExtra("isDeleted", false);

                if (o.getResultCode() == Activity.RESULT_OK && clickedPosition == REQUEST_CODE_ADD_NOTE) {
//                     处理返回的结果
//                    Intent data = o.getData();
//                     进行相应的操作
                    getAllNotes(REQUEST_CODE_ADD_NOTE, false);
                } else if (o.getResultCode() == Activity.RESULT_OK && clickedPosition == UPDATE) {
                    getAllNotes(UPDATE, isNoteDeleted);
                }
            }
        });

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewNotes.class);
                addNoteLauncher.launch(intent);
            }
        });

        noteRec = view.findViewById(R.id.note_rec);
        noteRec.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteEntitiesList = new ArrayList<>();
        myNoteAdapter = new MyNoteAdapter(getActivity(), getContext(), noteEntitiesList, this);
        noteRec.setAdapter(myNoteAdapter);

        EditText inputSearch = view.findViewById(R.id.editText3);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (noteEntitiesList.size() != 0){
                    myNoteAdapter.searchNote(editable.toString());
                }
            }
        });

        getAllNotes(SHOW_NOTE, false);

        return view;
    }

    private void getAllNotes(int requestCode, boolean isNoteDeleted) {
        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<MyNoteEntities>>{

            @Override
            protected List<MyNoteEntities> doInBackground(Void... voids) {
                return MyNoteDatabase.getMyNoteDatabase(getActivity().getApplicationContext()).notesDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<MyNoteEntities> myNoteEntities) {
                super.onPostExecute(myNoteEntities);

                if (requestCode == SHOW_NOTE){
                    noteEntitiesList.addAll(myNoteEntities);
                    myNoteAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteEntitiesList.add(0, myNoteEntities.get(0));
                    myNoteAdapter.notifyItemInserted(0);
                    noteRec.smoothScrollToPosition(0);
                } else if (requestCode == UPDATE) {
                    noteEntitiesList.remove(clickedPosition);
                    if (isNoteDeleted){
                        myNoteAdapter.notifyItemRemoved(clickedPosition);
                    }else{
                        noteEntitiesList.add(clickedPosition, myNoteEntities.get(clickedPosition));
                        myNoteAdapter.notifyItemChanged(clickedPosition);
                    }
                }
            }
        }

        new GetNoteTask().execute();
    }


    @Override
    public void myNoteClick(MyNoteEntities myNoteEntities, int position) {
        clickedPosition = position;
        Intent intent = new Intent(getContext().getApplicationContext(), AddNewNotes.class);
        intent.putExtra("update", true);
        intent.putExtra("myNotes", myNoteEntities);
        addNoteLauncher.launch(intent);
    }
}