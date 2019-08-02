package com.example.searsnotes.dependencyInjection;


import com.example.searsnotes.AddNoteActivity;
import com.example.searsnotes.EditNoteActivity;
import com.example.searsnotes.MainActivity;
import com.example.searsnotes.ViewNoteActivity;

import dagger.Module;


@Module
public abstract class ActivityBuilderModule{

     abstract MainActivity contributeMainActivity();
     abstract ViewNoteActivity viewNoteActivity();
     abstract EditNoteActivity editNoteActivity();
     abstract AddNoteActivity AddNoteActivity();

}
