package com.runora_dev.runoraf.Activity;


import android.content.Intent;
import android.graphics.Typeface;
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


/*
class for computing the amount of water required for the body.
water drinking recommendation system to drink water after finding the body water.
class also calculates the BMI and provide recommendation
 */
public class Fragment4 extends Fragment implements View.OnClickListener{

    Button  b2, b3;
    String age, weight, height,name;
    TextView tv,tv1,tv2, tv3, tv4, tv5, tv6;
    ImageView iv1, iv2;
    FragmentsCommunicator fc;
    SeekBar seekBar;
    double bmi, water;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f4_layout,container,false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Questv1-Bold.otf");

        Home activity = (Home) getActivity();
        age = activity.getMyData("age");
        weight = activity.getMyData("weight");
        height = activity.getMyData("height");
        name = activity.getMyData("name");
        //age = "45";
        //weight = "70";
        //height = "180";


        iv1 = (ImageView)view.findViewById(R.id.imageView1);
        iv1.setOnClickListener(this);
        iv2 = (ImageView)view.findViewById(R.id.imageView2);
        iv2.setOnClickListener(this);

        b2 = (Button)view.findViewById(R.id.waterBtn);
        b2.setOnClickListener(this);
        b3 = (Button)view.findViewById(R.id.caloriesBtn);
        b3.setOnClickListener(this);

        tv1 = (TextView)view.findViewById(R.id.bmiTV1);
        tv2 = (TextView)view.findViewById(R.id.bmiTV2);
        tv = (TextView)view.findViewById(R.id.nameTV);
        tv4 = (TextView)view.findViewById(R.id.ageTV);
        tv3 = (TextView)view.findViewById(R.id.heightTV);
        tv5 = (TextView)view.findViewById(R.id.weightTV);
        tv6 = (TextView)view.findViewById(R.id.waterTV);


        //tv3.setText(activity.getMyData("gender"));
        tv.setText(name);
        tv.setTypeface(tf);
        tv4.setText(age +" YEAR");
        tv4.setTypeface(tf);
        tv3.setText(height + "CM");
        tv3.setTypeface(tf);
        tv5.setText(weight +" KG");
        tv5.setTypeface(tf);


        //BMI Calculations
        bmi = (Integer.valueOf(weight) * 10000) / (Integer.valueOf(height) * Integer.valueOf(height));
        tv1.setText("Your BMI: " + Math.round(bmi * 10d) / 10d);
        tv1.setTypeface(tf);

        if (bmi >= 30){
            tv2.setText("Obesity");
        }else if ((bmi >= 25) && (bmi < 30)){
            tv2.setText("Overweight");
        }else if (bmi <= 18){
            tv2.setText("Under Weight");
        }else if((bmi > 18) && (bmi < 25) ){
            tv2.setText("Normal");
        }

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setProgress((int)Math.round(bmi));



        //Calculate Body water
        if (Integer.valueOf(age) <= 30){
            water = (Integer.valueOf(weight) * 42 * 2.95) / (28.3 * 100);

        }else if (Integer.valueOf(age) > 30 && Integer.valueOf(age) <= 35){
            water = (Integer.valueOf(weight) * 37 * 2.95) / (28.3 * 100);

        }else if (Integer.valueOf(age) > 35){
            water = (Integer.valueOf(weight) * 32 * 2.95) / (28.3 * 100);
        }

        tv6.setText("You need: " + (Math.round(water * 10d) / 10d) + " L/day");


        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imageView1){
            Toast.makeText(getActivity(), "BMI = Body Mass Index", Toast.LENGTH_LONG).show();
        }else if(view.getId() == R.id.imageView2) {
            Toast.makeText(getActivity(), "Water Body needs", Toast.LENGTH_LONG).show();
        }else if(view.getId() == R.id.imageView3) {
            Toast.makeText(getActivity(), "Calories Analysis", Toast.LENGTH_LONG).show();
        }else if(view.getId() == R.id.waterBtn){
            Intent RemindersIntent = new Intent(getActivity(), RemindersActivity.class);
            startActivity(RemindersIntent);
        }else if(view.getId() == R.id.caloriesBtn) {
            Intent apiIntent = new Intent(getActivity(), ApiActivity.class);
            startActivity(apiIntent);
        }
    }
}
