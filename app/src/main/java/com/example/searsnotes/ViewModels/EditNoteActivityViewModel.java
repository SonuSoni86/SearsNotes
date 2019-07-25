package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.model.NotesVo;

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

    public Bundle makeBundle(int noteID, EditText noteTitle, EditText noteText, String imageUri, String gettime) {
        Bundle noteDataBundle = new Bundle();
        noteDataBundle.putInt("id",noteID);
        noteDataBundle.putString("title",noteTitle.getText().toString().trim());
        noteDataBundle.putString("text",noteText.getText().toString().trim());
        if(imageUri==null){
            noteDataBundle.putString("uri","default");
        }
        else {
            noteDataBundle.putString("uri",imageUri);
        }
        noteDataBundle.putString("time", ImportantMethods.gettime());
        return  noteDataBundle;
    }

}
