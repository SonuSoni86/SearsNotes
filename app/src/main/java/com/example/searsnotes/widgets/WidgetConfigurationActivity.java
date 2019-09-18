package com.example.searsnotes.widgets;

import androidx.appcompat.app.AppCompatActivity;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.searsnotes.R;

import static com.example.searsnotes.widgets.WidgetProvider.ACTION_VIEW;

public class WidgetConfigurationActivity extends AppCompatActivity {


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if(extras!=null){
            appWidgetId=extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        setResult(RESULT_CANCELED,resultValue);
        if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }
        setWidget();
    }

    private void setWidget() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        Intent serviceIntent= new Intent(this,WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent clickIntent = new Intent(this,WidgetProvider.class);
        clickIntent.setAction(ACTION_VIEW);
        PendingIntent pendingClickIntent= PendingIntent.getBroadcast(this,0,clickIntent,0);

        RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget_layout);
        views.setRemoteAdapter(R.id.listView,serviceIntent);
        views.setEmptyView(R.id.listView,R.id.widget_empty_view);
        views.setPendingIntentTemplate(R.id.listView,pendingClickIntent);

        manager.updateAppWidget(appWidgetId,views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK,resultValue);
        finish();
    }
}
