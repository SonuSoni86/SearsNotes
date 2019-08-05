package com.example.searsnotes.dependencyInjection;

import androidx.lifecycle.ViewModel;

import com.example.searsnotes.viewModels.AddNoteActivityViewModel;
import com.example.searsnotes.viewModels.EditNoteActivityViewModel;
import com.example.searsnotes.viewModels.MainActivityViewModel;
import com.example.searsnotes.viewModels.ViewNoteActivityViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    public abstract ViewModel bindMainViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ViewNoteActivityViewModel.class)
    public abstract ViewModel bindViewNoteViewModel(ViewNoteActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditNoteActivityViewModel.class)
    public abstract ViewModel bindEditNoteViewModel(ViewNoteActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddNoteActivityViewModel.class)
    public abstract ViewModel bindAddNoteViewModel(AddNoteActivityViewModel viewModel);
}
