package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.Model.NotesVo;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    public String TAG= this.getClass().getSimpleName();
    private NotesDao notesDao;
    private NotesDatabase notesDatabaseInstace;
    private LiveData<List<NotesVo>> listOfNotes;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        notesDatabaseInstace = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstace.notesDao();
        listOfNotes = notesDao.getListOfNotes();
    }


    public LiveData<List<NotesVo>> getListOfNotes(){return listOfNotes;}
    public void addNote(NotesVo note) {
        new AddNoteAsyncTask(notesDao).execute(note);
    }

    private class AddNoteAsyncTask extends AsyncTask<NotesVo, Void, Void> {

        NotesDao mNotesDao;

        public AddNoteAsyncTask(NotesDao notesDao) {
            mNotesDao = notesDao;
        }

        @Override
        protected Void doInBackground(NotesVo... notesVos) {
            mNotesDao.addNote(notesVos[0]);
            return null;
        }
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"MainActivity View Model Destroyed");
    }
}
