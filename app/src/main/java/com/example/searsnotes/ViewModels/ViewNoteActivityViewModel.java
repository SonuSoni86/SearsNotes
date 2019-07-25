package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.R;
import com.example.searsnotes.model.NotesVo;

import static android.app.Activity.RESULT_OK;

public class ViewNoteActivityViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private NotesDao notesDao;
    private NotesDatabase notesDatabaseInstance;


    public ViewNoteActivityViewModel(@NonNull Application application) {
        super(application);
        notesDatabaseInstance = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstance.notesDao();
    }
    public LiveData<NotesVo> getNote(int noteId){ return  notesDao.getNote(noteId); }
    public void updateNote(NotesVo note){new UpdateNoteAsyncTask(notesDao).execute(note); }
    public void deleteNote(NotesVo note){ new DeleteNoteAsyncTask(notesDao).execute(note); }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IntentRequestCodes.UPDATE_NOTE_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle dataBundle = data.getBundleExtra("note_data");
                NotesVo notesVo = new NotesVo();
                notesVo.setNoteID(dataBundle.getInt("id"));
                notesVo.setNoteTitle(dataBundle.getString("title"));
                notesVo.setNoteText(dataBundle.getString("text"));
                notesVo.setNoteImage(dataBundle.getString("uri"));
                notesVo.setNoteTime(dataBundle.getString("time"));
                updateNote(notesVo);
                Toast.makeText(getApplication().getApplicationContext(), notesVo.getNoteTitle(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplication().getApplicationContext(), "Edited Note Not saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"ViewNoteActivity destroyed");

    }

    private class UpdateNoteAsyncTask extends AsyncTask<NotesVo,Void,Void> {
        NotesDao notesDao;
        public UpdateNoteAsyncTask(NotesDao notesDao) { this.notesDao = notesDao;}

        @Override
        protected Void doInBackground(NotesVo... notesVos) {
            notesDao.updateNote(notesVos[0]);
            return null;
        }
    }

    private class DeleteNoteAsyncTask extends AsyncTask<NotesVo,Void,Void> {
        NotesDao notesDao;
        public DeleteNoteAsyncTask(NotesDao notesDao) { this.notesDao = notesDao;}

        @Override
        protected Void doInBackground(NotesVo... notesVos) {
            notesDao.deleteNote(notesVos[0]);
            return null;
        }
    }

}
