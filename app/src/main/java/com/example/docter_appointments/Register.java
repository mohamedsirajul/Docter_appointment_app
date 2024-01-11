package com.example.docter_appointments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://docterappointmentss-default-rtdb.firebaseio.com/");

        final EditText fullname = findViewById(R.id.fullname);
        final EditText email = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText conPassword = findViewById(R.id.conPassword);
        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MobilePattern = "[0-9]{10}";

                final String fullnameTxt = fullname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();

                if (fullnameTxt.isEmpty() || phoneTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || conPasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!phoneTxt.matches(MobilePattern)) {
                    Toast.makeText(Register.this, "Mobile must be 10 digits", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new User object
                    User user = new User(fullnameTxt, emailTxt, passwordTxt, phoneTxt);

                    // Store the user data in Firebase
                    databaseReference.child("users").child(phoneTxt).setValue(user);

                    Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class User {
        private String fullname;
        private String email;
        private String password;
        private String mobile;

        public User() {
            // Default constructor required for Firebase
        }

        public User(String fullname, String email, String password, String mobile) {
            this.fullname = fullname;
            this.email = email;
            this.password = password;
            this.mobile = mobile;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
