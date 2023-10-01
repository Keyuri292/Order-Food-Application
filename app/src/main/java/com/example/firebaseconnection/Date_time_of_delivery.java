package com.example.firebaseconnection;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Date_time_of_delivery extends AppCompatActivity {

    private EditText dateSlotEditText;
    private Spinner timeSlotSpinner;
    private Button confirmOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_delivery);

        getSupportActionBar().hide();
        dateSlotEditText = findViewById(R.id.dateslot);
        timeSlotSpinner = findViewById(R.id.timeslot_spinner);
        confirmOrderButton = findViewById(R.id.confirmorder);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_slots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = dateSlotEditText.getText().toString();
                String selectedTime = timeSlotSpinner.getSelectedItem().toString();  // Get selected item from Spinner


                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(currentUser.getUid());
                        userReference.child("date").setValue(selectedDate);
                        userReference.child("time").setValue(selectedTime);
                }

                Intent confirmIntent = new Intent(Date_time_of_delivery.this, Notification.class);
                List<String> selectedItems = getIntent().getStringArrayListExtra("selectedItems");
                confirmIntent.putStringArrayListExtra("selectedItems", (ArrayList<String>) selectedItems);
                confirmIntent.putExtra("selectedDate", selectedDate);
                confirmIntent.putExtra("selectedTime", selectedTime);
                startActivity(confirmIntent);

                Toast.makeText(Date_time_of_delivery.this, "Yay!" + "\n Your order has been placed!", Toast.LENGTH_SHORT).show();

                String channelID = "CHANNEL_ID_NOTIFICATION";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
                builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                        .setContentTitle("MyApp")
                        .setContentText("Order Placed!")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("data", "Dear customer, your order is successfully placed!");
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
                    if (notificationChannel == null) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        notificationChannel = new NotificationChannel(channelID, "Some description", importance);
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }
                notificationManager.notify(0, builder.build());
            }
        });
    }
}
