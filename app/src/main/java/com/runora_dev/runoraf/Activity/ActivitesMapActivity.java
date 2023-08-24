package com.runora_dev.runoraf.Activity;

import static com.runora_dev.runoraf.Activity.TestMap.LOCATION_PERMISSION_REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.runora_dev.runoraf.R;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.runora_dev.runoraf.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ActivitesMapActivity extends AppCompatActivity implements LocationListener, com.google.android.gms.location.LocationListener, OnMapReadyCallback, PermissionsListener {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fitness-final-a14e0-default-rtdb.firebaseio.com");
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://runora-app-ceeee-default-rtdb.firebaseio.com");
    private static final int PermissionCode = 58;


    LocationManager locationManager;
    TextView HomePageDate;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;

    TelephonyManager telephonyManager;

    FusedLocationProviderClient fusionprovider;
    LocationRequest locationRequest;
    Location start_location, end_location, curr_location;
    private long elapsedTime;

    TextView distance_counter;
    TextView calories_counter;
    TextView SpdInmph;
    TextView SpdInkmh;
    TextView CountDownTimerView;
    Button play_button, pause_button, stop_btn, homebutton;
    ImageView overlayscreen;

    Chronometer timer;
    private CountDownTimer Counttimer;

    CountDownTimer countDownTimer;
    String Distance, Calories, SpeedKm, SpeedIn;
    static long StartTime = 11000;
    long TimeLeft = StartTime;

    boolean active;
    long update;
    double distance;
    double current_speed;
    private boolean isTracking = false;
    private double previousLatitude = 0;
    private double previousLongitude = 0;
    private double totalDistanceMeters = 0;
    private double currentSpeedKmph = 0;
    private double currentSpeedMph = 0;
    private double caloriesBurned = 0;

    private static final int AccessCode = 48;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_activites_map);
        mapView = findViewById(R.id.activites_mapView);
        //mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        HomePageDate = findViewById(R.id.activites_HomePageDate);
        distance_counter = findViewById(R.id.activites_distance_counter);
        calories_counter = findViewById(R.id.activites_calories_counter);
        play_button = findViewById(R.id.activites_play_button);
        pause_button = findViewById(R.id.activites_pause_button);
        stop_btn = findViewById(R.id.activites_stop_btn);
        SpdInmph = findViewById(R.id.activites_spdInmph);
        SpdInkmh = findViewById(R.id.activites_speedInkmh);
        timer = findViewById(R.id.activites_timer);
        overlayscreen = findViewById(R.id.activites_overlayScreen);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fusionprovider = LocationServices.getFusedLocationProviderClient(ActivitesMapActivity.this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Distance = distance_counter.getText().toString();
        Calories = calories_counter.getText().toString();
        SpeedKm = SpdInkmh.getText().toString();
        SpeedIn = SpdInmph.getText().toString();


        ImageView imageButton_back_home = findViewById(R.id.activites_back_button_home);
        imageButton_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });

        // Check if the READ_PHONE_STATE permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted, so you can proceed with accessing the phone state data
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                int networkType = telephonyManager.getDataNetworkType();
            }
            // Do something with the networkType data
        } else {
            // Permission has not been granted yet, so you need to request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        ToggleTheme();
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - timer.getBase()) >= 86400000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
            }
        });
        CalenderDate();
        FloatingActionButton account = (FloatingActionButton) findViewById(R.id.activites_account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitesMapActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        play_button.setOnClickListener(view -> {
            startTracking();
            play_button.setVisibility(View.GONE);
            pause_button.setVisibility(View.VISIBLE);
            stop_btn.setVisibility(View.VISIBLE);
        });

        pause_button.setOnClickListener(view -> {
            pauseTracking();
            play_button.setVisibility(View.VISIBLE);
            pause_button.setVisibility(View.GONE);
            stop_btn.setVisibility(View.VISIBLE);
        });

        stop_btn.setOnClickListener(view -> {
            captureLocation();
            // Calculate calories burned and distance here
            double distanceKm = totalDistanceMeters / 1000; // Convert meters to kilometers

            calculateCaloriesBurned(distanceKm);
            // Update the UI elements with calculated values
            updateDistanceUI(totalDistanceMeters);
            updateCaloriesUI(caloriesBurned);
            play_button.setVisibility(View.VISIBLE);
            pause_button.setVisibility(View.GONE);
            stop_btn.setVisibility(View.GONE);
        });

        // Initially, only the Play button should be visible
        play_button.setVisibility(View.VISIBLE);
        pause_button.setVisibility(View.GONE);
        stop_btn.setVisibility(View.GONE);
//        play_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                ResumeRunnable resumeRunnable = new ResumeRunnable();
//                new Thread(resumeRunnable).start();
//            }
//
//            class ResumeRunnable implements Runnable {
//                @Override
//                public void run() {
//                    play_button.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!active) {
//                                if (ContextCompat.checkSelfPermission(ActivitesMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                                        && ContextCompat.checkSelfPermission(ActivitesMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                                        && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                                        && ContextCompat.checkSelfPermission(ActivitesMapActivity.this,
//                                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, ActivitesMapActivity.this);
//                                    createLocationRequest();
//                                    timer.setBase(SystemClock.elapsedRealtime() - update);
//                                    timer.start();
//                                    play_button.setVisibility(View.GONE);
//                                    pause_button.setVisibility(View.VISIBLE);
//                                    active = true;
//                                    // calcute values
//                                    calculateValues();
//                                } else {
//                                    RequestPermissions();
//                                    {
//                                    }
//                                    play_button.setVisibility(View.VISIBLE);
//                                    active = false;
//                                }
//                            }
//                        }
//                    });
//                    pause_button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            PauseRunnable pauseRunnable = new PauseRunnable();
//                            new Thread(pauseRunnable).start();
//                        }
//
//                        class PauseRunnable implements Runnable {
//                            @Override
//                            public void run() {
//                                pause_button.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (active) {
//                                            timer.stop();
//                                            active = false;
//                                            play_button.setVisibility(View.VISIBLE);
//                                            update = SystemClock.elapsedRealtime() - timer.getBase();
//                                            locationManager.removeUpdates(ActivitesMapActivity.this);
//
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//
//                    stop_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            StopRunnable stopRunnable = new StopRunnable();
//                            new Thread(stopRunnable).start();
//
//
//                        }
//
//                        class StopRunnable implements Runnable {
//                            @Override
//                            public void run() {
//                                stop_btn.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //   SaveData();
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        stop_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                databaseReference.child("users").child("502093655").child("Distance").setValue(Distance);
//                databaseReference.child("users").child("PhoneNumber").child("Calories").setValue(Calories);
//                databaseReference.child("users").child("PhoneNumber").child("SpdInkmh").setValue(SpdInkmh);
//                databaseReference.child("users").child("PhoneNumber").child("SpdInmph").setValue(SpdInmph);
//            }
//        });


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                ActivitesMapActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                    // Initialize LocationComponent
                    LocationComponent locationComponent = mapboxMap.getLocationComponent();

                    // Activate LocationComponent with the appropriate options
                    LocationComponentActivationOptions activationOptions =
                            LocationComponentActivationOptions.builder(getApplicationContext(), style)
                                    .useDefaultLocationEngine(true) // Use the default location engine
                                    .build();

                    locationComponent.activateLocationComponent(activationOptions);

                    // Enable LocationComponent
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // Request location permission if not granted
                        ActivityCompat.requestPermissions(ActivitesMapActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    } else {
                        locationComponent.setLocationComponentEnabled(true);
                        locationComponent.addOnLocationClickListener(new OnLocationClickListener() {
                            @Override
                            public void onLocationComponentClick() {
                                // Handle user clicks on the location icon if needed
                            }
                        });

                        locationComponent.addOnCameraTrackingChangedListener(new OnCameraTrackingChangedListener() {
                            @Override
                            public void onCameraTrackingChanged(int currentMode) {
                                // Handle camera tracking changes if needed
                            }

                            @Override
                            public void onCameraTrackingDismissed() {
                                // Handle camera tracking dismissed if needed
                            }
                        });

                        // Set up location engine request
                        LocationEngineRequest request = new LocationEngineRequest.Builder(1000) // Update interval in milliseconds
                                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                                .setMaxWaitTime(2000) // Maximum wait time between updates
                                .build();

                        // Set up location engine callback
                        LocationEngineCallback<LocationEngineResult> callback = new LocationEngineCallback<LocationEngineResult>() {
                            @Override
                            public void onSuccess(LocationEngineResult result) {
                                Location location = result.getLastLocation();
                                if (location != null) {
                                    // Calculate distance and speed
                                    if (isTracking) {
                                        double newLatitude = location.getLatitude();
                                        double newLongitude = location.getLongitude();
                                        double distance = calculateDistance(previousLatitude, previousLongitude, newLatitude, newLongitude);

                                        // Calculate time elapsed
                                        long timeElapsedMillis = System.currentTimeMillis() - StartTime;

                                        // Update total distance with the accumulated distance
                                        totalDistanceMeters += distance;

                                        // Calculate speed in km/h and mph based on distance and time
                                        double timeElapsedHours = timeElapsedMillis / (1000.0 * 60 * 60);
                                        currentSpeedKmph = totalDistanceMeters / timeElapsedHours;
                                        currentSpeedMph = totalDistanceMeters / (timeElapsedHours * 1.60934);

                                        // Calculate calories burned
                                        calculateCaloriesBurned(distance);

                                        // Update UI elements with calculated values
                                        updateElapsedTimeUI(timeElapsedMillis);
                                        updateDistanceUI(totalDistanceMeters);
                                        updateSpeedUI(currentSpeedKmph, currentSpeedMph);
                                        updateCaloriesUI(caloriesBurned);

                                        // Update previous location for next calculation
                                        previousLatitude = newLatitude;
                                        previousLongitude = newLongitude;
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle location engine failure if needed
                            }
                        };

                        // Request location updates
                        locationComponent.getLocationEngine().requestLocationUpdates(request, callback, getMainLooper());
                    }
                });


//                play_button.setOnClickListener(view -> startTracking());
//                stop_btn.setOnClickListener(view -> captureLocation());
//                playButton.setOnClickListener(view -> startTracking());
//                finishButton.setOnClickListener(view -> captureLocation());
            }

        });

    }
    private void startTracking() {
        // Start location tracking logic
        isTracking = true;

        // Capture the start time
        StartTime = System.currentTimeMillis();

        // Request location updates using LocationEngine
        // Update map with location changes
// Capture the start location
        start_location = mapboxMap.getLocationComponent().getLastKnownLocation();
        if (start_location != null) {
            previousLatitude = start_location.getLatitude();
            previousLongitude = start_location.getLongitude();
        }
        // Start the timer
        startTimer();

        // Update the camera position to the current location
        if (start_location != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(start_location.getLatitude(), start_location.getLongitude()), 14));
        }
    }

    private void startTimer() {
        Counttimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime = System.currentTimeMillis() - StartTime;
                updateElapsedTimeUI(elapsedTime);
                updateValues();

            }

            @Override
            public void onFinish() {
                // Timer finished (this might not happen with Long.MAX_VALUE)
            }
        };
        Counttimer.start();
    }
    private void pauseTracking() {
        // Pause tracking logic
        isTracking = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void stopTracking() {
        // Stop tracking logic
        pauseTracking();

        // Capture the end location
        Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
        if (lastLocation != null) {
            double endLatitude = lastLocation.getLatitude();
            double endLongitude = lastLocation.getLongitude();

            double distance = calculateDistance(start_location.getLatitude(), start_location.getLongitude(), endLatitude, endLongitude);
            totalDistanceMeters += distance;

            // Calculate time elapsed
            long timeElapsedMillis = System.currentTimeMillis() - StartTime;

            // Calculate calories burned based on distance and time
            calculateCaloriesBurned(distance);

            // Update UI elements with calculated values
            updateDistanceUI(totalDistanceMeters);
            updateCaloriesUI(caloriesBurned);
        }
    }
    private void updateValues() {
        Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
        if (lastLocation != null) {
            double newLatitude = lastLocation.getLatitude();
            double newLongitude = lastLocation.getLongitude();

            // Calculate distance between current and previous locations
            double distance = calculateDistance(previousLatitude, previousLongitude, newLatitude, newLongitude);

            // Calculate time taken to travel between two consecutive locations
            long timeElapsedMillis = System.currentTimeMillis() - StartTime;
            double timeElapsedHours = timeElapsedMillis / (1000.0 * 60 * 60);

            // Calculate speed in km/h and mph based on distance and time
            currentSpeedKmph = distance / timeElapsedHours;
            currentSpeedMph = currentSpeedKmph / 1.60934;

            // Update total distance with the accumulated distance
            totalDistanceMeters += distance;

            // Update previous location for next calculation
            previousLatitude = newLatitude;
            previousLongitude = newLongitude;

            // Update UI elements with calculated values
            updateElapsedTimeUI(timeElapsedMillis);
            updateDistanceUI(totalDistanceMeters);
            updateSpeedUI(currentSpeedKmph, currentSpeedMph);
            calculateCaloriesBurned(totalDistanceMeters / 1000); // Convert to kilometers
        }
    }

    private void captureLocation() {
        if (isTracking) {
            // Capture the end location
            Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
            if (lastLocation != null) {
                double endLatitude = lastLocation.getLatitude();
                double endLongitude = lastLocation.getLongitude();

                double distance = calculateDistance(start_location.getLatitude(), start_location.getLongitude(), endLatitude, endLongitude);
                // Update total distance with the accumulated distance
                totalDistanceMeters += distance;

                // Calculate time elapsed
                long timeElapsedMillis = System.currentTimeMillis() - StartTime;

                // Calculate calories burned based on distance and time
                calculateCaloriesBurned(distance);

                // Stop the timer
                if (Counttimer != null) {
                    Counttimer.cancel();
                    Counttimer = null;
                }

                // Update UI elements with calculated values
                updateDistanceUI(totalDistanceMeters);
                updateCaloriesUI(caloriesBurned);
                updateElapsedTimeUI(timeElapsedMillis);
            }

            isTracking = false;
        }
    }

    private void updateElapsedTimeUI(long timeInMillis) {
        Chronometer elapsedTimeTextView = findViewById(R.id.activites_timer);
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String elapsedTimeText = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        elapsedTimeTextView.setText(elapsedTimeText);
    }

    private void calculateCaloriesBurned(double distanceKm) {
        // Constants for calorie estimation (replace with appropriate values)
        double caloriesPerKm = 30.0; // Calories burned per kilometer

        // Calculate calories burned based on distance
        double calories = caloriesPerKm * distanceKm;

        // Update UI with calculated calories
        caloriesBurned = calories; // Update the global caloriesBurned variable
        updateCaloriesUI(calories);
    }
    private void updateSpeedUI(double kmph, double mph) {
        TextView speedKmphTextView = findViewById(R.id.activites_speedInkmh);
        TextView speedMphTextView = findViewById(R.id.activites_spdInmph);
        speedKmphTextView.setText(String.format("%.2f km/h", kmph));
        speedMphTextView.setText(String.format("%.2f mph", mph));
    }

    private void updateCaloriesUI(double calories) {
        TextView caloriesTextView = findViewById(R.id.activites_calories_counter);
        caloriesTextView.setText(String.format("%.2f calories", calories));
    }

    private void updateDistanceUI(double distanceMeters) {
        TextView distanceTextView = findViewById(R.id.activites_distance_counter);
        distanceTextView.setText(String.format("%.2f km", distanceMeters / 1000));
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in kilometers
    }
    private void calculateValues() {
        // Get user input or predefined values
        double distanceKm = 10.0; // Example distance in kilometers
        double timeMinutes = 30.0; // Example time in minutes
        double weightKg = 70.0; // Example weight in kilograms

        // Calculate speed in mph
        double distanceMiles = distanceKm * 0.621371; // Convert distance to miles
        double timeHours = timeMinutes / 60.0; // Convert time to hours
        double speedMph = distanceMiles / timeHours; // Calculate speed in mph

        // Calculate speed in km/h
        double speedKmH = distanceKm / timeHours; // Calculate speed in km/h

        // Calculate distance in miles
        double distanceMiless = distanceKm * 0.621371;

        // Calculate burned calories (example formula for running)
        double caloriesBurned = 0.75 * weightKg * distanceKm;

        // Update TextViews with calculated values
        SpdInmph.setText(String.format("%.2f mph", speedMph));
        SpdInkmh.setText(String.format("%.2f km/h", speedKmH));
        distance_counter.setText(String.format("%.2f miles", distanceMiles));
        calories_counter.setText(String.format("%.2f cal", caloriesBurned));

    }


    private void RequestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivitesMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Required")
                    .setMessage("Please allow permission access to proceed.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ActivityCompat.requestPermissions(ActivitesMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                                Manifest.permission.READ_PHONE_STATE}, AccessCode);
                            }
                            ActivityCompat.requestPermissions(ActivitesMapActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.READ_PHONE_STATE}, AccessCode);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.READ_PHONE_STATE}, AccessCode);
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE}, AccessCode);
        }
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void LocationPermissionRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivitesMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Required")
                    .setMessage("Please allow permission access to proceed.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ActivityCompat.requestPermissions(ActivitesMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                                Manifest.permission.READ_PHONE_STATE}, PermissionCode);
                            }
                            ActivityCompat.requestPermissions(ActivitesMapActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.READ_PHONE_STATE}, PermissionCode);
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.READ_PHONE_STATE}, PermissionCode);
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE}, PermissionCode);
        }
    }


    public void LocationAlert() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
            dialogbuilder.setMessage(" Enable GPS To Continue")
                    .setPositiveButton("Turn location on", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent call_gps_settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(call_gps_settings);


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alertDialog = dialogbuilder.create();
            alertDialog.show();

        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    private void startRun_interface() {
        Intent intent = new Intent(ActivitesMapActivity.this, RunInterface.class);
        startActivity(intent);

    }


    @Override
    public void onProviderDisabled(final String provider) {

    }

    public void CalenderDate() {
        Calendar calendar = Calendar.getInstance();
        HomePageDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        ActivitesMapActivity.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.OUTDOORS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });


    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, so you can proceed with accessing the phone state data
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    int networkType = telephonyManager.getDataNetworkType();
                }
                // Do something with the networkType data
            } else {
                // Permission has been denied, so you need to handle the situation accordingly
                // For example, you could show a message explaining why the permission is necessary and ask the user to grant it again
            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }
//    private void distanceInMiles() {
//        String miles = "mi";
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        SpannableString spannableString = new SpannableString(miles);
//        spannableString.setSpan(new RelativeSizeSpan(0.50f), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        distance = distance + (start_location.distanceTo(end_location) * 0.00062137);
//        start_location = end_location;
//        builder.append(new DecimalFormat("0.00 ").format(distance));
//        builder.append(spannableString);
//        distance_counter.setText(builder);
//
//
//    }

    private void distanceInkilometers() {
        String kilometers = "km";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(kilometers);
        spannableString.setSpan(new RelativeSizeSpan(0.50f), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        distance = distance + (start_location.distanceTo(end_location) / 1000);
        start_location = end_location;
        builder.append(new DecimalFormat("0.00 ").format(distance));
        builder.append(spannableString);
        distance_counter.setText(builder);

    }

    private void ToggleTheme() {
        final SharedPreferences LastSelectedItem = getApplicationContext().getSharedPreferences("PriorSelected", Context.MODE_PRIVATE);
        int LastSelection = LastSelectedItem.getInt("LastSelection", 0);
        if (LastSelection == 1) {
            LightTheme();

        }
    }

    private void LightTheme() {
        overlayscreen.setBackgroundResource(R.drawable.light_theme);
        homebutton.setBackgroundResource(R.drawable.light_theme_buttons);
        play_button.setBackgroundResource(R.drawable.light_theme_buttons);
        pause_button.setBackgroundResource(R.drawable.light_theme_buttons);
        stop_btn.setBackgroundResource(R.drawable.light_theme_buttons);
    }

    private void StartCountDown() {
        countDownTimer = new CountDownTimer(TimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                homebutton.setEnabled(false);
                homebutton.setBackgroundResource(R.drawable.disabled_button_resource);

                play_button.setEnabled(false);
                play_button.setBackgroundResource(R.drawable.disabled_button_resource);

                pause_button.setEnabled(false);
                pause_button.setBackgroundResource(R.drawable.disabled_button_resource);

                stop_btn.setEnabled(false);
                stop_btn.setBackgroundResource(R.drawable.disabled_button_resource);


                TimeLeft = millisUntilFinished;
                CountDownViewUpdate();

            }


            @Override
            public void onFinish() {
                active = false;
                AutoStartActivity();
                CountDownTimerView.setVisibility(View.GONE);
                ToggleTheme();

                homebutton.setEnabled(true);
                homebutton.setBackgroundResource(R.drawable.resume_button);

                play_button.setEnabled(true);
                play_button.setBackgroundResource(R.drawable.resume_button);

                pause_button.setEnabled(true);
                pause_button.setBackgroundResource(R.drawable.resume_button);

                stop_btn.setEnabled(true);
                stop_btn.setBackgroundResource(R.drawable.resume_button);


            }
        }.start();
    }


    private void CountDownViewUpdate() {
        int min = (int) (TimeLeft / 1000) / 60;
        int sec = (int) (TimeLeft / 1000) % 60;

        String TimeLeftFormat = String.format(Locale.getDefault(), "%02d:%02d", min, sec);

        CountDownTimerView.setText(TimeLeftFormat);

    }

    private void AutoStartActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            play_button.performContextClick();
        }
        View playButton = findViewById(R.id.play_button);
        playButton.performClick();

    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

