package com.example.searsnotes.ViewModels;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Utilities.ImportantMethods;

import static android.app.Activity.RESULT_OK;

public class AddNoteActivityViewModel extends AndroidViewModel {

    public final String TAG = this.getClass().getSimpleName();

    public AddNoteActivityViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "Add note ACtivity destroyed ");
    }

    public Bundle makeBundle(EditText noteTitle, EditText noteText, String imageUri, String gettime) {
        Bundle noteDataBundle = new Bundle();
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

    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        String imageUri = null;
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
}
