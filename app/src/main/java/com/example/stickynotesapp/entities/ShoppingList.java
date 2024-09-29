package com.example.stickynotesapp.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shoppingList")
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "dateTime")
    private String dateTime;

    @ColumnInfo(name = "noteText")
    private String noteText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
