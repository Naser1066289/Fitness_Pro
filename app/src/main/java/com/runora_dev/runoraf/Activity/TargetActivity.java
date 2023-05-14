package com.runora_dev.runoraf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.runora_dev.runoraf.R;

public class TargetActivity extends AppCompatActivity {
    SeekBar kgSeekbarTarget, monthsSeekbarTarget;
    Button calculateBtnSetTargetGoal;
    TextView kgValueTarget, monthsValueTarget;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        kgSeekbarTarget  =(SeekBar) findViewById(R.id.seekbar_kg_target);
        monthsSeekbarTarget  =(SeekBar) findViewById(R.id.seekBar_months_target);
        calculateBtnSetTargetGoal = (Button) findViewById(R.id.calculate_goal_button);
        kgValueTarget = (TextView) findViewById(R.id.kgValueTarget);
        monthsValueTarget = (TextView) findViewById(R.id.monthsValueTarget);

        // Get a reference to the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        kgSeekbarTarget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                kgValueTarget.setText(i + " Kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        monthsSeekbarTarget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                monthsValueTarget.setText(i + " month");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        calculateBtnSetTargetGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDatabase();
                Intent intent = new Intent(TargetActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }

    private void saveToDatabase() {
        int kgValue = kgSeekbarTarget.getProgress();
        int monthsValue = monthsSeekbarTarget.getProgress();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = mDatabase.child("users").child(userId);
        userReference.child("target").child("kg").setValue(kgValue);
        userReference.child("target").child("months").setValue(monthsValue);

        Toast.makeText(this, "Target goal saved successfully", Toast.LENGTH_SHORT).show();
    }
}