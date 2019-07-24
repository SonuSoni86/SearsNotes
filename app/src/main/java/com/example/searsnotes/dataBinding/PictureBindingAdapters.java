package com.example.searsnotes.dataBinding;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.searsnotes.R;
import com.squareup.picasso.Picasso;

public class PictureBindingAdapters {

    @BindingAdapter("imageResource")
    public static void setImageUri(ImageView imageView,String tempUri){
        Context context = imageView.getContext();
       if(tempUri!=null && !tempUri.equals("default"))
       {
           Picasso.with(context).load(Uri.parse(tempUri)).into(imageView);
           return;
       }
           imageView.setImageResource(R.drawable.note_thumbnail);
    }
}
