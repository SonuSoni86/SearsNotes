package com.example.searsnotes.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searsnotes.Model.NotesVo;
import com.example.searsnotes.R;
import com.example.searsnotes.ViewNoteActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private List<NotesVo> listOfNotes;
    private Context context;
    private LayoutInflater inflater;

    public NoteListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_of_notes,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       if(listOfNotes.size()!=0){
           final NotesVo note = listOfNotes.get(position);
           holder.noteTitle.setText(note.getNoteTitle());
           holder.noteTime.setText(note.getNoteTime());
           if(note.getNoteImage().equals("default")){
               holder.noteImage.setImageResource(R.drawable.note_thumbnail);
           }else{
               Picasso.with(context).load(Uri.parse(note.getNoteImage())).into(holder.noteImage);
               //holder.noteImage.setImageURI(Uri.parse(note.getNoteImage()));
           }

           holder.getView().setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(context, ViewNoteActivity.class);
                   intent.putExtra("id",note.getNoteID());
                   ((Activity)context).startActivity(intent);
               }
           });
       }


    }

    @Override
    public int getItemCount() {
        if(listOfNotes!=null){return listOfNotes.size();}
        else return 0;

    }

    public void setNotelist(List<NotesVo> notesVos) {
        listOfNotes = notesVos;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle;
        private TextView noteTime;
        private CircleImageView noteImage;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            noteTitle = (TextView)itemView.findViewById(R.id.note_title);
            noteTime = (TextView)itemView.findViewById(R.id.note_time);
            noteImage = (CircleImageView) itemView.findViewById(R.id.note_image);
        }

        public View getView(){return this.itemView;}
    }
}
