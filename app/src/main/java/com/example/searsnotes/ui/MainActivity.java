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
import android.util.Log;

import com.example.searsnotes.Constants.IntentRequestCodes;
import com.example.searsnotes.R;
import com.example.searsnotes.databinding.ActivityMainBinding;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.view.NoteListAdapter;
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
    private List<NotesVo> tempList = new ArrayList<>();
    private int intial, last;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.notesView.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.notesView.setHasFixedSize(true);
        mainActivityViewModel = ViewModelProviders.of(this, providerFactory).get(MainActivityViewModel.class);
        adapter = new NoteListAdapter(this);
        mainBinding.notesView.setAdapter(adapter);
        mainActivityViewModel.getListOfNotes().observe(this, new Observer<List<NotesVo>>() {
            @Override
            public void onChanged(List<NotesVo> notesVos) {
                adapter.setNotelist(notesVos);
                upDatedList = notesVos;
            }
        });
        mainActivityViewModel.setNavigator(this);
        mainBinding.setViewModel(mainActivityViewModel);
        mainActivityViewModel.hideFab(mainBinding.notesView, mainBinding.addNoteBtn);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                NotesVo note1 = upDatedList.get(dragged.getAdapterPosition()), note2= upDatedList.get(target.getAdapterPosition());
                int id1 = note1.getNoteID(),id2= note2.getNoteID();
                intial = dragged.getAdapterPosition();
                last = target.getAdapterPosition();
                note1.setNoteID(id2);
                note2.setNoteID(id1);
                if(!tempList.contains(note1))tempList.add(note1);
                if(!tempList.contains(note2))tempList.add(note2);
                Collections.swap(upDatedList, dragged.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(dragged.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                Log.d("Test"," moved from "+ intial+" to "+last);
               if(!tempList.isEmpty())mainActivityViewModel.updateMultipleNotes(upDatedList);

            }
        });
        itemTouchHelper.attachToRecyclerView(mainBinding.notesView);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainActivityViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void addNoteClicked() {
        Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivityForResult(addNoteIntent, IntentRequestCodes.NEW_NOTE_ACTIVITY_REQUEST);
    }

}
