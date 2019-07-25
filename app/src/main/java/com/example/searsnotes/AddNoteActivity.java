package com.example.searsnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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

    private String imageUri;
    private TextView picImage,captureImage;
    private ActivityAddNoteBinding addNoteBinding;
    private NotesVo note= new NotesVo();
    private AddNoteActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNoteBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_note);
        imageUri = null;
        picImage  = findViewById(R.id.pic_note_image);
        captureImage  = findViewById(R.id.capture_note_image);
        addNoteBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(addNoteBinding.noteTitle,this));
        addNoteBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(addNoteBinding.noteText,this));
        addNoteBinding.setNoteObject(note);
        viewModel = ViewModelProviders.of(this).get(AddNoteActivityViewModel.class);
    }


    public void saveBtnClicked(View view) {
        Bundle noteDataBundle = viewModel.makeBundle(addNoteBinding.noteTitle,addNoteBinding.noteText,imageUri,ImportantMethods.gettime());
        Intent resultIntent = new Intent().putExtra("note_data",noteDataBundle);
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

        imageUri = viewModel.onActivityResult(requestCode,resultCode,data);
        if(imageUri!=null){addNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));}

    }


}
