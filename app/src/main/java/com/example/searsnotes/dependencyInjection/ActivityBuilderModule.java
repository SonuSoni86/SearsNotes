package com.example.searsnotes.dependencyInjection;

import com.example.searsnotes.MainActivity;

import dagger.Module;


@Module
public abstract class ActivityBuilderModule{

     abstract MainActivity contributeMainActivity();

}
