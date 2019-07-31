package com.example.searsnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.Utilities.CustomCallBack;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.ViewModels.ViewNoteActivityViewModel;
import com.example.searsnotes.databinding.ActivityViewNoteBinding;

import javax.inject.Inject;

public class ViewNoteActivity extends AppCompatActivity {

    private int noteID;
    private LiveData<NotesVo> note;
    private NotesVo noteObject;
    private ActivityViewNoteBinding mBinding;
    @Inject
    ViewModelProviderFactory providerFactory;
    private ViewNoteActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_note);
        noteID = getIntent().getIntExtra("id", -1);
        viewModel = ViewModelProviders.of(this,providerFactory).get(ViewNoteActivityViewModel.class);
        note = viewModel.getNote(noteID);
        note.observe(this, new Observer<NotesVo>() {
            @Override
            public void onChanged(NotesVo notesVo) {
                noteObject = notesVo;
                mBinding.setNoteObject(noteObject);
            }
        });
        mBinding.noteTitle.setCustomSelectionActionModeCallback(new CustomCallBack(mBinding.noteTitle, this));
        mBinding.noteText.setCustomSelectionActionModeCallback(new CustomCallBack(mBinding.noteText, this));

    }

    public void discardBtnClicked(View view) {
        finish();
    }

    public void editBtnClicked(View view) {
        startActivityForResult(new Intent(ViewNoteActivity.this, EditNoteActivity.class).putExtra("id",noteID), IntentRequestCodes.UPDATE_NOTE_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteBtnClicked(View view) {

        AlertDialog.Builder confirmation = new AlertDialog.Builder(this);
        confirmation.setTitle("Confirm");
        confirmation.setMessage(" Do you really want to delete this note?");
        confirmation.setIcon(R.drawable.ic_delete_black_24dp);
        confirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                note.removeObservers(ViewNoteActivity.this);
                viewModel.deleteNote(noteObject);
                finish();
            }
        });
        confirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmation.show();
    }


}
