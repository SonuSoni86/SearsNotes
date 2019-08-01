package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Dao.NotesDao;
import com.example.searsnotes.Dao.NotesDatabase;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.navigators.EditNoteActivityNavigator;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class EditNoteActivityViewModel extends BaseViewModel<EditNoteActivityNavigator> {

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

    public String onActivityResult(int requestCode, int resultCode, Intent data, String oldImageUri) {
        String imageUri = oldImageUri;
        switch (requestCode){
            case IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
                {
                    Bitmap tempBmp = (Bitmap)data.getExtras().get("data");
                    imageUri= ImportantMethods.getImageUri(getApplication().getApplicationContext(),tempBmp).toString();
                }
                break;

            case IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
                {
                    Uri tempUri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getApplication().getContentResolver().takePersistableUriPermission(tempUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    imageUri = tempUri.toString();
                }
                break;
        }
        return imageUri;
    }

    public void openViewModelGalary() {
        if(ImportantMethods.hasAllPermissions(getApplication().getApplicationContext())){getNavigator().openGalary();}
    }
    public void openViewModelCamera() {
        if(ImportantMethods.hasAllPermissions(getApplication().getApplicationContext())){getNavigator().openCamera();}
    }

    public void checkImageUri(String imageUri) {
        if(imageUri!=null){getNavigator().setNoteImage(imageUri);}
    }

}
