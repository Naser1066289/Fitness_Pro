package com.runora_dev.runoraf.Activity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.runora_dev.runoraf.R;


/*

This activity is displayed while adding an item to the offline database (ie sqlite)

 */
public class AddesSuccessfully extends AppCompatActivity {
    Handler mHandler;
    Runnable mRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addes_successfully);
        initview();

    }

    private void initview() {
        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );
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
                finish();

            }
        };

        // its trigger runnable after 4000 millisecond.
        mHandler.postDelayed(mRunnable, 3000);
    }

}