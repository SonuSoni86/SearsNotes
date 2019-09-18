package com.example.searsnotes.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.searsnotes.dao.NotesDao;
import com.example.searsnotes.dao.NotesDatabase;
import com.example.searsnotes.R;
import com.example.searsnotes.model.NotesVo;

import java.util.ArrayList;
import java.util.List;

import static com.example.searsnotes.widgets.WidgetProvider.EXTRA_ITEM_POSITION;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(),intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory{

        private Context context;
        private NotesDao notesDao;
        private List<NotesVo> notesVoList = new ArrayList<>();

        WidgetItemFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            NotesDatabase notesDatabaseInstance = NotesDatabase.getNotesDatabaseInstance(getApplicationContext());
            notesDao = notesDatabaseInstance.notesDao();
        }

        @Override
        public void onDataSetChanged() {
         notesVoList = notesDao.getListOfWidgetNotes();
        }

        @Override
        public void onDestroy() {
             notesVoList.clear();
        }

        @Override
        public int getCount() {
            if(notesVoList==null) return 0;
            else return notesVoList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_row_item);
           NotesVo note = notesVoList.get(i);
            views.setTextViewText(R.id.note_title,note.getNoteTitle());
           views.setTextViewText(R.id.note_time,note.getNoteTime());

           Intent fillIntent = new Intent();
           fillIntent.putExtra(EXTRA_ITEM_POSITION,note.getNoteID());
           views.setOnClickFillInIntent(R.id.widgetRowItem,fillIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
