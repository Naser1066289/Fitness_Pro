package com.runora_dev.runoraf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.runora_dev.runoraf.R;


public class NotifcationView extends AppCompatActivity {
    TextView textView;
    Button bclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcation_view);
        textView = findViewById(R.id.tvRR);
        bclose = findViewById(R.id.btnDRVclose);
        //getting the notification message
        //String message=getIntent().getStringExtra("message");
        textView.setText("Drink A Cup of Water");
        bclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}