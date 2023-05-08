package com.runora_dev.runoraf.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.runora_dev.runoraf.R;


public class Splash2 extends AppCompatActivity {
    Handler mHandler;
    Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.addAnimatorUpdateListener(
                (animation) -> {

                });
        animationView.playAnimation();

        if (animationView.isAnimating()) {

        }
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                Intent splashIntent = new Intent(Splash2.this, LogInActivity.class);
                startActivity(splashIntent);
                finish();

            }
        };

        // its trigger runnable after 4000 millisecond.
        mHandler.postDelayed(mRunnable,0000);
    }
}