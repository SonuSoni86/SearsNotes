package com.example.searsnotes.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.searsnotes.model.NotesVo;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void addNote(NotesVo note);

    @Query("Select * from Notes_Table")
    LiveData<List<NotesVo>> getListOfNotes();

    @Query("Select * from Notes_Table")
    List<NotesVo> getListOfWidgetNotes();

    @Query("Select * from NOTES_TABLE where Note_ID=:noteId")
    LiveData<NotesVo> getNote(int noteId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(NotesVo note);

    @Delete
    void deleteNote(NotesVo note);
}
