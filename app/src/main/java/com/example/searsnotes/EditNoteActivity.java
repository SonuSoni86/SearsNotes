package com.example.searsnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.ViewModels.EditNoteActivityViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {

    private EditText noteTitle,noteText;
    private String imageUri,noteTime;
    private ImageView noteImage;
    private TextView picImage,captureImage;
    private Boolean flag=false;
    private int noteID;
    private EditNoteActivityViewModel viewModel;
    private LiveData<NotesVo> note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        noteImage = findViewById(R.id.note_image);
        imageUri = null;
        picImage  = findViewById(R.id.pic_note_image);
        captureImage  = findViewById(R.id.capture_note_image);
        noteTitle.setCustomSelectionActionModeCallback(new callback(noteTitle));
        noteText.setCustomSelectionActionModeCallback(new callback(noteText));

        noteID = getIntent().getIntExtra("id",0);
        if(noteID==0){
            Toast.makeText(this,"Data Not found",Toast.LENGTH_SHORT).show();
            finish();
        }
        viewModel = ViewModelProviders.of(this).get(EditNoteActivityViewModel.class);
        note =viewModel.getNote(noteID);
        note.observe(this, new Observer<NotesVo>() {
            @Override
            public void onChanged(NotesVo notesVo) {
                noteTitle.setText(notesVo.getNoteTitle());
                noteText.setText(notesVo.getNoteText());
                if(!notesVo.getNoteImage().equals("default")){
                    Picasso.with(EditNoteActivity.this).load(Uri.parse(notesVo.getNoteImage())).into(noteImage);
                    imageUri=notesVo.getNoteImage();
                    //holder.noteImage.setImageURI(Uri.parse(note.getNoteImage()));
                }
            }
        });
    }

    public void saveBtnClicked(View view) {
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
        Intent resultIntent = new Intent();
        resultIntent.putExtra("note_data",noteDataBundle);
        setResult(RESULT_OK,resultIntent);
        finish();
    }
    public void picImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Pic an Image"), IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST);
    }

    public void captureImageClicked(View view) {
        checkRuntimePermission();
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

        switch (requestCode){
            case IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST:
                if(resultCode==RESULT_OK)
                {
                    Bitmap tempBmp = (Bitmap)data.getExtras().get("data");
                    noteImage.setImageBitmap(tempBmp);
                    imageUri= ImportantMethods.getImageUri(EditNoteActivity.this,tempBmp).toString();
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

    private void checkRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                flag=true;
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED
                   )
            {
                Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class callback implements ActionMode.Callback {
        EditText text;
        public callback(EditText text) { this.text = text;}

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            menu.add(0,1,2,"Show Meaning");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if(menuItem.getTitle().equals("Show Meaning")){
                String textSelected= text.getText().toString().trim().substring(text.getSelectionStart(),text.getSelectionEnd());
                ImportantMethods.getWordMeaning(textSelected,EditNoteActivity.this);
                actionMode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }

}
