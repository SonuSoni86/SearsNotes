package com.example.searsnotes.ui;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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
import com.example.searsnotes.R;
import com.example.searsnotes.Utilities.CustomCallBack;
import com.example.searsnotes.viewModels.AddNoteActivityViewModel;
import com.example.searsnotes.databinding.ActivityAddNoteBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.navigators.AddNoteActivityNavigator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import javax.inject.Inject;

public class AddNoteActivity extends AppCompatActivity implements AddNoteActivityNavigator {

    private String imageUri;
    private ActivityAddNoteBinding addNoteBinding;
    private NotesVo note = new NotesVo();
    @Inject
    ViewModelProviderFactory providerFactory;
    private AddNoteActivityViewModel viewModel;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);
        imageUri = null;
        addNoteBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(addNoteBinding.noteTitle, this));
        addNoteBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(addNoteBinding.noteText, this));
        addNoteBinding.setNoteObject(note);
        viewModel = ViewModelProviders.of(this, providerFactory).get(AddNoteActivityViewModel.class);
        viewModel.setNavigator(this);
        addNoteBinding.setViewModel(viewModel);
    }





    public void captureImageClicked(View view) {
        requestMultiplePermissions(IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST);
        viewModel.openViewModelCamera();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = viewModel.onActivityResult(requestCode, resultCode, data);
        viewModel.checkImageUri(imageUri);
    }


    @Override
    public void requestMultiplePermissions(final int requestCode) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            flag = true;
                            if(requestCode==IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST) viewModel.openViewModelGalary();
                            if(requestCode==IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST) viewModel.openViewModelCamera();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            flag = false;
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
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

    @Override
    public void openGalary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"), "Pic an Image"), IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST);
        }
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
        addNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));
    }

    @Override
    public void saveButtonClicked() {
        Bundle noteDataBundle = viewModel.makeBundle(addNoteBinding.noteTitle, addNoteBinding.noteText, imageUri);
        setResult(RESULT_OK,  new Intent().putExtra("note_data", noteDataBundle));
        finish();
    }

    @Override
    public void discardButtonClicked() {finish(); }
}