package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.Model.NotesVo;

public class ViewNoteActivityViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private NotesDao notesDao;
    private NotesDatabase notesDatabaseInstance;


    public ViewNoteActivityViewModel(@NonNull Application application) {
        super(application);
        notesDatabaseInstance = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstance.notesDao();
    }
    public LiveData<NotesVo> getNote(int noteId){
        return  notesDao.getNote(noteId);
    }
    public void updateNote(NotesVo note){new UpdateNoteAsyncTask(notesDao).execute(note);}
    public void deleteNote(NotesVo note){ new DeleteNoteAsyncTask(notesDao).execute(note); }




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
            notesDao.updateNote(notesVos[0].getNoteID(),notesVos[0].getNoteTitle(),notesVos[0].getNoteText(),notesVos[0].getNoteTime(),notesVos[0].getNoteImage());
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
