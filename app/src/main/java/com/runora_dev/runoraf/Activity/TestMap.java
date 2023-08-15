package com.runora_dev.runoraf.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.runora_dev.runoraf.R;

public class TestMap extends AppCompatActivity {
    private MapView mapView;
    private Button playButton;
    private Button finishButton;
    private MapboxMap mapboxMap;
    private boolean isTracking = false;
    private double startLatitude = 25.276987; // Replace with actual start latitude
    private double startLongitude = 55.296249; // Replace with actual start longitude
    private CountDownTimer timer;
    private long startTime;
    private long elapsedTime;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001; // Replace with your desired value
    private Location startLocation;
    private double totalDistanceMeters = 0;
    private double caloriesBurned = 0;
    private double previousLatitude = 0;
    private double previousLongitude = 0;
    private double currentSpeedKmph = 0;
    private double currentSpeedMph = 0;
    private static final long UPDATE_INTERVAL = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token)); // Replace with your Mapbox access token
        setContentView(R.layout.activity_test_map);

        mapView = findViewById(R.id.testmapView);
        playButton = findViewById(R.id.playButton);
        finishButton = findViewById(R.id.finishButton);

        mapView.onCreate(savedInstanceState);

        finishButton.setOnClickListener(view -> captureLocation());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                TestMap.this.mapboxMap = mapboxMap;
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
                        ActivityCompat.requestPermissions(TestMap.this,
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
                                        totalDistanceMeters += distance;
                                        previousLatitude = newLatitude;
                                        previousLongitude = newLongitude;

                                        // Calculate speed in km/h and mph
                                        long timeElapsedMillis = System.currentTimeMillis() - startTime;
                                        double timeElapsedHours = timeElapsedMillis / (1000.0 * 60 * 60);
                                        currentSpeedKmph = totalDistanceMeters / timeElapsedHours;
                                        currentSpeedMph = totalDistanceMeters / (timeElapsedHours * 1.60934);

                                        // Calculate calories burned (you need an appropriate formula for this)
                                        // caloriesBurned = ...;

                                        // Update UI elements with calculated values
                                        updateElapsedTimeUI(timeElapsedMillis);
                                        updateDistanceUI(totalDistanceMeters);
                                        updateSpeedUI(currentSpeedKmph, currentSpeedMph);
                                        updateCaloriesUI(caloriesBurned);
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


                playButton.setOnClickListener(view -> startTracking());
                finishButton.setOnClickListener(view -> captureLocation());
            }

        });
    }

    private void updateSpeedUI(double kmph, double mph) {
        TextView speedKmphTextView = findViewById(R.id.speedKmphTextView);
        TextView speedMphTextView = findViewById(R.id.speedMphTextView);
        speedKmphTextView.setText(String.format("%.2f km/h", kmph));
        speedMphTextView.setText(String.format("%.2f mph", mph));
    }

    private void updateCaloriesUI(double calories) {
        TextView caloriesTextView = findViewById(R.id.caloriesTextView);
        caloriesTextView.setText(String.format("%.2f calories", calories));
    }

    private void updateDistanceUI(double distanceMeters) {
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        distanceTextView.setText(String.format("%.2f km", distanceMeters / 1000));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with using the feature
                // For example, start location tracking or access LocationComponent
                // ...
            } else {
                // Permission denied, handle accordingly (show a message, disable the feature, etc.)
            }
        }
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

    private void startTracking() {
        // Start location tracking logic
        isTracking = true;

        // Capture the start time
        startTime = System.currentTimeMillis();

        // Request location updates using LocationEngine
        // Update map with location changes
// Capture the start location
        startLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
        // Start the timer
        startTimer();

        // Update the camera position to the current location
        if (startLocation != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(startLocation.getLatitude(), startLocation.getLongitude()), 14));
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime = System.currentTimeMillis() - startTime;
                updateElapsedTimeUI(elapsedTime);
                updateValues();

            }

            @Override
            public void onFinish() {
                // Timer finished (this might not happen with Long.MAX_VALUE)
            }
        };
        timer.start();
    }


    private void updateValues() {
        Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
        if (lastLocation != null) {
            double newLatitude = lastLocation.getLatitude();
            double newLongitude = lastLocation.getLongitude();

            // Calculate distance between current and previous locations
            double distance = calculateDistance(previousLatitude, previousLongitude, newLatitude, newLongitude);

            // Calculate time taken to travel between two consecutive locations
            long timeElapsedMillis = System.currentTimeMillis() - startTime;
            double timeElapsedHours = timeElapsedMillis / (1000.0 * 60 * 60);

            // Calculate speed in km/h and mph based on distance and time
            currentSpeedKmph = distance / timeElapsedHours;
            currentSpeedMph = currentSpeedKmph / 1.60934;

            // Update previous location for next calculation
            previousLatitude = newLatitude;
            previousLongitude = newLongitude;

            // Update UI elements with calculated values
            updateElapsedTimeUI(timeElapsedMillis);
            updateDistanceUI(totalDistanceMeters);
            updateSpeedUI(currentSpeedKmph, currentSpeedMph);
            updateCaloriesUI(caloriesBurned);
        }
    }
    private void updateElapsedTimeUI(long timeInMillis) {
        TextView elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String elapsedTimeText = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        elapsedTimeTextView.setText(elapsedTimeText);
    }


    private void captureLocation() {
        if (isTracking) {
            // Capture the end location
            Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
            if (lastLocation != null) {
                double endLatitude = lastLocation.getLatitude();
                double endLongitude = lastLocation.getLongitude();

                double distance = calculateDistance(startLocation.getLatitude(), startLocation.getLongitude(), endLatitude, endLongitude);
                // Display the calculated distance
                TextView distanceTextView = findViewById(R.id.distanceTextView);
                distanceTextView.setText(String.format("%.2f km", distance));

                // Calculate time elapsed
                long timeElapsedMillis = System.currentTimeMillis() - startTime;

                // Calculate calories burned based on distance and time
                calculateCaloriesBurned(distance, timeElapsedMillis);
                // Update UI elements with calculated values
                updateDistanceUI(totalDistanceMeters);
                updateCaloriesUI(caloriesBurned);
            }
                // Stop the timer
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                isTracking = false;
            }
        }

    // Calculate calories burned based on distance and time
    private void calculateCaloriesBurned(double distanceKm, long timeElapsedMillis) {
        // Constants for calorie estimation (replace with appropriate values)
        double caloriesPerKm = 30.0; // Calories burned per kilometer
        double caloriesPerHour = 3600000.0; // Calories burned per hour

        // Calculate calories burned
        double calories = (caloriesPerKm * distanceKm) + ((timeElapsedMillis / caloriesPerHour) * caloriesPerHour);

        // Update UI with calculated calories
        updateCaloriesUI(calories);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}