package com.example.searsnotes.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends AndroidViewModel {

    private WeakReference<N> mNavigator;
     BaseViewModel(@NonNull Application application) {
        super(application);
    }

     N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N mNavigator) {
        this.mNavigator = new WeakReference<>(mNavigator);
    }
}
