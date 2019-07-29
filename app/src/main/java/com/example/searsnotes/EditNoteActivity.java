package com.example.searsnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Utilities.CustomCallBack;
import com.example.searsnotes.databinding.ActivityEditNoteBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.ViewModels.EditNoteActivityViewModel;

import java.io.IOException;

import javax.inject.Inject;

public class EditNoteActivity extends AppCompatActivity {
    private String imageUri;
    private TextView picImage,captureImage;
    private int noteID;
    @Inject
    ViewModelProviderFactory providerFactory;
    private EditNoteActivityViewModel viewModel;
    private LiveData<NotesVo> note;
    private ActivityEditNoteBinding editNoteBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editNoteBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_note);
        picImage  = findViewById(R.id.pic_note_image);
        captureImage  = findViewById(R.id.capture_note_image);
        editNoteBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(editNoteBinding.noteTitle,this));
        editNoteBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(editNoteBinding.noteText,this));

        noteID = getIntent().getIntExtra("id",-1);
        viewModel = ViewModelProviders.of(this,providerFactory).get(EditNoteActivityViewModel.class);
        note =viewModel.getNote(noteID);
        note.observe(this, new Observer<NotesVo>() {
            @Override
            public void onChanged(NotesVo notesVo) {
                editNoteBinding.setNoteObject(notesVo);
                imageUri = notesVo.getNoteImage();
            }
        });
    }

    public void saveBtnClicked(View view) {
        Bundle noteDataBundle = viewModel.makeBundle(noteID,editNoteBinding.noteTitle,editNoteBinding.noteText,imageUri,ImportantMethods.gettime());
        Intent resultIntent = new Intent().putExtra("note_data",noteDataBundle);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

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
    public void discardBtnClicked(View view) { finish();}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = viewModel.onActivityResult(requestCode,resultCode,data,imageUri);
        editNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));
    }



}
