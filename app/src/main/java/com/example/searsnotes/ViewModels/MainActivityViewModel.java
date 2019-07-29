package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.MainActivity;
import com.example.searsnotes.model.NotesVo;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class MainActivityViewModel extends AndroidViewModel {

    public String TAG= this.getClass().getSimpleName();
    private NotesDao notesDao;
    private NotesDatabase notesDatabaseInstace;
    private LiveData<List<NotesVo>> listOfNotes;

    @Inject
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        notesDatabaseInstace = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstace.notesDao();
        listOfNotes = notesDao.getListOfNotes();
    }


    public LiveData<List<NotesVo>> getListOfNotes(){
            return listOfNotes;
    }

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

   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST){
            if(resultCode==RESULT_OK){
                Bundle dataBundle = data.getBundleExtra("note_data");
                NotesVo note = new NotesVo();
                note.setNoteTitle(dataBundle.getString("title"));
                note.setNoteText(dataBundle.getString("text"));
                note.setNoteImage(dataBundle.getString("uri"));
                note.setNoteTime(dataBundle.getString("time"));
                addNote(note);
                Toast.makeText(getApplication().getApplicationContext(),"Note saved",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplication().getApplicationContext(),"Note Not saved",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"MainActivity View Model Destroyed");
    }
}
