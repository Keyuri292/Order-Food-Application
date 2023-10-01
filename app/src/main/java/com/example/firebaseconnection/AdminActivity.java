package com.example.firebaseconnection;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebaseconnection.AdminDashboard;
import com.google.android.material.button.MaterialButton;

public class AdminActivity extends AppCompatActivity {

    private static final String ADMIN_PASSWORD = "------"; // Change this to the actual admin password

    private EditText passwordAdminEditText;
    private TextView userDetailsTextView; // Correct case here

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().hide();
        passwordAdminEditText = findViewById(R.id.passwordAdmin);
        userDetailsTextView = findViewById(R.id.userdetails); // Correct case here

        MaterialButton loginButton = findViewById(R.id.loginbtn1);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordAdminEditText.getText().toString().trim();
                if (enteredPassword.equals(ADMIN_PASSWORD)) {
                    // Password is correct
                    Toast.makeText(AdminActivity.this, "Logged in as admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminActivity.this, AdminDashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AdminActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
