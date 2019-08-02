package com.example.searsnotes;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Utilities.CustomCallBack;
import com.example.searsnotes.databinding.ActivityEditNoteBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.ViewModels.EditNoteActivityViewModel;
import com.example.searsnotes.navigators.EditNoteActivityNavigator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import javax.inject.Inject;

public class EditNoteActivity extends AppCompatActivity implements EditNoteActivityNavigator {
    private String imageUri;
    private int noteID;
    @Inject
    ViewModelProviderFactory providerFactory;
    private EditNoteActivityViewModel viewModel;
    private ActivityEditNoteBinding editNoteBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editNoteBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_note);
        editNoteBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(editNoteBinding.noteTitle,this));
        editNoteBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(editNoteBinding.noteText,this));

        noteID = getIntent().getIntExtra("id",-1);
        viewModel = ViewModelProviders.of(this,providerFactory).get(EditNoteActivityViewModel.class);
        LiveData<NotesVo> note = viewModel.getNote(noteID);
        note.observe(this, new Observer<NotesVo>() {
            @Override
            public void onChanged(NotesVo notesVo) {
                editNoteBinding.setNoteObject(notesVo);
                imageUri = notesVo.getNoteImage();
            }
        });
        viewModel.setNavigator(this);
    }

    public void saveBtnClicked(View view) {
        Bundle noteDataBundle = viewModel.makeBundle(noteID,editNoteBinding.noteTitle,editNoteBinding.noteText,imageUri,ImportantMethods.gettime());
        setResult(RESULT_OK,new Intent().putExtra("note_data",noteDataBundle));
        finish();
    }

    public void picImageClicked(View view) {
        requestMultiplePermissions();
        viewModel.openViewModelGalary();
    }

    public void captureImageClicked(View view) {
        requestMultiplePermissions();
      viewModel.openViewModelCamera();
    }

    public void discardBtnClicked(View view) { finish();}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = viewModel.onActivityResult(requestCode,resultCode,data,imageUri);
        editNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));
    }



    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            return;
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage(" Some Permissions are permanently denied. you need to go to setting to allow the permissions.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void openGalary() {
        startActivityForResult(Intent.createChooser( new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"), "Pic an Image"), IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST);
    }

    @Override
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST);
        }
    }


    @Override
    public void setNoteImage(String uri){
        editNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));
    }

}
