package com.example.searsnotes.widgets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;

import com.example.searsnotes.R;
import com.example.searsnotes.dependencyInjection.ViewModelProviderFactory;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.viewModels.MainActivityViewModel;
import com.example.searsnotes.viewModels.WidgetConfigurationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WidgetConfigurationActivity extends AppCompatActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private WidgetConfigurationViewModel widgetConfigurationViewModel;
    private List<String> upDatedList = new ArrayList<>();

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_BUTTON_TEXT = "keyButtonText";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        widgetConfigurationViewModel = ViewModelProviders.of(this,providerFactory).get(WidgetConfigurationViewModel.class);
        widgetConfigurationViewModel.getListOfNotes().observe(this, new Observer<List<NotesVo>>() {
            @Override
            public void onChanged(List<NotesVo> notesVos) {
                for(int i=0;i<5;i++){
                    upDatedList.add(notesVos.get(i).getNoteTitle());
                }
            }
        });


    }
}
