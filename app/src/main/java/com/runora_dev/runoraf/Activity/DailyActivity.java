package com.runora_dev.runoraf.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.Webservice.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
Class Takes the daily food details in the form of breakfast, lunch and dinner
calculates each food item calori using the asynchoronous communication
store the details into the sqlite database

three seperate background jobs are used to fetch the calori detils of each food item using the JSON standard
 */
public class DailyActivity extends AppCompatActivity {

    String sb, sl, sd;
    String mealQuery;
    EditText etb, etl, etd;
    TextView tvb, tvl, tvd;
    View v1, v2, v3;
    String bc, lc, dc;
    Button btnClose, btnRep;
    DatabaseHelper databaseHelper;
    private FirebaseDatabase database;
    private static final String TAG = "DailyActivity";
    private Spinner spinnerMeals;
    private EditText mealEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        databaseHelper = new DatabaseHelper(getApplicationContext());
//            etb = findViewById(R.id.etb);
//            etl = findViewById(R.id.etl);
//            etd = findViewById(R.id.etd);
        tvb = findViewById(R.id.tvb);
        tvl = findViewById(R.id.tvl);
        tvd = findViewById(R.id.tvd);
        btnRep = findViewById(R.id.btndailyrep);
        btnRep = findViewById(R.id.btnclose);

        spinnerMeals = findViewById(R.id.spinnerMeals);
        mealEditText = findViewById(R.id.meal);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMeals.setAdapter(adapter);
         // initialize Firebase database
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();

        ImageView back = findViewById(R.id.food_back);
        ImageView profile = findViewById(R.id.dialy_profile_page);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Home.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return  true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();    }

    public void onClose(View v) {
        finish();
    }
    public  void onStatistic(View view){
        Intent intent = new Intent(DailyActivity.this, StatisticActivity.class);
        startActivity(intent);
    }

    public void onReport(View v) {
        Intent obj = new Intent(getApplicationContext(), DailyReportActivity.class);
        startActivity(obj);
    }

    public void calculateClk(View view) {
//        sb = etb.getText().toString();
//        sl = etl.getText().toString();
//        sd = etd.getText().toString();
        String selectedMeal = spinnerMeals.getSelectedItem().toString();
        mealQuery =  mealEditText.getText().toString();

        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
//        new MyAsyncTask().execute();  //three background jobs
//        new MyAsyncTaskL().execute();
//        new MyAsyncTaskD().execute();
        new CallApiTask().execute();
    }

    public void onSave(View v) {
        float fb, fl, fd;         //storing the values in to the sqlite database
        fb = Float.parseFloat(bc);
        fl = Float.parseFloat(lc);
        fd = Float.parseFloat(dc);
        String pattern = "dd-MM-yyyy";
        String dt = new SimpleDateFormat(pattern).format(new Date());


        System.out.println(sb + " " + fb + " " + sl + " " + fl + " " + sd + " " + fd + " " + dt);

        // Save data to offline
        databaseHelper.addDailyFood(sb, fb, sl, fl, sd, fd, dt);

         // Save data to firebase
        //Create a new database reference object
        DatabaseReference reference = database.getReference("dailyFood");


        // Create hashmap object to store the data
        HashMap<String, Object> data = new HashMap<>();
        data.put("breakfastCalories", fb);
        data.put("lunchCalories", fl);
        data.put("dinnerCalories",fd);
        data.put("date", dt);
        // Use the setValue() method to save the data to Firebase Realtime Database
        reference.push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Saved to Firebase");
                Toast.makeText(getApplicationContext(), "Added to Firebase Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error saving to Firebase", e.fillInStackTrace());
                Log.e(TAG, "Error saving to Firebase Stack trace", e.fillInStackTrace());
                Log.e(TAG, "Error saving to Firebase cause", e.getCause());
                Toast.makeText(getApplicationContext(), "Error adding data", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG,"Saved to firebase"+reference);
        Log.d(TAG,"Saved to firebase"+data);

        // Display a toast message to the user
        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();

        // Close the current activity
        this.finish();
    }

    public void saveDataToFirebase(){

    }

    private class CallApiTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... queries) {
//            String query = queries[0];
            String apiKey = "JbKygV4Po77yVvpiiMArPQ==xaVWcihDC54KWLNR"; // Replace with your actual API key
            String apiUrl = "https://api.calorieninjas.com/v1/nutrition?query=" + mealQuery;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("X-Api-Key", apiKey)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                // Handle the API response here
                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray h = j.getJSONArray("items"); // Replace "items" with the actual key in your API response

                    if (h.length() > 0) {
                        JSONObject item = h.getJSONObject(0);

                        double calories = item.getDouble("calories"); // Replace "calories" with the actual key in your API response

                        // Update your UI elements with the calculated calories
                        tvb.setText("Food Calories: " + calories);
                        v1 = findViewById(R.id.view0);
                        v2 = findViewById(R.id.view1);
                        v3 = findViewById(R.id.view2);
                        // Hide your views as needed
                        v1.setVisibility(View.INVISIBLE);
                        v2.setVisibility(View.INVISIBLE);
                        v3.setVisibility(View.INVISIBLE);

                    } else {
                        // Handle case when no items are found
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle error
            }
        }
    }

    /* #####AsyncTask Subclass################################################################### */
    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String allStrings;
            try {
                URL myUrl = new URL("https://api.nutritionix.com/v1_1/search/" +
                        sb + "?fields=item_name%2Citem_id%2Cnf_calories%2Cnf_total_fat" +
                        "&appId=3fe5fa47&appKey=61729b9d2d8612a629467f0cdbbd6d2c");
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setConnectTimeout(700);
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);

                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                allStrings = stringBuilder.toString();
                publishProgress(allStrings);

            } catch (Exception e) {
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            try {
                JSONObject j = new JSONObject(values[0]);

                JSONArray h = (JSONArray) j.get("hits");

                JSONObject rec = h.getJSONObject(0);

                JSONObject fields = rec.getJSONObject("fields");

                bc = fields.getString("nf_calories");

                tvb.setText("Break Fast: " + bc);

                v1 = findViewById(R.id.view0);
                v1.setVisibility(View.INVISIBLE);
                v2 = findViewById(R.id.view1);
                v2.setVisibility(View.INVISIBLE);
                v3 = findViewById(R.id.view2);
                v3.setVisibility(View.INVISIBLE);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* #####AsyncTask Subclass################################################################### */
    private class MyAsyncTaskL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String allStrings;
            try {
                URL myUrl = new URL("https://api.nutritionix.com/v1_1/search/" +
                        sl + "?fields=item_name%2Citem_id%2Cnf_calories%2Cnf_total_fat" +
                        "&appId=3fe5fa47&appKey=61729b9d2d8612a629467f0cdbbd6d2c");
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setConnectTimeout(700);
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);

                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                allStrings = stringBuilder.toString();
                publishProgress(allStrings);

            } catch (Exception e) {
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            try {
                JSONObject j = new JSONObject(values[0]);

                JSONArray h = (JSONArray) j.get("hits");

                JSONObject rec = h.getJSONObject(0);

                JSONObject fields = rec.getJSONObject("fields");

                lc = fields.getString("nf_calories");

                tvl.setText("Lunch: " + lc);

                v1 = findViewById(R.id.view0);
                v1.setVisibility(View.INVISIBLE);
                v2 = findViewById(R.id.view1);
                v2.setVisibility(View.INVISIBLE);
                v3 = findViewById(R.id.view2);
                v3.setVisibility(View.INVISIBLE);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* #####AsyncTask Subclass################################################################### */
    private class MyAsyncTaskD extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String allStrings;
            try {
                URL myUrl = new URL("https://api.nutritionix.com/v1_1/search/" +
                        sd + "?fields=item_name%2Citem_id%2Cnf_calories%2Cnf_total_fat" +
                        "&appId=3fe5fa47&appKey=61729b9d2d8612a629467f0cdbbd6d2c");
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setConnectTimeout(700);
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);

                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                allStrings = stringBuilder.toString();
                publishProgress(allStrings);

            } catch (Exception e) {
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            try {
                JSONObject j = new JSONObject(values[0]);

                JSONArray h = (JSONArray) j.get("hits");

                JSONObject rec = h.getJSONObject(0);

                JSONObject fields = rec.getJSONObject("fields");

                dc = fields.getString("nf_calories");

                tvd.setText("Dinner: " + dc);

                v1 = findViewById(R.id.view0);
                v1.setVisibility(View.INVISIBLE);
                v2 = findViewById(R.id.view1);
                v2.setVisibility(View.INVISIBLE);
                v3 = findViewById(R.id.view2);
                v3.setVisibility(View.INVISIBLE);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
