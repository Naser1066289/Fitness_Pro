package com.runora_dev.runoraf.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.runora_dev.runoraf.R;


public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        waterReminder();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        Intent ii = getIntent();
        String val = ii.getExtras().getString("frag");
        if (val.equals("4"))
            showFragment(new Fragment4());
        else
            showFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.bottomnav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        showFragment(new HomeFragment());

                        break;
                    case R.id.offline:
                        showFragment(new OfflineFragment());

                        break;
                    case R.id.health:
                        showFragment(new Fragment4());
                        break;
                    case R.id.food:
                        Intent foodIntent = new Intent(Home.this, DailyActivity.class);
                        startActivityForResult(foodIntent, 0);
                        break;
                    case R.id.run:
                        Intent launchNewIntent = new Intent(Home.this, MapBoxActivity.class);
                        startActivityForResult(launchNewIntent, 0);

                        break;
                }

                return true;
            }
        });
    }

    public String getMyData(String s) {
        String myString = s;
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        switch (s) {
            case ("age"):
                myString = sharedPreferences.getString("age", null);
                break;
            case ("weight"):
                myString = sharedPreferences.getString("weight", null);
                break;
            case ("height"):
                myString = sharedPreferences.getString("height", null);
                break;
            case ("name"):
                myString = sharedPreferences.getString("name", null);
                break;
        }
        return myString;
    }

    private void showFragment(Fragment f) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frme, f);
        fragmentTransaction.commit();
    }

    public void waterReminder() {
        SharedPreferences sharedPreferences = getSharedPreferences("SettingsData", Activity.MODE_PRIVATE);
        int t = sharedPreferences.getInt("water_delay", 1000);
        if (sharedPreferences.getString("water_reminder", "false").matches("true")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT );
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), t * 60 * 1000, pendingIntent);
        }
    }
}