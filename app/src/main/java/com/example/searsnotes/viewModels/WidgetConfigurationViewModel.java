package com.example.searsnotes.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.model.NotesVo;

import java.util.List;

import javax.inject.Inject;

public class WidgetConfigurationViewModel extends AndroidViewModel {
    private String TAG= this.getClass().getSimpleName();
    private NotesDao notesDao;
    private LiveData<List<NotesVo>> listOfNotes;

    @Inject
    WidgetConfigurationViewModel(@NonNull Application application) {
        super(application);

        NotesDatabase notesDatabaseInstace = NotesDatabase.getNotesDatabaseInstance(application);
        notesDao = notesDatabaseInstace.notesDao();
        listOfNotes = notesDao.getListOfNotes();
    }
    public LiveData<List<NotesVo>> getListOfNotes(){
        return listOfNotes;
    }
}
