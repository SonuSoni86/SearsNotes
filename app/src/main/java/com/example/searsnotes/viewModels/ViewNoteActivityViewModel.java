package com.example.searsnotes.viewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;import androidx.lifecycle.LiveData;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.dao.NotesDao;
import com.example.searsnotes.dao.NotesDatabase;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.navigators.ViewNoteActivityNavigator;

import static android.app.Activity.RESULT_OK;

public class ViewNoteActivityViewModel extends BaseViewModel<ViewNoteActivityNavigator> {

    private final String TAG = this.getClass().getSimpleName();
    private NotesDao notesDao;


    public ViewNoteActivityViewModel(@NonNull Application application) {
        super(application);
        NotesDatabase notesDatabaseInstance = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstance.notesDao();
    }
    public LiveData<NotesVo> getNote(int noteId){ return  notesDao.getNote(noteId); }
    private void updateNote(NotesVo note){new UpdateNoteAsyncTask(notesDao).execute(note); }
    public void deleteNote(NotesVo note){ new DeleteNoteAsyncTask(notesDao).execute(note); }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IntentRequestCodes.UPDATE_NOTE_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Bundle dataBundle = data.getBundleExtra("note_data");
                NotesVo notesVo = new NotesVo();
                assert dataBundle != null;
                notesVo.setNoteID(dataBundle.getInt("id"));
                notesVo.setNoteTitle(dataBundle.getString("title"));
                notesVo.setNoteText(dataBundle.getString("text"));
                notesVo.setNoteImage(dataBundle.getString("uri"));
                notesVo.setNoteTime(dataBundle.getString("time"));
                notesVo.setNoteReminderTime(dataBundle.getString("reminderTime"));
                notesVo.setNoteReminderDate(dataBundle.getString("reminderDate"));
                notesVo.setNoteReminderStatus(dataBundle.getBoolean("reminderStatus"));
                notesVo.setNoteReminderId(dataBundle.getString("reminderID"));
                updateNote(notesVo);
               Toast.makeText(getApplication().getApplicationContext(), "Edited note saved", Toast.LENGTH_LONG).show();

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

    @SuppressLint("StaticFieldLeak")
    private class UpdateNoteAsyncTask extends AsyncTask<NotesVo,Void,Void> {
        NotesDao notesDao;
        UpdateNoteAsyncTask(NotesDao notesDao) { this.notesDao = notesDao;}

        @Override
        protected Void doInBackground(NotesVo... notesVos) {
            notesDao.updateNote(notesVos[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteNoteAsyncTask extends AsyncTask<NotesVo,Void,Void> {
        NotesDao notesDao;
        DeleteNoteAsyncTask(NotesDao notesDao) { this.notesDao = notesDao;}

        @Override
        protected Void doInBackground(NotesVo... notesVos) {
            notesDao.deleteNote(notesVos[0]);
            return null;
        }
    }

    public void discardBtnClicked() { getNavigator().discardBtnClicked();}
    public void editBtnClicked() {getNavigator().editBtnClicked(); }
    public void deleteBtnClicked() {getNavigator().deleteBtnClicked(); }

}
