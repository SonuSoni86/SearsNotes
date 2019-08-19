package com.example.searsnotes.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.R;
import com.example.searsnotes.databinding.ActivityMainBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.View.NoteListAdapter;
import com.example.searsnotes.viewModels.MainActivityViewModel;
import com.example.searsnotes.navigators.MainActivityNavigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {

    private MainActivityViewModel mainActivityViewModel;
    private NoteListAdapter adapter;
    private List<NotesVo> upDatedList = new ArrayList<>();

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
                upDatedList = notesVos;
            }
        });
        mainActivityViewModel.setNavigator(this);
        mainBinding.setViewModel(mainActivityViewModel);
        mainActivityViewModel.hideFab(mainBinding.notesView,mainBinding.addNoteBtn);

        ItemTouchHelper itemTouchHelper  = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
              NotesVo note1 = upDatedList.get(dragged.getAdapterPosition()),note2 = upDatedList.get(target.getAdapterPosition());
                Collections.swap(upDatedList,dragged.getAdapterPosition(),target.getAdapterPosition());
                adapter.notifyItemMoved(dragged.getAdapterPosition(),target.getAdapterPosition());
                int tempId = note1.getNoteID();
                note1.setNoteID(note2.getNoteID());
                note2.setNoteID(tempId);
                mainActivityViewModel.updateNote(note1);
                mainActivityViewModel.updateNote(note2);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(mainBinding.notesView);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainActivityViewModel.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void addNoteClicked() {
        Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivityForResult(addNoteIntent, IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST);
    }




}
