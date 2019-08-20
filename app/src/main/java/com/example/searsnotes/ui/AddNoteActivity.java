package com.example.searsnotes.ui;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.R;
import com.example.searsnotes.reminder.ReminderBroadcastReceiver;
import com.example.searsnotes.utilities.CustomCallBack;
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

import java.util.Calendar;
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
    private String reminderTim;
    private String reminderDat;
    private boolean isReminderOn = false;
    private Calendar calendar;
    private Bundle reminderBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);
        imageUri = null;
        reminderBundle = new Bundle();
        calendar = Calendar.getInstance();
        addNoteBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(addNoteBinding.noteTitle, this));
        addNoteBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(addNoteBinding.noteText, this));
        addNoteBinding.setNoteObject(note);
        viewModel = ViewModelProviders.of(this, providerFactory).get(AddNoteActivityViewModel.class);
        viewModel.setNavigator(this);
        addNoteBinding.setViewModel(viewModel);
        addNoteBinding.reminderTime.setText(""+calendar.get(Calendar.HOUR)+":"+ calendar.get(Calendar.MINUTE)+" "+(calendar.get(Calendar.AM_PM)==0 ? "AM":"PM"));
        addNoteBinding.reminderDate.setText(""+ calendar.get(Calendar.DATE)+"-"+ calendar.get(Calendar.MONTH) +"-"+ calendar.get(Calendar.YEAR));
        reminderBundle.putInt("hour",calendar.get(Calendar.HOUR));
        reminderBundle.putInt("min",calendar.get(Calendar.MINUTE));
        reminderBundle.putInt("day",calendar.get(Calendar.DAY_OF_MONTH));
        reminderBundle.putInt("month",calendar.get(Calendar.MONTH));
        reminderBundle.putInt("year",calendar.get(Calendar.YEAR));
        reminderBundle.putString("AM_PM",(calendar.get(Calendar.AM_PM)==0 ? "AM":"PM"));
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
    public void setTime() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if(hour>12)hour=hour-12;
                reminderBundle.putInt("hour",hour);
                reminderBundle.putInt("min",minute);
                reminderBundle.putString("AM_PM",(timePicker.getCurrentHour() >12?"PM":"AM"));
                reminderTim =""+hour+":"+minute+" "+(timePicker.getCurrentHour() >12?"PM":"AM");
                addNoteBinding.reminderTime.setText(reminderTim);
            }
        },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
        dialog.show();
    }


    @Override
    public void setDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                reminderBundle.putInt("day",date);
                reminderBundle.putInt("month",month);
                reminderBundle.putInt("year",year);
                reminderDat = ""+date+"-"+month+"-"+year;
                addNoteBinding.reminderDate.setText(""+date+"-"+month+"-"+year);
            }
        },calendar.get(Calendar.DATE),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE,0);
        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void setNoteImage(String uri){
        addNoteBinding.noteImage.setImageURI(Uri.parse(imageUri));
    }

    @Override
    public void saveButtonClicked() {
        int reminderId= viewModel.setReminder(addNoteBinding.remindercheckbox,reminderBundle);
        Toast.makeText(getApplicationContext(),""+reminderId,Toast.LENGTH_LONG).show();
        Bundle noteDataBundle = viewModel.makeBundle(addNoteBinding.noteTitle, addNoteBinding.noteText, imageUri,addNoteBinding.reminderTime,addNoteBinding.reminderDate,addNoteBinding.remindercheckbox);
        setResult(RESULT_OK,  new Intent().putExtra("note_data", noteDataBundle));
        finish();
    }

    @Override
    public void discardButtonClicked() {finish(); }

    @Override
    public void setReminder(Calendar calendar_alarm, int reminderId) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AddNoteActivity.this, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNoteActivity.this,reminderId,intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar_alarm.getTimeInMillis(),pendingIntent);
    }

}
