package com.example.stickynotesapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.stickynotesapp.dao.MyNotesDao;
import com.example.stickynotesapp.entities.MyNoteEntities;
import com.example.stickynotesapp.entities.MyReminderEntities;
import com.example.stickynotesapp.entities.ShoppingList;

@Database(entities = {MyNoteEntities.class, MyReminderEntities.class, ShoppingList.class}, version = 1, exportSchema = false)
public abstract class MyNoteDatabase extends RoomDatabase {
    private static MyNoteDatabase myNoteDatabase;

    public static synchronized MyNoteDatabase getMyNoteDatabase(Context context){
        if (myNoteDatabase == null){
            myNoteDatabase = Room.databaseBuilder(context, MyNoteDatabase.class, "note_db").build();
        }
        return myNoteDatabase;
    }

    public abstract MyNotesDao notesDao();
}
