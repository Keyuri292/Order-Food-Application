package com.example.firebaseconnection;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboard extends AppCompatActivity {

    private LinearLayout usersLayout;
    private DatabaseReference registeredUsersRef;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        usersLayout = findViewById(R.id.usersLayout);
        registeredUsersRef = FirebaseDatabase.getInstance().getReference("Registered Users");
        handler = new Handler();

        // Initialize a Runnable to refresh the data every 1 minute
        final Runnable refreshDataRunnable = new Runnable() {
            @Override
            public void run() {
                refreshData();
                handler.postDelayed(this, 60000); // Refresh every 1 minute
            }
        };

        // Start the initial data refresh
        handler.post(refreshDataRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any callbacks when the activity is destroyed to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }

    private void refreshData() {
        usersLayout.removeAllViews(); // Remove all previous data

        registeredUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    displayUserDetails(userSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }



    private void displayUserDetails(DataSnapshot userSnapshot) {



        String email = userSnapshot.child("emailId").getValue(String.class);
        String mobileNo = userSnapshot.child("mobileNo").getValue(String.class);
        String date = userSnapshot.child("date").getValue(String.class);
        String time = userSnapshot.child("time").getValue(String.class);

        StringBuilder userDetailsBuilder = new StringBuilder();
        userDetailsBuilder.append("Email: ").append(email).append("\n");
        userDetailsBuilder.append("Mobile No: ").append(mobileNo).append("\n");
        userDetailsBuilder.append("Date: ").append(date).append("\n");
        userDetailsBuilder.append("Time: ").append(time).append("\n");

        for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
            if (orderSnapshot.child("selectedItems").exists()) {
                userDetailsBuilder.append("Ordered Items:\n");
                for (DataSnapshot itemSnapshot : orderSnapshot.child("selectedItems").getChildren()) {
                    String item = itemSnapshot.getValue(String.class);
                    userDetailsBuilder.append("- ").append(item).append("\n");
                }

                int totalBill = orderSnapshot.child("total_bill").getValue(Integer.class);
                userDetailsBuilder.append("Total Bill: ").append(totalBill).append("\n");
            }
        }

        TextView userDetailsTextView = new TextView(this);
        userDetailsTextView.setText(userDetailsBuilder.toString());
        usersLayout.addView(userDetailsTextView);

        Button deliveredButton = new Button(this);
        deliveredButton.setText("To be Delivered");

        deliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the displayed order details from the layout
                usersLayout.removeView(userDetailsTextView);
                usersLayout.removeView(deliveredButton);

                SharedPreferences preferences = getSharedPreferences("OrderStatus", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(userSnapshot.getKey(), true); // Use the user's key as the order identifier
                editor.apply();
            }
        });
        SharedPreferences preferences = getSharedPreferences("OrderStatus", MODE_PRIVATE);
        boolean isDelivered = preferences.getBoolean(userSnapshot.getKey(), false); // Use the user's key as the order identifier

        // Adjust the UI based on the "isDelivered" status
        if (isDelivered) {
            deliveredButton.setText("Delivered");
            deliveredButton.setTextColor(Color.GRAY);
        } else {
            deliveredButton.setText("To be Delivered");
        }


        usersLayout.addView(deliveredButton);
    }

}
