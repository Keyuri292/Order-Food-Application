package com.example.firebaseconnection;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        textView = findViewById(R.id.Success_message);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(currentUser.getUid());
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    DataSnapshot userNode = dataSnapshot.child(currentUser.getUid());

                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Fetch total bill

                        int totalBill = userNode.child("total_bill").getValue(Integer.class);
                        DataSnapshot selectedItemsSnapshot = userNode.child("selectedItems");


                        //DataSnapshot selectedItemsSnapshot = dataSnapshot.child(currentUser.getUid()).child("selectedItems");

                        List<String> selectedItems = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : selectedItemsSnapshot.getChildren()) {
                            String item = itemSnapshot.getValue(String.class);
                            selectedItems.add(item);
                        }

                        String date = user.getDate();
                        String time = user.getTime();

                        StringBuilder orderDetails = new StringBuilder("Ordered Items:\n");

                        if (selectedItems != null && !selectedItems.isEmpty()) {
                            for (String item : selectedItems) {
                                orderDetails.append("- ").append(item).append("\n");
                            }
                        } else {
                            orderDetails.append("No items selected\n");
                        }

                        // Include total bill in the order details
                        orderDetails.append("\nTotal Bill: ").append(totalBill).append("\n");
                        orderDetails.append("Delivery Date: ").append(date).append("\n");
                        orderDetails.append("Delivery Time: ").append(time);

                        textView.setText(orderDetails.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }
}
