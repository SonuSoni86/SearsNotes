package com.example.searsnotes.ui;

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
import com.example.searsnotes.constants.IntentRequestCodes;
import com.example.searsnotes.R;
import com.example.searsnotes.utilities.CustomCallBack;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.navigators.ViewNoteActivityNavigator;
import com.example.searsnotes.viewModels.ViewNoteActivityViewModel;
import com.example.searsnotes.databinding.ActivityViewNoteBinding;

import javax.inject.Inject;

public class ViewNoteActivity extends AppCompatActivity implements ViewNoteActivityNavigator {

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
        viewModel.setNavigator(this);
        mBinding.setViewModel(viewModel);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void deleteBtnClicked() {
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

    @Override
    public void discardBtnClicked() {finish();}

    @Override
    public void editBtnClicked() {
        startActivityForResult(new Intent(ViewNoteActivity.this, EditNoteActivity.class).putExtra("id",noteID), IntentRequestCodes.UPDATE_NOTE_ACTIVITY_REQUEST);
    }
}
