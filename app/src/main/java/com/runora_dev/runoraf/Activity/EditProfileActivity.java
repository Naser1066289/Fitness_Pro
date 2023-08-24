package com.runora_dev.runoraf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.runora_dev.runoraf.R;

import org.checkerframework.checker.nullness.qual.NonNull;

public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button saveButton;

    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    nameEditText = findViewById(R.id.nameEditTextEmail);
    emailEditText = findViewById(R.id.emailTextEmail);
    phoneEditText = findViewById(R.id.editTextPhoneNumber);
    saveButton = findViewById(R.id.saveProfile);
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");

        nameEditText.setText(name);
        emailEditText.setText(email);
        phoneEditText.setText(phone);

    firebaseFirestore = FirebaseFirestore.getInstance();

    // Retrieve user data from previous activity or Firestore

        saveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String newName = nameEditText.getText().toString();
            String newEmail = emailEditText.getText().toString();
            String newPhone = phoneEditText.getText().toString();

            updateProfile(newName, newEmail, newPhone);
        }
    });
}

    private void updateProfile(String newName, String newEmail, String newPhone) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDocRef = firebaseFirestore.collection("user").document(userId);

        userDocRef.update("name", newName, "email", newEmail, "phone", newPhone)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update successful
                            Toast.makeText(EditProfileActivity.this,"Your information updated successfully ",Toast.LENGTH_SHORT).show();
                            finish(); // Finish the activity after successful update
                        } else {
                            // Handle update error
                        }
                    }
                });
    }
}