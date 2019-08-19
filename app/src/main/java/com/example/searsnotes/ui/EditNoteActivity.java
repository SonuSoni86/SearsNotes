package com.example.searsnotes.ui;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.R;
import com.example.searsnotes.Utilities.CustomCallBack;
import com.example.searsnotes.databinding.ActivityEditNoteBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.viewModels.EditNoteActivityViewModel;
import com.example.searsnotes.navigators.EditNoteActivityNavigator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class EditNoteActivity extends AppCompatActivity implements EditNoteActivityNavigator {
    private String imageUri;
    private int noteID;
    @Inject
    ViewModelProviderFactory providerFactory;
    private EditNoteActivityViewModel viewModel;
    private ActivityEditNoteBinding editNoteBinding;
    private String reminderTim;
    private String reminderDat;
    private boolean isReminderOn = false;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note);
        editNoteBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(editNoteBinding.noteTitle,this));
        editNoteBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(editNoteBinding.noteText,this));
        calendar = Calendar.getInstance();
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
        editNoteBinding.setViewModel(viewModel);
      //  editNoteBinding.reminderTime.setText(""+calendar.get(Calendar.HOUR)+":"+ calendar.get(Calendar.MINUTE)+" "+(calendar.get(Calendar.AM_PM)==0 ? "AM":"PM"));
      //  editNoteBinding.reminderDate.setText(""+ calendar.get(Calendar.DATE)+"-"+ calendar.get(Calendar.MONTH) +"-"+ calendar.get(Calendar.YEAR));

    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = viewModel.onActivityResult(requestCode,resultCode,data,imageUri);
        editNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));
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
                            if(requestCode==IntentRequestCodes.PICK_PICTURE_ACTIVITY_REQUEST) viewModel.openViewModelGalary();
                            if(requestCode==IntentRequestCodes.CAPTURE_PICTURE_ACTIVITY_REQUEST) viewModel.openViewModelCamera();
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

    @Override
    public void saveButtonClicked() {
        Bundle noteDataBundle = viewModel.makeBundle(noteID,editNoteBinding.noteTitle,editNoteBinding.noteText,imageUri,editNoteBinding.reminderTime,editNoteBinding.reminderDate,editNoteBinding.remindercheckbox);
        setResult(RESULT_OK,new Intent().putExtra("note_data",noteDataBundle));
        finish();
    }

    @Override
    public void discardButtonClicked() {finish(); }

    @Override
    public void setTime() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                reminderTim = ""+hour+":"+minute+" "+(timePicker.getCurrentHour().intValue()>12?"PM":"AM");
                editNoteBinding.reminderTime.setText(reminderTim);

            }
        },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
        dialog.show();
    }
    @Override
    public void setDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int date, int month, int year) {
                reminderDat = ""+date+"-"+month+"-"+year;
                editNoteBinding.reminderDate.setText(""+date+"-"+month+"-"+year);
            }
        },calendar.get(Calendar.DATE),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE,0);
        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }
}
