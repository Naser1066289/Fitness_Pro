package com.runora_dev.runoraf.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.runora_dev.runoraf.R;


public class Splash extends AppCompatActivity {
    Handler mHandler;
    Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                Intent splashIntent = new Intent(Splash.this, Splash2.class);
                startActivity(splashIntent);

            }
        };

        // its trigger runnable after 3000 millisecond.
        mHandler.postDelayed(mRunnable,0000);
    }
}