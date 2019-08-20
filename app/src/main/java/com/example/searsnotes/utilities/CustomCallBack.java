package com.example.searsnotes.utilities;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import android.view.ActionMode;

public class CustomCallBack implements ActionMode.Callback {

   private EditText editText;
   private TextView textView;
   private Context context;
   private int choice;

    public CustomCallBack(EditText text, Context context) { this.editText = text; this.context= context; choice=1;}
    public CustomCallBack(TextView text, Context context) { this.textView = text; this.context =context; choice=2;}

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        menu.add(0,1,2,"Show Meaning");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getTitle().equals("Show Meaning")){
            String textSelected="";

           switch (choice){
               case 1:
                   textSelected= editText.getText().toString().trim().substring(editText.getSelectionStart(),editText.getSelectionEnd());
                   break;
               case 2:
                   textSelected= textView.getText().toString().trim().substring(textView.getSelectionStart(),textView.getSelectionEnd());
                   break;
           }
            ImportantMethods.getWordMeaning(textSelected,context);
            mode.finish();
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }


}
