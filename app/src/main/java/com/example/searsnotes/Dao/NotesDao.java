package com.example.searsnotes.Dao;

import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.searsnotes.Model.NotesVo;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void addNote(NotesVo note);

    @Query("Select * from Notes_Table")
    LiveData<List<NotesVo>> getListOfNotes();

    @Query("Select * from NOTES_TABLE where Note_ID=:noteId")
    LiveData<NotesVo> getNote(int noteId);

   /* @Update
    void updateNote(NotesVo note);*/

     @Query("Update Notes_Table set Note_Title=:title, Note_Text=:text, Note_Time=:time, Note_Image=:image Where Note_ID=:id")
     void updateNote(int id,String title,String text,String time, String image);

    @Delete
    void deleteNote(NotesVo note);
}
