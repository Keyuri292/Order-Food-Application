package com.example.firebaseconnection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity {

    private EditText editTextusername, editTextpassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");
        editTextusername = findViewById(R.id.usernameLog);
        editTextpassword = findViewById(R.id.passwordLog);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        //LoginButton
        Button loginbutton1 = findViewById(R.id.loginbtn1);
        loginbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textusername = editTextusername.getText().toString();
                String textpassword = editTextpassword.getText().toString();

                if (TextUtils.isEmpty(textusername)) {
                    Toast.makeText(SigninActivity.this, "Please enter emailid", Toast.LENGTH_LONG).show();
                    editTextusername.setError("Emailid is required");
                    editTextusername.requestFocus();
                } else if (TextUtils.isEmpty(textpassword)) {
                    Toast.makeText(SigninActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    editTextusername.setError("Password is required");
                    editTextusername.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    // Attempt to log in the user
                    loginUser(textusername, textpassword);
                }
            }
        });

    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SigninActivity.this, "You are logged in now!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SigninActivity.this, OrderActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity after login
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SigninActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void forgotPasswordClick(View view) {
        // Show a dialog or navigate to a new activity for password reset
        // For example, you can use an AlertDialog to get the user's email
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Enter your email");
        builder.setView(input);

        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();

                // Send a password reset email to the user
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Password reset email sent successfully
                                    Toast.makeText(SigninActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Password reset email sending failed
                                    Toast.makeText(SigninActivity.this, "Password reset email sending failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}