package com.example.searsnotes.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notes_Table")
public class NotesVo {

    @NonNull
    @ColumnInfo(name = "Note_ID")
    @PrimaryKey(autoGenerate = true)
    private int noteID;
    @ColumnInfo(name = "Note_Title")
    private String noteTitle;
    @ColumnInfo(name = "Note_Text")
    private  String noteText;
    @ColumnInfo (name = "Note_Time")
    private String noteTime;
    @ColumnInfo (name = "Note_Image")
    private String noteImage;


    @NonNull
    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(@NonNull int noteID) {
        this.noteID = noteID;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }
}
