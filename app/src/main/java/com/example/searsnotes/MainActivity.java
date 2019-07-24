package com.example.searsnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.View.NoteListAdapter;
import com.example.searsnotes.ViewModels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private FloatingActionButton addNoteBtn;
    private RecyclerView viewOfNotes;
    private List<NotesVo> notesVoList = new ArrayList<>();
    private NoteListAdapter adapter;
    public int numberOfNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNoteBtn = findViewById(R.id.add_note_btn);
        viewOfNotes = findViewById(R.id.notes_view);
        viewOfNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteListAdapter(this);
        viewOfNotes.setAdapter(adapter);
        viewOfNotes.setHasFixedSize(true);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getListOfNotes().observe(this, new Observer<List<NotesVo>>() {
            @Override
            public void onChanged(List<NotesVo> notesVos) {
                if(notesVos.size()==0){
                    Toast.makeText(getApplicationContext(),"No Notes Available",Toast.LENGTH_LONG).show();
                }
                adapter.setNotelist(notesVos);
                numberOfNotes=notesVos.size();

            }
        });



    }

    public void addNoteClicked(View view) {
        Intent addNoteIntent = new Intent(MainActivity.this,AddNoteActivity.class);
        startActivityForResult(addNoteIntent, IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST){
            if(resultCode==RESULT_OK){
                Bundle dataBundle = data.getBundleExtra("note_data");
                NotesVo note = new NotesVo();
                note.setNoteTitle(dataBundle.getString("title"));
                note.setNoteText(dataBundle.getString("text"));
                note.setNoteImage(dataBundle.getString("uri"));
                note.setNoteTime(dataBundle.getString("time"));
                mainActivityViewModel.addNote(note);
                Toast.makeText(getApplicationContext(),"Note saved",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(),"Note Not saved",Toast.LENGTH_LONG).show();
            }
        }
    }
}
