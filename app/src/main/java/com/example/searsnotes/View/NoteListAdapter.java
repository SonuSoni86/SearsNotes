package com.example.searsnotes.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searsnotes.databinding.RowOfNotesBinding;
import com.example.searsnotes.model.NotesVo;
import com.example.searsnotes.R;
import com.example.searsnotes.ViewNoteActivity;
import com.squareup.picasso.Picasso;

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
        RowOfNotesBinding rowOfNotesBinding = (RowOfNotesBinding) DataBindingUtil.inflate(inflater, R.layout.row_of_notes, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowOfNotesBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listOfNotes.size() != 0) {
            final NotesVo note = listOfNotes.get(position);
            holder.rowOfNotesBinding.setNoteObject(note);
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewNoteActivity.class);
                    intent.putExtra("id", note.getNoteID());
                    ((Activity) context).startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listOfNotes != null) {
            return listOfNotes.size();
        } else return 0;

    }

    public void setNotelist(List<NotesVo> notesVos) {
        listOfNotes = notesVos;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowOfNotesBinding rowOfNotesBinding;


        public ViewHolder(@NonNull RowOfNotesBinding itemView) {
            super(itemView.getRoot());
            this.rowOfNotesBinding = itemView;
        }

        public View getView() {
            return rowOfNotesBinding.getRoot();
        }
    }
}
