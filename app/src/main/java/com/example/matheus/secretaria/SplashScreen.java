package com.example.matheus.secretaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import static com.example.matheus.secretaria.R.layout.activity_splash_screen;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciar();
            }
        },2000);

    }

    private void iniciar(){
        Intent in = new Intent (SplashScreen.this, MainActivity.class);
        startActivity(in);
        finish();
    }



}
