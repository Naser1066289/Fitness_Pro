package com.runora_dev.runoraf.Activity;


import static com.runora_dev.runoraf.Webservice.DatabaseHelper.name;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.runora_dev.runoraf.R;

import org.checkerframework.checker.nullness.qual.NonNull;


public class ProfileActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView phoneTextView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView profile_back_btn;
    String userDataName, userDataEmail, userDataPhone;
private  LinearLayout editButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        profile_back_btn = (ImageView) findViewById(R.id.profile_back_btn);

        profile_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Home.class);
                 startActivity(intent);
            }
        });
        editButton = (LinearLayout) findViewById(R.id.btn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("name", userDataName);
                intent.putExtra("email", userDataEmail);
                intent.putExtra("phone", userDataPhone);
                startActivity(intent);
            }
        });
        userNameTextView = findViewById(R.id.nameTextView);
        userEmailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        Log.d("ProfileActivity : ", "User data "+user.toString() );

        if (user != null) {
            Log.d("ProfileActivity : ", "User is not null"+user.toString() );

            String userEmail = user.getEmail();
            String uid = user.getUid();
            userEmailTextView.setText(userEmail);


            DocumentReference userDocRef = firebaseFirestore.collection("user").document(uid);
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("ProfileActivity : ", "task is successfull "+task.isSuccessful() );

                        DocumentSnapshot document = task.getResult();
                        Log.d("ProfileActivity : ", "document data "+document );

                        if (document.exists()) {
                            Log.d("ProfileActivity : ", "document data exist "+document.toString() );

                            String name = document.getString("name");
                            String height = document.getString("height");
                            String weight = document.getString("weight");
                            String age = document.getString("age");
                            String email = document.getString("email");
                            String phone = document.getString("phone");
                            userDataName = name;
                            userDataEmail = email;
                            userDataPhone = phone;

                            userNameTextView.setText(name);
                            userEmailTextView.setText(email);
//                            userEmailTextView.setText(email);

                            // Display additional info as needed
                            String additionalInfo = "Age: " + age + "\nWeight: " + weight + "\nHeight: " + height;
                            phoneTextView.setText(phone);
                        }
                    } else {
                        Log.d("ProfileActivity", "Error getting user document: ", task.getException());
                    }
                }
            });
        }
    }

}
