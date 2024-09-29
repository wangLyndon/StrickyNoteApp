package com.example.stickynotesapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.stickynotesapp.entities.MyNoteEntities;
import com.example.stickynotesapp.entities.MyReminderEntities;
import com.example.stickynotesapp.entities.ShoppingList;

import java.util.List;

@Dao
public interface MyNotesDao {

    @Query("SELECT * FROM note ORDER BY id DESC")
    List<MyNoteEntities> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(MyNoteEntities myNoteEntities);

    @Delete
    void deleteNodes(MyNoteEntities myNoteEntities);

    @Query("SELECT * FROM reminder ORDER BY id DESC")
    List<MyReminderEntities> getAllReminder();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(MyReminderEntities myReminderEntities);

    @Delete
    void deleteReminder(MyReminderEntities myReminderEntities);

    @Query("SELECT * FROM shoppingList ORDER BY id DESC")
    List<ShoppingList> getAllNShoppingList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShoppingList(ShoppingList shoppingList);
}
