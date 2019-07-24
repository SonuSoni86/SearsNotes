package com.example.searsnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.ViewModels.ViewNoteActivityViewModel;
import com.example.searsnotes.databinding.ActivityViewNoteBinding;
import com.squareup.picasso.Picasso;

public class ViewNoteActivity extends AppCompatActivity {

    private TextView noteTitle, noteText;
    private ImageView noteImage;
    private ViewNoteActivityViewModel viewModel;
    private int noteID;
    private LiveData<NotesVo> note;
    private NotesVo noteObject;
    private MainActivity mainActivityInstance;
    private ActivityViewNoteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_view_note);
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        noteImage = findViewById(R.id.note_image);
        noteID = getIntent().getIntExtra("id", -1);
        if (noteID == -1) {
            Toast.makeText(this, "Data Not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        viewModel = ViewModelProviders.of(this).get(ViewNoteActivityViewModel.class);
        note = viewModel.getNote(noteID);
        note.observe(this, new Observer<NotesVo>() {
            @Override
            public void onChanged(NotesVo notesVo) {
                noteObject = notesVo;
                mBinding.setNoteObject(noteObject);
            }
        });

        noteTitle.setCustomSelectionActionModeCallback(new callback(noteTitle));
        noteText.setCustomSelectionActionModeCallback(new callback(noteText));


    }


    public void discardBtnClicked(View view) {
        finish();
    }

    public void editBtnClicked(View view) {
        Intent intent = new Intent(ViewNoteActivity.this, EditNoteActivity.class);
        intent.putExtra("id", noteID);
        startActivityForResult(intent, IntentRequestCodes.UPDATE_NOTE_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodes.UPDATE_NOTE_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle dataBundle = data.getBundleExtra("note_data");
                NotesVo notesVo = new NotesVo();
                notesVo.setNoteID(dataBundle.getInt("id"));
                notesVo.setNoteTitle(dataBundle.getString("title"));
                notesVo.setNoteText(dataBundle.getString("text"));
                notesVo.setNoteImage(dataBundle.getString("uri"));
                notesVo.setNoteTime(dataBundle.getString("time"));
                viewModel.updateNote(notesVo);
                Toast.makeText(getApplicationContext(), notesVo.getNoteTitle(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Edited Note Not saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void deleteBtnClicked(View view) {

        final AlertDialog.Builder confirmation = new AlertDialog.Builder(this);
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

    private class callback implements ActionMode.Callback {
        TextView text;

        public callback(TextView text) {
            this.text = text;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            menu.add(0, 1, 2, "Show Meaning");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getTitle().equals("Show Meaning")) {
                String textSelected = text.getText().toString().trim().substring(text.getSelectionStart(), text.getSelectionEnd());
                ImportantMethods.getWordMeaning(textSelected, ViewNoteActivity.this);
                actionMode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}
