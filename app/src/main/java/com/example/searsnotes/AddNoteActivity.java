package com.example.searsnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.Utilities.CustomCallBack;
import com.example.searsnotes.databinding.ActivityAddNoteBinding;
import com.example.searsnotes.model.NotesVo;

import java.io.IOException;

public class AddNoteActivity extends AppCompatActivity {

    private EditText noteTitle,noteText;
    private String imageUri;
    private ImageView noteImage;
    private TextView picImage,captureImage;
    private ActivityAddNoteBinding addNoteBinding;
    private NotesVo note= new NotesVo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNoteBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_note);
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        noteImage = findViewById(R.id.note_image);
        imageUri = null;
        picImage  = findViewById(R.id.pic_note_image);
        captureImage  = findViewById(R.id.capture_note_image);
        noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(noteTitle,this));
        noteText.setCustomSelectionActionModeCallback(new CustomCallBack(noteText,this));
        addNoteBinding.setNoteObject(note);
    }


    public void saveBtnClicked(View view) {
        Bundle noteDataBundle = new Bundle();
        noteDataBundle.putString("title",noteTitle.getText().toString().trim());
        noteDataBundle.putString("text",noteText.getText().toString().trim());
       if(imageUri==null){
           noteDataBundle.putString("uri","default");
       }
       else {
           noteDataBundle.putString("uri",imageUri);
       }
       noteDataBundle.putString("time",ImportantMethods.gettime());
       Intent resultIntent = new Intent();
       resultIntent.putExtra("note_data",noteDataBundle);
       setResult(RESULT_OK,resultIntent);
       finish();
    }


    public void discardBtnClicked(View view) { finish();}

    public void picImageClicked(View view) {
        StartActivity startActivity = new StartActivity();
        boolean flag = startActivity.checkRuntimePermission(this);
        if(flag){
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"Pic an Image"), IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST);
        }
    }

    public void captureImageClicked(View view) {
        StartActivity startActivity = new StartActivity();
       boolean flag = startActivity.checkRuntimePermission(this);
       if(flag){
           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           if(intent.resolveActivity(getPackageManager())!=null){
               startActivityForResult(intent, IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST);
           }
       }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
                {Bitmap tempBmp = (Bitmap)data.getExtras().get("data");
                    noteImage.setImageBitmap(tempBmp);
                    imageUri= ImportantMethods.getImageUri(AddNoteActivity.this,tempBmp).toString();
                    picImage.setVisibility(View.GONE);
                    captureImage.setVisibility(View.GONE);
                }
                break;

            case IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
            {
                Uri tempUri = data.getData();
                Bitmap tempBmp=null;
                    try {
                        tempBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),tempUri);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    noteImage.setImageBitmap(tempBmp);
                picImage.setVisibility(View.GONE);
                captureImage.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    this.getContentResolver().takePersistableUriPermission(tempUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                imageUri = tempUri.toString();
            }
            break;
        }
    }


}
