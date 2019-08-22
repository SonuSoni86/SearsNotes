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

import com.example.searsnotes.Constants.IntentRequestCodes;

import com.example.searsnotes.utilities.ImportantMethods;
import com.example.searsnotes.navigators.AddNoteActivityNavigator;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddNoteActivityViewModel extends BaseViewModel<AddNoteActivityNavigator> {

    private final String TAG = this.getClass().getSimpleName();

    public AddNoteActivityViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "Add note ACtivity destroyed ");
    }

    public Bundle makeBundle(EditText noteTitle, EditText noteText, String imageUri, TextView reminderTime, TextView reminderDate, CheckBox reminderCheckBox,String reminderId) {
        Bundle noteDataBundle = new Bundle();
        noteDataBundle.putString("title", noteTitle.getText().toString().trim());
        noteDataBundle.putString("text", noteText.getText().toString().trim());
        if (imageUri == null) {
            noteDataBundle.putString("uri", "default");
        } else {
            noteDataBundle.putString("uri", imageUri);
        }
        noteDataBundle.putString("time", ImportantMethods.gettime());
        noteDataBundle.putString("reminderTime", reminderTime.getText().toString());
        noteDataBundle.putString("reminderDate", reminderDate.getText().toString());
        noteDataBundle.putBoolean("reminderStatus", reminderCheckBox.isChecked());
        noteDataBundle.putString("reminderId", reminderId);
        return noteDataBundle;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        String imageUri = null;
        switch (requestCode) {
            case IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap tempBmp = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    assert tempBmp != null;
                    imageUri = ImportantMethods.getImageUri(getApplication().getApplicationContext(), tempBmp).toString();
                }
                break;

            case IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST:
                if (resultCode == RESULT_OK) {
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
        if (ImportantMethods.hasAllPermissions(getApplication().getApplicationContext())) {
            getNavigator().openGalary();
        }
    }

    public void openViewModelCamera() {
        if (ImportantMethods.hasAllPermissions(getApplication().getApplicationContext())) {
            getNavigator().openCamera();
        }
    }

    public void checkImageUri(String imageUri) {
        if (imageUri != null) {
            getNavigator().setNoteImage(imageUri);
        }
    }

    public void saveBtnClicked() {
        getNavigator().saveButtonClicked();
    }


    public void discardBtnClicked() {
        getNavigator().discardButtonClicked();
    }

    public void picImageClicked() {
        getNavigator().requestMultiplePermissions(IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST);
        openViewModelGalary();
    }

    public void captureImageClicked() {
        getNavigator().requestMultiplePermissions(IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST);
        openViewModelCamera();
    }

    public void chooseTimeClicked() {
        getNavigator().setTime();
    }

    public void chooseDateClicked() {
        getNavigator().setDate();
    }

    public int setReminder(CheckBox remindercheckbox, Bundle reminderBundle) {
        int reminderId = -1;
        if (!remindercheckbox.isChecked()) return reminderId;
        else {
            reminderId = System.identityHashCode(reminderBundle);
            int hour = reminderBundle.getInt("hour");
            int minute = reminderBundle.getInt("min");
            if (reminderBundle.getString("AM_PM").equals("PM")) {
                hour = hour + 12;
            }
            int day = reminderBundle.getInt("day");
            int month = reminderBundle.getInt("month");
            int year = reminderBundle.getInt("year");
            Calendar calendar_alarm = Calendar.getInstance();
            calendar_alarm.set(year,month,day,hour,minute,0);
            getNavigator().setReminder(calendar_alarm,reminderId);
        }
        return reminderId;
    }
}
