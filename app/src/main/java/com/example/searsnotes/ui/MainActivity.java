package com.example.searsnotes.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.R;
import com.example.searsnotes.databinding.ActivityMainBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.View.NoteListAdapter;
import com.example.searsnotes.ViewModels.MainActivityViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private NoteListAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.notesView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteListAdapter(this);
        mainBinding.notesView.setAdapter(adapter);
        mainBinding.notesView.setHasFixedSize(true);
        mainActivityViewModel = ViewModelProviders.of(this,providerFactory).get(MainActivityViewModel.class);
        mainActivityViewModel.getListOfNotes().observe(this, new Observer<List<NotesVo>>() {
            @Override
            public void onChanged(List<NotesVo> notesVos) {
                adapter.setNotelist(notesVos);
            }
        });

    }

    public void addNoteClicked(View view) {
        Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivityForResult(addNoteIntent, IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainActivityViewModel.onActivityResult(requestCode,resultCode,data);
    }
}
