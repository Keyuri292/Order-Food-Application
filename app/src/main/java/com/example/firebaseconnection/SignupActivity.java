package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class SignupActivity extends AppCompatActivity {

    private EditText editTextusername, editTextmobileno, editTextpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setTitle("Signup");
        Toast.makeText(SignupActivity.this,"You can register now!", Toast.LENGTH_LONG).show();

        editTextusername = findViewById(R.id.username);
        editTextmobileno = findViewById(R.id.mobileno);
        editTextpassword = findViewById(R.id.password);

        Button buttonregister = findViewById(R.id.registerbtn);
        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textemailid = editTextusername.getText().toString();
                String textmobileno = editTextmobileno.getText().toString();
                String textpassword = editTextpassword.getText().toString();

                if(TextUtils.isEmpty(textemailid))
                {
                    Toast.makeText(SignupActivity.this, "Please enter emailid.", Toast.LENGTH_LONG).show();
                    editTextusername.setError("Emailid is required");
                    editTextusername.requestFocus();
                }
                else if(TextUtils.isEmpty(textmobileno))
                {
                    Toast.makeText(SignupActivity.this, "Please enter mobileno.", Toast.LENGTH_LONG).show();
                    editTextmobileno.setError("Mobile Number is required");
                    editTextmobileno.requestFocus();
                }
                else if(TextUtils.isEmpty(textpassword))
                {
                    Toast.makeText(SignupActivity.this, "Please enter password.", Toast.LENGTH_LONG).show();
                    editTextpassword.setError("Password is required");
                    editTextpassword.requestFocus();
                }
                else
                {
                    registerUser(textemailid,textmobileno,textpassword);
                }
            }
        });
        Button loginbutton = findViewById(R.id.loginbtn);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registerUser(String textemailid, String textmobileno, String textpassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(textemailid, textpassword).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    // Create a User object using the provided details
                    List<String> selectedItems = new ArrayList<>(); // Initialize selectedItems as needed
                    User newUser = new User(textemailid, textmobileno, textpassword, selectedItems, "", "", 0);

                    // Save the User object to the database
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(SignupActivity.this, "Signed up successfully! You will get a verification link in email.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Signed up failed!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Registration failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
