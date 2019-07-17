package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.Model.NotesVo;

public class EditNoteActivityViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private NotesDao notesDao;
    private NotesDatabase notesDatabaseInstance;


    public EditNoteActivityViewModel(@NonNull Application application) {
        super(application);
        notesDatabaseInstance = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstance.notesDao();
    }
    public LiveData<NotesVo> getNote(int noteId){
        return  notesDao.getNote(noteId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"EditNoteActivity destroyed");
    }
}
