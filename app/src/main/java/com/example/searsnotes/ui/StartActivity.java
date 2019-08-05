package com.example.searsnotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import com.example.searsnotes.R;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        creatingSplash();

    }

    private void creatingSplash() {
        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        },800);
    }


}
