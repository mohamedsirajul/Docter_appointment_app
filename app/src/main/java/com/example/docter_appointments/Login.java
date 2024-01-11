package com.example.docter_appointments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://docterappointmentss-default-rtdb.firebaseio.com/");

    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView registerNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobilePattern = "[0-9]{10}";

                final String phoneTxt = phoneEditText.getText().toString();
                final String passwordTxt = passwordEditText.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your mobile or password", Toast.LENGTH_SHORT).show();
                } else if (!phoneTxt.matches(mobilePattern)) {
                    Toast.makeText(Login.this, "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneTxt)) {
                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);

                                if (getPassword.equals(passwordTxt)) {
                                    String nameFromDB = snapshot.child(phoneTxt).child("fullname").getValue(String.class);
                                    String emailFromDB = snapshot.child(phoneTxt).child("email").getValue(String.class);
                                    String passwordFromDB = snapshot.child(phoneTxt).child("password").getValue(String.class);
                                    String mobileFromDB = snapshot.child(phoneTxt).child("mobile").getValue(String.class);

                                    Toast.makeText(Login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("name", nameFromDB);
                                    intent.putExtra("email", emailFromDB);
                                    intent.putExtra("password", passwordFromDB);
                                    intent.putExtra("mobile", mobileFromDB);
                                    startActivity(intent);
                                    finish(); // Optional: If you want to finish the Login activity so that it's not accessible by pressing the back button
                                } else {
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}
