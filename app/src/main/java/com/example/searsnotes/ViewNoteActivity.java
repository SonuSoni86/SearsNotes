package com.example.searsnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.example.searsnotes.Model.NotesVo;
import com.example.searsnotes.Utilities.ImportantMethods;
import com.example.searsnotes.ViewModels.ViewNoteActivityViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ViewNoteActivity extends AppCompatActivity {

    private TextView noteTitle, noteText;
    private ImageView noteImage;
    private ViewNoteActivityViewModel viewModel;
    private int noteID;
    private LiveData<NotesVo> note;
    private NotesVo noteObject;
    private MainActivity mainActivityInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        noteImage = findViewById(R.id.note_image);
        noteID = getIntent().getIntExtra("id", 0);
        if (noteID == 0 ) {
            Toast.makeText(this, "Data Not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        viewModel = ViewModelProviders.of(this).get(ViewNoteActivityViewModel.class);
        note = viewModel.getNote(noteID);
        if(note==null){
            return;}
        note.observe(this, new Observer<NotesVo>() {
            @Override
            public void onChanged(NotesVo notesVo) {
                noteObject=notesVo;
                noteTitle.setText(notesVo.getNoteTitle());
                noteText.setText(notesVo.getNoteText());
                if (!notesVo.getNoteImage().equals("default")) {
                    Picasso.with(ViewNoteActivity.this).load(Uri.parse(notesVo.getNoteImage())).into(noteImage);
                    //holder.noteImage.setImageURI(Uri.parse(note.getNoteImage()));
                }
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
                NotesVo note = new NotesVo();
                note.setNoteTitle(dataBundle.getString("title"));
                note.setNoteText(dataBundle.getString("text"));
                note.setNoteImage(dataBundle.getString("uri"));
                note.setNoteTime(dataBundle.getString("time"));
                viewModel.updateNote(note);
                Toast.makeText(getApplicationContext(), "Edited Note saved", Toast.LENGTH_LONG).show();
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
                viewModel.getNote(noteID).removeObservers(ViewNoteActivity.this);
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
