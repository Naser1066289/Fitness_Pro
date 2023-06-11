package com.runora_dev.runoraf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

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

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        LineChart lineChart = findViewById(R.id.lineChartBurned);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        pieChart = (PieChart) findViewById(R.id.pieChart);
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

//                // Set additional properties of the chart
//                pieChartConsumedFood.setData(pieData);
//                pieChartConsumedFood.getDescription().setEnabled(false);
//                pieChartConsumedFood.setDrawEntryLabels(false);
//                pieChartConsumedFood.setHoleColor(Color.TRANSPARENT);
//                pieChartConsumedFood.setTransparentCircleRadius(0f);
//
//                // Refresh the chart
//                pieChartConsumedFood.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
        // Create entries for the graph
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(10, "Last Week"));
        entries.add(new PieEntry(8, "Last Week"));
        entries.add(new PieEntry(15, "Last Month"));

        // Create a dataset with the entries
        PieDataSet dataSet = new PieDataSet(entries, "");

        // Set colors for the segments
        dataSet.setColors(Color.BLUE, Color.GREEN, Color.RED);

        // Create a PieData object and add the dataset to it
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);

        // Set additional properties of the chart
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(0f);

        // Refresh the chart
        pieChart.invalidate();


        // Dummy food consumption data for the last 30 days
        float[] foodConsumption = {2.5f, 3.0f, 2.0f, 2.5f, 3.5f, 4.0f, 3.5f, 3.0f, 2.5f, 3.0f,
                3.5f, 3.0f, 2.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 3.5f, 3.0f,
                2.5f, 3.0f, 3.5f, 3.0f, 2.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f};

        ArrayList<Entry> entriess = new ArrayList<>();
        for (int i = 0; i < foodConsumption.length; i++) {
            entriess.add(new Entry(i, foodConsumption[i]));
        }

        LineDataSet dataSets = new LineDataSet(entriess, "Food Consumption");
        dataSets.setColor(Color.BLUE);
        dataSets.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSetsd = new ArrayList<>();
        dataSetsd.add(dataSets);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);
        lineChart.invalidate();

    }
}