package com.example.searsnotes.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.searsnotes.R;

public class WidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appWidgetId: appWidgetIds){
            Intent serviceIntent= new Intent(context,WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            view.setRemoteAdapter(R.id.listView,serviceIntent);
            view.setEmptyView(R.id.listView,R.id.widget_empty_view);
            appWidgetManager.updateAppWidget(appWidgetId,view);
        }
    }
}
