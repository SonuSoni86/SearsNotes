package com.example.searsnotes.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.searsnotes.model.NotesVo;

@Database(entities = NotesVo.class,version = 1,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao notesDao();
    private static volatile NotesDatabase notesDatabaseInstance;

    public static NotesDatabase getNotesDatabaseInstance(final Context context){
        if(notesDatabaseInstance==null){
            synchronized (NotesDatabase.class) {
                if(notesDatabaseInstance==null){
                    notesDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),NotesDatabase.class,"Notes_Database").build();
                }
            }
        }
        return  notesDatabaseInstance;
    }

}
