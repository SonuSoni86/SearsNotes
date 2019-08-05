package com.example.searsnotes.dependencyInjection;


import com.example.searsnotes.ui.AddNoteActivity;
import com.example.searsnotes.ui.EditNoteActivity;
import com.example.searsnotes.ui.MainActivity;
import com.example.searsnotes.ui.ViewNoteActivity;

import dagger.Module;


@Module
public abstract class ActivityBuilderModule{

     abstract MainActivity contributeMainActivity();
     abstract ViewNoteActivity viewNoteActivity();
     abstract EditNoteActivity editNoteActivity();
     abstract AddNoteActivity AddNoteActivity();

}
