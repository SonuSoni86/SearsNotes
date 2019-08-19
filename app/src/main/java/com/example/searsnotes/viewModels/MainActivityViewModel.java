package com.example.searsnotes.viewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.navigators.MainActivityNavigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class MainActivityViewModel extends BaseViewModel<MainActivityNavigator> {

    private String TAG= this.getClass().getSimpleName();
    private NotesDao notesDao;
    private LiveData<List<NotesVo>> listOfNotes;

    @Inject
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        NotesDatabase notesDatabaseInstace = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstace.notesDao();
        listOfNotes = notesDao.getListOfNotes();
    }


    public LiveData<List<NotesVo>> getListOfNotes(){
            return listOfNotes;
    }
    public void updateNote(NotesVo note){ new UpdateNoteAsyncTask(notesDao).execute(note); }

    private void addNote(NotesVo note) {
        new AddNoteAsyncTask(notesDao).execute(note);
    }

    public void hideFab(RecyclerView notesView, final FloatingActionButton addNoteBtn) {
        notesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && addNoteBtn.isShown())
                {
                    addNoteBtn.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    addNoteBtn.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class AddNoteAsyncTask extends AsyncTask<NotesVo, Void, Void> {

        NotesDao mNotesDao;

        AddNoteAsyncTask(NotesDao notesDao) {
            mNotesDao = notesDao;
        }

        @Override
        protected Void doInBackground(NotesVo... notesVos) {
            mNotesDao.addNote(notesVos[0]);
            return null;
        }
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

   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST){
            if(resultCode==RESULT_OK){
                assert data != null;
                Bundle dataBundle = data.getBundleExtra("note_data");
                NotesVo note = new NotesVo();
                assert dataBundle != null;
                note.setNoteTitle(dataBundle.getString("title"));
                note.setNoteText(dataBundle.getString("text"));
                note.setNoteImage(dataBundle.getString("uri"));
                note.setNoteTime(dataBundle.getString("time"));
                note.setNoteReminderTime(dataBundle.getString("reminderTime"));
                note.setNoteReminderDate(dataBundle.getString("reminderDate"));
                note.setNoteReminderStatus(dataBundle.getBoolean("reminderStatus"));
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
    public void addNoteClicked() {
        getNavigator().addNoteClicked();
    }
}
