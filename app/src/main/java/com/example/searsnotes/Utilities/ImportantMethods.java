package com.example.searsnotes.Utilities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.searsnotes.Dao.RetrofitClientInstance;
import com.example.searsnotes.Dao.WordApi;
import com.example.searsnotes.Model.Definitions;
import com.example.searsnotes.Model.DictionaryMeaning;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportantMethods {

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static String gettime()
    {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String formattedtime = simpleDateFormat.format(date);
        return formattedtime;
    }

    public static boolean checkIfWord(String text){
        if(text.indexOf(' ')>=0)return false;
        else return true;
    }

    public static void getWordMeaning(String textSelected, final Context context){
        if(!checkIfWord(textSelected)){
            Toast.makeText(context,"Meaning of only words can be shown",Toast.LENGTH_LONG).show();
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            WordApi wordApi = RetrofitClientInstance.getRetrofitInstance().create(WordApi.class);
            Call<DictionaryMeaning> meaningCall =wordApi.getMeaning(textSelected);

            meaningCall.enqueue(new Callback<DictionaryMeaning>() {
                @Override
                public void onResponse(Call<DictionaryMeaning> call, Response<DictionaryMeaning> response)
                {
                    progressDialog.dismiss();
                    if(!response.isSuccessful()){
                        if(response.code()==404){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Word not found | Check your spelling");
                            builder.show();
                        }
                        else
                            Toast.makeText(context,"something went wrong: "+response.code(),Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<Definitions> definitionsList = response.body().getDefinitions();
                    if(definitionsList.size()==0)
                    {
                        Toast.makeText(context,"No definition found",Toast.LENGTH_LONG).show();
                    }else{
                        String value = definitionsList.get(0).getDefinition();
                        AlertDialog.Builder dialogue = new AlertDialog.Builder(context);
                        dialogue.setMessage(value);
                        dialogue.show();
                    }
                }

                @Override
                public void onFailure(Call<DictionaryMeaning> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }


}
