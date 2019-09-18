package com.example.searsnotes.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.searsnotes.R;
import com.example.searsnotes.ui.ViewNoteActivity;

public class WidgetProvider extends AppWidgetProvider {

    public static final String ACTION_VIEW = "actionViewNote";
    public static final String EXTRA_ITEM_POSITION = "extraItemPosition";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appWidgetId: appWidgetIds){
            Intent serviceIntent= new Intent(context,WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent clickIntent = new Intent(context,WidgetProvider.class);
            clickIntent.setAction(ACTION_VIEW);
            PendingIntent pendingClickIntent= PendingIntent.getBroadcast(context,0,clickIntent,0);

            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            view.setRemoteAdapter(R.id.listView,serviceIntent);
            view.setEmptyView(R.id.listView,R.id.widget_empty_view);
            view.setPendingIntentTemplate(R.id.listView,pendingClickIntent);
            appWidgetManager.updateAppWidget(appWidgetId,view);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION_VIEW.equals(intent.getAction())){
            int noteID = intent.getIntExtra(EXTRA_ITEM_POSITION,0);
            Intent intentView = new Intent(context.getApplicationContext(), ViewNoteActivity.class);
            intentView.putExtra("id", noteID);
            intentView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            (context.getApplicationContext()).startActivity(intentView);
        }

        super.onReceive(context, intent);
    }
}
