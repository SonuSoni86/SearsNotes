package com.example.searsnotes.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.searsnotes.dao.RetrofitClientInstance;
import com.example.searsnotes.dao.WordApi;
import com.example.searsnotes.model.Definitions;
import com.example.searsnotes.model.DictionaryMeaning;

import org.jetbrains.annotations.NotNull;

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
        //noinspection deprecation
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String gettime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        return simpleDateFormat.format(date);
    }


    private static boolean checkIfWord(String text) {
            return !(text.indexOf(' ')>=0);
    }

    static void getWordMeaning(String textSelected, final Context context) {
        if (!checkIfWord(textSelected)) {
            Toast.makeText(context, "Meaning of only words can be shown", Toast.LENGTH_LONG).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            WordApi wordApi = RetrofitClientInstance.getRetrofitInstance().create(WordApi.class);
            Call<DictionaryMeaning> meaningCall = wordApi.getMeaning(textSelected);

            meaningCall.enqueue(new Callback<DictionaryMeaning>() {
                @Override
                public void onResponse(@NotNull Call<DictionaryMeaning> call, @NotNull Response<DictionaryMeaning> response) {
                    progressDialog.dismiss();
                    if (!response.isSuccessful()) {
                        if (response.code() == 404) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Word not found | Check your spelling");
                            builder.show();
                        } else
                            Toast.makeText(context, "something went wrong: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    assert response.body() != null;
                    List<Definitions> definitionsList = response.body().getDefinitions();
                    if (definitionsList.size() == 0) {
                        Toast.makeText(context, "No definition found", Toast.LENGTH_LONG).show();
                    } else {
                        String value = definitionsList.get(0).getDefinition();
                        AlertDialog.Builder dialogue = new AlertDialog.Builder(context);
                        dialogue.setMessage(value);
                        dialogue.show();
                    }
                }


                @Override
                public void onFailure(@NotNull  Call<DictionaryMeaning> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public static boolean hasAllPermissions(Context context) {
       return ((ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
               && (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
               && (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));

    }

}
