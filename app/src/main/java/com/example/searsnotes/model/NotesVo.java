package com.example.searsnotes.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes_Table")
public class NotesVo {

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
    @ColumnInfo (name = "Reminder_Time")
    private String noteReminderTime;
    @ColumnInfo (name = "Reminder_Date")
    private String noteReminderDate;
    @ColumnInfo (name = "Reminder_status")
    private boolean noteReminderStatus;

    public boolean isNoteReminderStatus() {
        return noteReminderStatus;
    }

    public void setNoteReminderStatus(boolean noteReminderStatus) {
        this.noteReminderStatus = noteReminderStatus;
    }

    public String getNoteReminderTime() {
        return noteReminderTime;
    }

    public void setNoteReminderTime(String noteReminderTime) {
        this.noteReminderTime = noteReminderTime;
    }

    public String getNoteReminderDate() {
        return noteReminderDate;
    }

    public void setNoteReminderDate(String noteReminderDate) {
        this.noteReminderDate = noteReminderDate;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID( int noteID) {
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
