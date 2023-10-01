package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private List<String> selectedItems = new ArrayList<>();
    private int totalBill = 0;
    private TextView selectedItemText;

    private Map<String, Double> itemPrices = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().hide();
        itemPrices.put("Sprout bhel", 40.0);
        itemPrices.put("Sprout bhel (Jain)", 40.0);
        itemPrices.put("Fruit Bowl", 60.0);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(OrderActivity.this, "Please login to place an order", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

        selectedItemText = findViewById(R.id.selectedItemText);



        CheckBox sproutsCheckbox = findViewById(R.id.sproutsCheckbox);
        CheckBox sproutsmixCheckbox = findViewById(R.id.sproutsmixCheckbox);
        CheckBox fruitsmallCheckbox = findViewById(R.id.fruitsmallCheckbox);

        sproutsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedItemsAndTotalBill("Sprout bhel", isChecked, 40));
        sproutsmixCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedItemsAndTotalBill("Sprout bhel (Jain)", isChecked, 40));
        fruitsmallCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedItemsAndTotalBill("Fruit Bowl", isChecked, 60));

        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(view -> placeOrderInFirebase());
    }

    private void updateSelectedItemsAndTotalBill(String item, boolean isSelected, int itemPrice) {
        if (isSelected) {
            selectedItems.add(item);
            totalBill += itemPrice;
        } else {
            selectedItems.remove(item);
            totalBill -= itemPrice;
        }

        updateTotalBillText();
    }

    private void updateTotalBillText() {
        selectedItemText.setText("Total Bill: Rs. " + totalBill);
    }

    private void placeOrderInFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userReference = databaseReference.child(currentUser.getUid());

            userReference.child("selectedItems").setValue(selectedItems); // Update selected items

            double calculatedTotalBill = calculateTotalBill(selectedItems); // Calculate total bill
            userReference.child("total_bill").setValue(calculatedTotalBill); // Update total bill in the database

            // Navigate to Date_time_of_delivery activity
            Intent intent = new Intent(OrderActivity.this, Date_time_of_delivery.class);
            intent.putStringArrayListExtra("selectedItems", (ArrayList<String>) selectedItems);
            intent.putExtra("totalBill", calculatedTotalBill); // Use calculated total bill
            startActivity(intent);
        }
    }

    private double calculateTotalBill(List<String> selectedItems) {
        double totalBill = 0.0;
        for (String item : selectedItems) {
            if (itemPrices.containsKey(item)) {
                totalBill += itemPrices.get(item);
            }
        }
        return totalBill;
    }
}
