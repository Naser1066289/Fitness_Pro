package com.runora_dev.runoraf.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.runora_dev.runoraf.R;

import org.checkerframework.checker.nullness.qual.NonNull;


public class ProfileActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Get the current user's UID
        String currentUserUid = firebaseAuth.getCurrentUser().getUid();
        ImageView statisticBack = findViewById(R.id.profile_back_btn);
        statisticBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Home.class);
                startActivity(intent);
            }
        });
        // Fetch user data from Firestore based on UID
        firestore.collection("user").document(currentUserUid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");

                            // Set the retrieved data to the UI elements
                            nameTextView.setText(name);
                            emailTextView.setText(email);
                            phoneTextView.setText(phone);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle error
                    }
                });

        LinearLayout editProfile = (LinearLayout) findViewById(R.id.btn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}
