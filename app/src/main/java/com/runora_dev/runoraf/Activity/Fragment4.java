package com.runora_dev.runoraf.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.runora_dev.runoraf.R;

public class Fragment4 extends Fragment implements View.OnClickListener {

    Button b2, b3, goal_btn;
    String age, weight, height, name;
    TextView tv, tv1, tv2, tv3, tv4, tv5, tv6;
    ImageView iv1, iv2;
    FragmentsCommunicator fc;
    SeekBar seekBar;
    double bmi, water;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f4_layout, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Questv1-Bold.otf");

        Home activity = (Home) getActivity();
        age = activity.getMyData("age");
        weight = activity.getMyData("weight");
        height = activity.getMyData("height");
        name = activity.getMyData("name");
        //age = "45";
        //weight = "70";
        //height = "180";

        iv1 = view.findViewById(R.id.imageView1);
        iv1.setOnClickListener(this);
        iv2 = view.findViewById(R.id.imageView2);
        iv2.setOnClickListener(this);

        b2 = view.findViewById(R.id.waterBtn);
        b2.setOnClickListener(this);
        b3 = view.findViewById(R.id.caloriesBtn);
        goal_btn = view.findViewById(R.id.goal_activity);
        b3.setOnClickListener(this);

        tv1 = view.findViewById(R.id.bmiTV1);
        tv2 = view.findViewById(R.id.bmiTV2);
        tv = view.findViewById(R.id.nameTV);
        tv4 = view.findViewById(R.id.ageTV);
        tv3 = view.findViewById(R.id.heightTV);
        tv5 = view.findViewById(R.id.weightTV);
        tv6 = view.findViewById(R.id.waterTV);

        //tv3.setText(activity.getMyData("gender"));
        tv.setText(name);
        tv.setTypeface(tf);
        tv4.setText(age + " YEAR");
        tv4.setTypeface(tf);
        tv3.setText(height + "CM");
        tv3.setTypeface(tf);
        tv5.setText(weight + " KG");
        tv5.setTypeface(tf);


        ImageView profileBack = view.findViewById(R.id.profile_back);
        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Home.class);
                startActivity(intent);
            }
        });

        goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GoalActivity.class);
                startActivity(intent);
            }
        });

        //BMI Calculations
        try {
            int weightValue = Integer.parseInt(weight);
            int heightValue = Integer.parseInt(height);
            int ageValue = Integer.parseInt(age);

            bmi = (weightValue * 10000) / (heightValue * heightValue);
            tv1.setText("Your BMI: " + Math.round(bmi * 10d) / 10d);
            tv1.setTypeface(tf);

            if (bmi >= 30) {
                tv2.setText("Obesity");
            } else if ((bmi >= 25) && (bmi < 30)) {
                tv2.setText("Overweight");
            } else if (bmi <= 18) {
                tv2.setText("Under Weight");
            } else if ((bmi > 18) && (bmi < 25)) {
                tv2.setText("Normal");
            }

            seekBar = view.findViewById(R.id.seekBar);
            seekBar.setProgress((int) Math.round(bmi));

            // Calculate Body water
            if (ageValue <= 30) {
                water = (weightValue * 42 * 2.95) / (28.3 * 100);
            } else if (ageValue > 30 && ageValue <= 35) {
                water = (weightValue * 37 * 2.95) / (28.3 * 100);
            } else if (ageValue > 35) {
                water = (weightValue * 32 * 2.95) / (28.3 * 100);
            }

            tv6.setText("You need: " + (Math.round(water * 10d) / 10d) + " L/day");
        } catch (NumberFormatException e) {
            // Handle the case when weight, height, or age is null or not a valid number
            // For example, show an error message or provide default values
            tv1.setText("Error: Invalid weight or height or age data");
            tv2.setText("");
            tv6.setText("");
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView1) {
            Toast.makeText(getActivity(), "BMI = Body Mass Index", Toast.LENGTH_LONG).show();
        } else if (view.getId() == R.id.imageView2) {
            Toast.makeText(getActivity(), "Water Body needs", Toast.LENGTH_LONG).show();
        } else if (view.getId() == R.id.waterBtn) {
            Intent RemindersIntent = new Intent(getActivity(), RemindersActivity.class);
            startActivity(RemindersIntent);
        } else if (view.getId() == R.id.caloriesBtn) {
            Intent apiIntent = new Intent(getActivity(), ApiActivity.class);
            startActivity(apiIntent);
        }
    }
}
