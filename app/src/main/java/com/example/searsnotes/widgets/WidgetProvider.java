package com.example.searsnotes.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.searsnotes.R;

public class WidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        int appWidgetId = appWidgetIds[0];
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        appWidgetManager.updateAppWidget(appWidgetId,view);
    }
}
