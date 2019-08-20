package com.example.searsnotes.viewModels;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.dao.NotesDao;
import com.example.searsnotes.dao.NotesDatabase;
import com.example.searsnotes.utilities.ImportantMethods;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.navigators.EditNoteActivityNavigator;


import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EditNoteActivityViewModel extends BaseViewModel<EditNoteActivityNavigator> {

    private final String TAG = this.getClass().getSimpleName();
    private NotesDao notesDao;


    public EditNoteActivityViewModel(@NonNull Application application) {
        super(application);
        NotesDatabase notesDatabaseInstance = NotesDatabase.getNotesDatabaseInstance(application);
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

    public Bundle makeBundle(int noteID, EditText noteTitle, EditText noteText, String imageUri, TextView reminderTime, TextView reminderDate, CheckBox reminderCheckBox) {
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
        noteDataBundle.putString("reminderTime", reminderTime.getText().toString());
        noteDataBundle.putString("reminderDate",reminderDate.getText().toString());
        noteDataBundle.putBoolean("reminderStatus", reminderCheckBox.isChecked());

        return  noteDataBundle;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String onActivityResult(int requestCode, int resultCode, Intent data, String oldImageUri) {
        String imageUri = oldImageUri;
        switch (requestCode){
            case IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
                {
                    Bitmap tempBmp = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    assert tempBmp != null;
                    imageUri= ImportantMethods.getImageUri(getApplication().getApplicationContext(),tempBmp).toString();
                }
                break;

            case IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
                {
                    Uri tempUri = data.getData();
                    assert tempUri != null;
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

    public void saveBtnClicked() { getNavigator().saveButtonClicked(); }


    public void discardBtnClicked(){getNavigator().discardButtonClicked(); }

    public void picImageClicked(){
        getNavigator().requestMultiplePermissions(IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST);
        openViewModelGalary();
    }

    public void captureImageClicked(){
        getNavigator().requestMultiplePermissions(IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST);
        openViewModelCamera();
    }

    public void chooseTimeClicked(){
        getNavigator().setTime();
    }
    public void chooseDateClicked(){
        getNavigator().setDate();
    }

}
