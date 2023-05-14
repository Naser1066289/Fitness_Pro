package com.runora_dev.runoraf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.runora_dev.runoraf.R;

public class GoalActivity extends AppCompatActivity {
  SeekBar kgSeekbar, monthsSeekbar;
  Button calculateBtn;
  TextView contentTextView, kgValue, monthsValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        kgSeekbar = (SeekBar)  findViewById(R.id.seekbar_kg);
        monthsSeekbar = (SeekBar)  findViewById(R.id.seekBar_months);
        calculateBtn = (Button)  findViewById(R.id.calculate_goal_button);
        contentTextView = (TextView) findViewById(R.id.content_text_view);
        kgValue = (TextView) findViewById(R.id.kgValue);
        monthsValue = (TextView) findViewById(R.id.monthsValue);


        kgSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 kgValue.setText(i+ " kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        monthsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                monthsValue.setText(i+" month");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedKg = kgSeekbar.getProgress();
                int selectedMonths = monthsSeekbar.getProgress();

                // Calculate the daily calorie deficit required to lose the selected kg in the selected months
                double dailyCalorieDeficit = (selectedKg * 7700) / (selectedMonths * 30);

                // Calculate the daily calorie intake and exercise duration for weight loss
                double basalMetabolicRate = 1500; // Assumption: Average BMR of a sedentary person
                double dailyCalorieIntake = basalMetabolicRate - dailyCalorieDeficit; // Assumption: Reduce 500 calories per day for weight loss
                int exerciseDuration = 30; // Assumption: 30 minutes of moderate exercise per day

                // Generate the weight loss plan text
                String weightLossPlan = "To lose " + selectedKg + " kg in " + selectedMonths + " months, you need to maintain a daily calorie deficit of " + dailyCalorieDeficit + " calories. We recommend following a balanced meal plan with a calorie intake of " + dailyCalorieIntake + " calories per day and exercising for " + exerciseDuration + " minutes per day.";

                // Set the weight loss plan text in the content text view
                TextView contentTextView = findViewById(R.id.content_text_view);
                contentTextView.setText(weightLossPlan);
            }
        });
     }

}