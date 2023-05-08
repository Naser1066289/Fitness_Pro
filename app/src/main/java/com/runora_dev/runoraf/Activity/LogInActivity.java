package com.runora_dev.runoraf.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.databinding.ActivityLoginBinding;


/*
uses firebase api to login verification
once it is verified, the data of loggedin user is stored in shared preference.
Sharedpreference provides the fascility to share the data to all the activities in the app.
 */
public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    String name;
    String height;
    String weight;
    String age;
    String PhoneNumber, Password;
    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    private EditText email, password;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private LinearLayout btnSignIn;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate:");

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        TextView login = findViewById(R.id.Text3);
        inputLayoutEmail = findViewById(R.id.input_layout_phone);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        email = findViewById(R.id.lg_email);
        password = findViewById(R.id.lg_password);
        btnSignIn = findViewById(R.id.btn);
        PhoneNumber = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = binding.lgEmail.getText().toString().trim();
                String password = binding.lgPassword.getText().toString();

                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(emailAddress, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                Toast.makeText(LogInActivity.this, "LoggedIn Successfully", Toast.LENGTH_LONG).show();
                                firebaseFirestore.collection("user").document(FirebaseAuth.getInstance().getUid())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                //fetching the personal data of the user
                                                if (!task.isSuccessful()) {
                                                    Log.e("firebase", "Error getting data", task.getException());
                                                } else {

                                                    Log.d("firebase", "" + task.getResult().getData());
                                                    System.out.println(task.getResult().getData());
                                                    name = (String) task.getResult().getData().get("name");
                                                    height = (String) task.getResult().getData().get("height");
                                                    weight = (String) task.getResult().getData().get("weight");
                                                    age = (String) task.getResult().getData().get("age");
                                                    //storing the personal data in shared preferences
                                                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("age", age);
                                                    editor.putString("weight", weight);
                                                    editor.putString("height", height);
                                                    editor.putString("name", name);
                                                    editor.commit();
                                                }
                                            }
                                        });
                                Intent ii = new Intent(LogInActivity.this, Home.class);
                                ii.putExtra("frag", "2");
                                startActivity(ii);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });

        TextView textView = findViewById(R.id.TextView2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        TextView resetPass = findViewById(R.id.Text3);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    /* Validating form */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateEmail() {


        if (email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if ((password.getText().toString().trim().isEmpty()) && (password.getText().toString().length() <= 4)) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.lg_email:
                    validateEmail();
                    break;
                case R.id.lg_password:
                    validatePassword();
                    break;
            }
        }
    }

}