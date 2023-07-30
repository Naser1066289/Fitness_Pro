package com.runora_dev.runoraf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.runora_dev.runoraf.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    private LineChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        LineChart lineChart = findViewById(R.id.lineChartBurned);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        pieChart = (LineChart) findViewById(R.id.pieChart);
        pieChart.setDragEnabled(true);
        pieChart.setScaleEnabled(false);

        ImageView statisticBack = findViewById(R.id.statisc_back);
        statisticBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getBaseContext(), Home.class);
               startActivity(intent);
            }
        });
// Get a reference to the Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("dailyFood");

        // Attach a listener to the database reference
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalBreakfastCalories = 0;
                double totalLunchCalories = 0;
                double totalDinnerCalories = 0;

                // Calculate total calories for each meal
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Double fb = data.child("breakfastCalories").getValue(Double.class);
                    Double fl = data.child("lunchCalories").getValue(Double.class);
                    Double fd = data.child("dinnerCalories").getValue(Double.class);

                    totalBreakfastCalories += (fb != null ? fb : 0);
                    totalLunchCalories += (fl != null ? fl : 0);
                    totalDinnerCalories += (fd != null ? fd : 0);
                }

                // Create entries for the graph
                List<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry((float) totalBreakfastCalories, "Breakfast"));
                entries.add(new PieEntry((float) totalLunchCalories, "Lunch"));
                entries.add(new PieEntry((float) totalDinnerCalories, "Dinner"));

                // Create a dataset with the entries
                PieDataSet dataSet = new PieDataSet(entries, "");

                // Set colors for the segments
                dataSet.setColors(Color.BLUE, Color.GREEN, Color.RED);

                // Create a PieData object and add the dataset to it
                PieData pieData = new PieData(dataSet);
                pieData.setValueTextSize(12f);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });

//        // Create entries for the graph
//        List<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(10, "Last Week"));
//        entries.add(new PieEntry(8, "Last Week"));
//        entries.add(new PieEntry(15, "Last Month"));
//
//        // Create a dataset with the entries
//        PieDataSet dataSet = new PieDataSet(entries, "");
//
//        // Set colors for the segments
//        dataSet.setColors(Color.BLUE, Color.GREEN, Color.RED);
//
//        // Create a PieData object and add the dataset to it
//        PieData pieData = new PieData(dataSet);
//        pieData.setValueTextSize(12f);
//
//        // Set additional properties of the chart
//        pieChart.setData(pieData);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setDrawEntryLabels(false);
//        pieChart.setHoleColor(Color.TRANSPARENT);
//        pieChart.setTransparentCircleRadius(0f);
//
//        // Refresh the chart
//        pieChart.invalidate();

        // Dummy food consumption data for the last 30 days
        float[] foodburnedLastWeek = {4.5f, 3.0f, 2.0f, 2.5f, 3.5f, 2.0f, 6.5f, 3.0f, 2.5f, 3.0f,
                3.5f, 4.0f, 2.5f, 2.0f, 2.5f, 5.0f, 9.5f, 4.0f, 3.6f, 3.0f};

        float[] foodburnedLastMonth = {6.5f, 5.0f, 7.0f, 5.5f, 4.5f, 6.0f, 4.5f, 5.0f, 6.5f, 4.0f,
                3.5f, 4.0f, 5.5f, 6.0f, 5.5f, 6.0f, 5.5f, 5.0f, 6.0f, 7.0f, 5.5f, 6.0f, 7.0f};

        float[] foodburnedLastYear = {8.5f, 9.0f, 7.5f, 8.0f, 9.5f, 8.0f, 8.5f, 9.0f, 7.0f, 8.0f,
                9.5f, 8.0f, 8.5f, 9.0f, 7.5f, 8.0f, 9.5f, 8.0f, 8.5f, 9.0f, 7.5f, 8.0f, 9.5f};

        ArrayList<Entry> entriesLastWeek = new ArrayList<>();
        for (int i = 0; i < foodburnedLastWeek.length; i++) {
            entriesLastWeek.add(new Entry(i, foodburnedLastWeek[i]));
        }

        ArrayList<Entry> entriesLastMonth = new ArrayList<>();
        for (int i = 0; i < foodburnedLastMonth.length; i++) {
            entriesLastMonth.add(new Entry(i, foodburnedLastMonth[i]));
        }

        ArrayList<Entry> entriesLastYear = new ArrayList<>();
        for (int i = 0; i < foodburnedLastYear.length; i++) {
            entriesLastYear.add(new Entry(i, foodburnedLastYear[i]));
        }

        LineDataSet dataSetLastWeek = new LineDataSet(entriesLastWeek, "Last Week");
        dataSetLastWeek.setColor(Color.BLUE);
        dataSetLastWeek.setValueTextColor(Color.BLACK);

        LineDataSet dataSetLastMonth = new LineDataSet(entriesLastMonth, "Last Month");
        dataSetLastMonth.setColor(Color.GREEN);
        dataSetLastMonth.setValueTextColor(Color.BLACK);

        LineDataSet dataSetLastYear = new LineDataSet(entriesLastYear, "Last Year");
        dataSetLastYear.setColor(Color.RED);
        dataSetLastYear.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetLastWeek);
        dataSets.add(dataSetLastMonth);
        dataSets.add(dataSetLastYear);

        LineData lineData = new LineData(dataSets);

        pieChart.setData(lineData);
        pieChart.invalidate();


        // Dummy food consumption data for the last 30 days
        float[] foodConsumption1 = {2.5f, 3.0f, 2.0f, 2.5f, 3.5f, 4.0f, 3.5f, 3.0f, 2.5f, 3.0f,
                3.5f, 3.0f, 2.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 3.5f, 3.0f,
                2.5f, 3.0f, 3.5f, 3.0f, 2.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f};

        ArrayList<Entry> entriess1 = new ArrayList<>();
        for (int i = 0; i < foodConsumption1.length; i++) {
            entriess1.add(new Entry(i, foodConsumption1[i]));
        }

        LineDataSet dataSets2 = new LineDataSet(entriess1, "Food Consumption");
        dataSets2.setColor(Color.BLUE);
        dataSets2.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSetsd2 = new ArrayList<>();
        dataSetsd2.add(dataSets2);

        LineData lineData1 = new LineData(dataSetsd2);

        lineChart.setData(lineData1);
        lineChart.invalidate();

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
}