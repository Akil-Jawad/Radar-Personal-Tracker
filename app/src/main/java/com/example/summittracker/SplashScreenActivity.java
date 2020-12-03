package com.example.summittracker;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
