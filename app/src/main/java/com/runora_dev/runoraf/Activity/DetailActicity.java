package com.runora_dev.runoraf.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.Webservice.DatabaseHelper;

import java.util.HashMap;


public class DetailActicity extends AppCompatActivity {
    TextView search, su, fi, serv, fat, fatT, cal, pro, carbo, sodi, potas, chole;
    String name;
    Button add, back;
    DatabaseHelper databaseHelper;
    String sodiumMg = "0", potassiumMg = "0", cholesterolMg = "0";
    double sugarG = 0, fiberG = 0, servingSizeG = 0, fatSaturatedG = 0, fatTotalG = 0, calories = 0, proteinG = 0, carbohydratesTotalG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpage);
        initdata();
    }

    private void initdata() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        name = getIntent().getStringExtra("name");
        sodiumMg = String.valueOf(getIntent().getIntExtra("sodiumMg", 0));
        sugarG = getIntent().getDoubleExtra("sugarG", 0);
        cholesterolMg = String.valueOf(getIntent().getIntExtra("cholesterolMg", 0));
        potassiumMg = String.valueOf(getIntent().getIntExtra("potassiumMg", 0));
        fiberG = getIntent().getDoubleExtra("fiberG", 0);
        servingSizeG = getIntent().getDoubleExtra("servingSizeG", 0);
        fatSaturatedG = getIntent().getDoubleExtra("fatSaturatedG", 0);
        fatTotalG = getIntent().getDoubleExtra("fatTotalG", 0);
        calories = getIntent().getDoubleExtra("calories", 0);
        proteinG = getIntent().getDoubleExtra("proteinG", 0);
        carbohydratesTotalG = getIntent().getDoubleExtra("carbohydratesTotalG", 0);
        search = findViewById(R.id.search);
        search.setText(name);
        su = findViewById(R.id.sugar);
        su.setText(String.format("%.2f", sugarG) + " gm");
        fi = findViewById(R.id.fiber);
        fi.setText(String.format("%.2f", fiberG) + " gm");
        serv = findViewById(R.id.serv);
        serv.setText(String.format("%.2f", servingSizeG) + " gm");
        fat = findViewById(R.id.fat);
        fat.setText(String.format("%.2f", fatSaturatedG) + " gm");
        fatT = findViewById(R.id.totalfat);
        fatT.setText(String.format("%.2f", fatTotalG) + " gm");
        cal = findViewById(R.id.cal);
        cal.setText(String.format("%.2f", calories) + " gm");
        pro = findViewById(R.id.protine);
        pro.setText(String.format("%.2f", proteinG) + " gm");
        carbo = findViewById(R.id.carb);
        carbo.setText(String.format("%.2f", carbohydratesTotalG) + " gm");
        sodi = findViewById(R.id.sodium);
        sodi.setText(sodiumMg + " mg");
        potas = findViewById(R.id.pottasium);
        potas.setText(potassiumMg + " mg");
        chole = findViewById(R.id.colestrol);
        potas.setText(cholesterolMg + " mg");
        add = findViewById(R.id.add);
        back = findViewById(R.id.back);

        if (getIntent().hasExtra("from")) {
            //add.setVisibility(View.GONE);
            add.setText("Delete");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a reference to the Firebase database where you want to store your data
                DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("foods");


                //String sugarG, String fiberG, String servingSizeG, String sodiumMg, String name1, String potassiumMg, String fatSaturatedG, String fatTotalG, String calories1, String cholesterolMg, String proteinG,String carbohydratesTotalG
                if (getIntent().hasExtra("from")) {
                    //add.setVisibility(View.GONE);
                    databaseHelper.Delete(name);
                    Log.d("name", "1" + name + "1");
                    finish();
                } else {
                    boolean res = databaseHelper.addfood(String.valueOf(sugarG), String.valueOf(fiberG), String.valueOf(servingSizeG), String.valueOf(sodiumMg), String.valueOf(name), String.valueOf(potassiumMg), String.valueOf(fatSaturatedG), String.valueOf(fatTotalG), String.valueOf(cholesterolMg), String.valueOf(proteinG), String.valueOf(proteinG), String.valueOf(carbohydratesTotalG));
                    if (res) {
                        // create a new HashMap to store the data that will be added to the Firebase database
                        HashMap<String, Object> foodMap = new HashMap<>();
                        foodMap.put("name", name);
                        foodMap.put("sugarG", sugarG);
                        foodMap.put("fiberG", fiberG);
                        foodMap.put("servingSizeG", servingSizeG);
                        foodMap.put("sodiumMg", sodiumMg);
                        foodMap.put("potassiumMg", potassiumMg);
                        foodMap.put("fatSaturatedG", fatSaturatedG);
                        foodMap.put("fatTotalG", fatTotalG);
                        foodMap.put("cholesterolMg", cholesterolMg);
                        foodMap.put("proteinG", proteinG);
                        foodMap.put("carbohydratesTotalG", carbohydratesTotalG);

                        // add the data to the Firebase database
                        String foodId = foodRef.push().getKey();
                        foodRef.child(foodId).setValue(foodMap);

                        Intent intent = new Intent(getApplicationContext(), AddesSuccessfully.class);
                        //  Toast.makeText(Detailpage.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DetailActicity.this, "Item Adding Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}