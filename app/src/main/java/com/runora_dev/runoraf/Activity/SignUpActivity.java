package com.runora_dev.runoraf.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.databinding.ActivitySignupBinding;


public class SignUpActivity extends AppCompatActivity {
 ActivitySignupBinding binding;
 FirebaseAuth firebaseAuth;
 ProgressDialog progressDialog;
FirebaseFirestore firebaseFirestore;
    private static final String TAG = "SecondActivity";
    EditText  name, email, password, rePassword, phoneNumber, height, weight, age;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword,inputLayoutRePassword,inputLayoutPhone,
            input_hight, input_weight, input_age;
    String FullName, Email, Password, RePassword, PhoneNumber, Height, Weight, Age;
    CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d (TAG, "onCreate: ");
          binding = ActivitySignupBinding.inflate(getLayoutInflater());
        firebaseAuth = firebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
          setContentView(binding.getRoot());
          progressDialog = new ProgressDialog(this);
        LinearLayout btn = findViewById(R.id.btn);
        TextView login = findViewById(R.id.login);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutRePassword = (TextInputLayout) findViewById(R.id.input_layout_Repassword);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        input_hight = (TextInputLayout) findViewById(R.id.input_height);
        input_weight = (TextInputLayout) findViewById(R.id.input_weghit);
        input_age = (TextInputLayout) findViewById(R.id.input_age);

        name = (EditText) findViewById(R.id.reg_name);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_pass);
        rePassword = (EditText) findViewById(R.id.reg_repass);
        phoneNumber = (EditText) findViewById(R.id.reg_Phone);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
        checkbox = findViewById(R.id.checkbox);
        btn.getBackground().setAlpha(64);
        btn.setEnabled(false);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    btn.getBackground().setAlpha(200);
                    btn.setEnabled(true);
                }else if (!isChecked){
                    btn.getBackground().setAlpha(64);
                    btn.setEnabled(false);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        binding.btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FullName = name.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();
                RePassword = rePassword.getText().toString();
                PhoneNumber = phoneNumber.getText().toString();
                Height = height.getText().toString();
                Weight = weight.getText().toString();
                Age = age.getText().toString();
               if (FullName.length() == 0)
                {
                    name.requestFocus();
                    name.setError("Field Cannot Be Empty, Please Fill The Name Field");
                } else  if (Email.length() == 0)
                {
                    email.requestFocus();
                    email.setError("Field Cannot Be Empty, Please Fill The Email Field");
                } else  if (Password.length() == 0)
                {
                    password.requestFocus();
                    password.setError("Field Cannot Be Empty, Please Fill The Password Field");
                }
               else  if (height.length() == 0)
                {
                    height.requestFocus();
                    height.setError("Field Cannot Be Empty, Please Fill The Height Field");
                }else  if (weight.length() == 0)
               {
                   weight.requestFocus();
                   weight.setError("Field Cannot Be Empty, Please Fill The Weight Field");
               }else  if (age.length() == 0)
               {
                   age.requestFocus();
                   age.setError("Field Cannot Be Empty, Please Fill The Age Field");
               }
               else  if (phoneNumber.length() == 0)
               {
                   phoneNumber.requestFocus();
                   phoneNumber.setError("Field Cannot Be Empty, Please Fill The Phone Number Field");
               }else {
      //SignUp
                   String name = binding.regName.getText().toString();
                   String email = binding.regEmail.getText().toString().trim();
                   String password = binding.regRepass.getText().toString();
                   String phone = binding.regPhone.getText().toString();
                   String age = binding.age.getText().toString();
                   String height = binding.height.getText().toString();
                   String weight = binding.weight.getText().toString();
                   progressDialog.show();
                   firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {
                           startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
              progressDialog.cancel();
                        firebaseFirestore.collection("user").document(FirebaseAuth.getInstance().getUid())
                                .set(new UserModel(name,email,phone,age,height,weight));
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {

Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
progressDialog.cancel();
                       }
                   });
              }
            }


        });

    }

    private Boolean Validationform(String FullName) {
      if (FullName.length() == 0)
      {
          name.requestFocus();
          name.setError("Feild Cannot Be Empty, Please Fill The Name Feild");
          return false;
      } else
        return true;
    }
    // Validating form

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validateRePassword()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateWeight()) {
            return;
        }
        if (!validateHeight()) {
            return;
        }
        if (!validateAge()) {
            return;
        }
        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateHeight() {
        if (height.getText().toString().trim().isEmpty()) {
            input_hight.setError(getString(R.string.err_msg_height));
            requestFocus(height);
            return false;
        } else {
            input_hight.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateWeight() {
        if (weight.getText().toString().trim().isEmpty()) {
            input_weight.setError(getString(R.string.err_msg_weight));
            requestFocus(weight);
            return false;
        } else {
            input_weight.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateAge() {
        if (age.getText().toString().trim().isEmpty()) {
            input_age.setError(getString(R.string.err_msg_age));
            requestFocus(age);
            return false;
        } else {
            input_age.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateEmail() {
         Email = email.getText().toString().trim();

        if (Email.isEmpty()) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else if (!isValidEmail(Email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        Password = password.getText().toString().trim();
        RePassword = rePassword.getText().toString().trim();
        if ((Password.isEmpty())){
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        }
       else             if ( password.getText().toString().length() <= 3) {
                inputLayoutPassword.setError(getString(R.string.err_msg_password2));

            requestFocus(password);
            return false;}
        else if (!RePassword.equals(Password)){
            inputLayoutPassword.setError(getString(R.string.err_msg_Repassword));
            requestFocus(password);
            return false; }



      else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
   private boolean validateRePassword() {
   Password = password.getText().toString().trim();
   RePassword = rePassword.getText().toString().trim();
        if ((RePassword.isEmpty())) {

            inputLayoutRePassword.setError(getString(R.string.err_msg_password));
            requestFocus(rePassword);
            return false;
        }
            else if (!RePassword.equals(Password)){
                inputLayoutRePassword.setError(getString(R.string.err_msg_Repassword));
                requestFocus(rePassword);
                return false; }
         else {
            inputLayoutRePassword.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePhone() {
        if (phoneNumber.getText().toString().trim().isEmpty()) {
            inputLayoutPhone.setError(getString(R.string.err_msg_Phone));
            requestFocus(phoneNumber);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.reg_name:
                    validateName();
                    break;
                    case R.id.height:
                    validateHeight();
                    break;
                case R.id.weight:
                    validateWeight();
                    break;
                case R.id.age:
                    validateAge();
                    break;
                case R.id.reg_email:
                    validateEmail();
                    break;
                case R.id.reg_pass:
                    validatePassword();
                    break;
           /*     case R.id.reg_repass:
                    validatePhone();
                    break;*/
                case R.id.reg_Phone:
                    validatePhone();
                    break;

            }
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

}
