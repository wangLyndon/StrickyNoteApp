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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stickynotesapp.R;
import com.example.stickynotesapp.activities.AddNewNotes;
import com.example.stickynotesapp.activities.AddNewShoppingList;
import com.example.stickynotesapp.adapters.MyNoteAdapter;
import com.example.stickynotesapp.adapters.ShoppingListAdapter;
import com.example.stickynotesapp.database.MyNoteDatabase;
import com.example.stickynotesapp.entities.MyReminderEntities;
import com.example.stickynotesapp.entities.ShoppingList;

import java.util.ArrayList;
import java.util.List;


public class ShoppingListFragment extends Fragment {

    ImageView addShoppingList;
    private ActivityResultLauncher<Intent> addListLauncher;

    RecyclerView noteRec;
    List<ShoppingList> noteEntitiesList;
    ShoppingListAdapter myNoteAdapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        addListLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    // 处理返回的结果
//                    Intent data = o.getData();
                    // 进行相应的操作
                    getAllShoppingList();
                }
            }
        });

        addShoppingList = view.findViewById(R.id.add_shopping_list);

        addShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewShoppingList.class);
                addListLauncher.launch(intent);
            }
        });

        noteRec = view.findViewById(R.id.shopping_rec);
        noteRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        noteEntitiesList = new ArrayList<>();
        myNoteAdapter = new ShoppingListAdapter(noteEntitiesList);
        noteRec.setAdapter(myNoteAdapter);

        getAllShoppingList();

        return view;

    }

    private void getAllShoppingList() {
        class GetAllReminder extends AsyncTask<Void, Void, List<ShoppingList>> {

            @Override
            protected List<ShoppingList> doInBackground(Void... voids) {
                return MyNoteDatabase.getMyNoteDatabase(getActivity().getApplicationContext()).notesDao().getAllNShoppingList();
            }

            @Override
            protected void onPostExecute(List<ShoppingList> myReminderEntities) {
                super.onPostExecute(myReminderEntities);

                if (noteEntitiesList.isEmpty()){
                    noteEntitiesList.addAll(myReminderEntities);
                    myNoteAdapter.notifyDataSetChanged();
                }else{
                    noteEntitiesList.add(0, myReminderEntities.get(0));
                    myNoteAdapter.notifyItemInserted(0);
                }

                noteRec.smoothScrollToPosition(0);
            }
        }
        new GetAllReminder().execute();
    }
}