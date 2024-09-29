package com.example.stickynotesapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.stickynotesapp.fragments.MyNotesFragment;
import com.example.stickynotesapp.fragments.ReminderFragment;
import com.example.stickynotesapp.fragments.ShoppingListFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottomNavBar);
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyNotesFragment()).commit();

        bottomMenu();
    }

    private void bottomMenu(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                if (i == R.id.home){
                    fragment = new MyNotesFragment();
                } else if (i == R.id.reminder) {
                    fragment = new ReminderFragment();
                } else if (i == R.id.shoppingList) {
                    fragment = new ShoppingListFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


            }
        });
    }
}