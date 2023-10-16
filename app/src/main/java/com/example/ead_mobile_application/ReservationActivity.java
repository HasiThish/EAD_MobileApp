package com.example.ead_mobile_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Button checkButton = findViewById(R.id.checkButton);
        CheckBox checkBox = findViewById(R.id.checkBox);
        Button reserveButton = findViewById(R.id.reserveButton);

        // Set an OnClickListener for the Check Button
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, you should implement the logic to check the MongoDB database
                // and automatically tick the CheckBox if relevant data is found.
                // If the relevant data is found, you can enable the Reserve Button.
                // Use checkBox.setChecked(true) to tick the CheckBox.
                // Use reserveButton.setEnabled(true) to enable the Reserve Button.
            }
        });

        // Set an OnClickListener for the Reserve Button
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, you should implement the logic to send the details to MongoDB
                // based on the selected values and the checked CheckBox.
            }
        });
    }
}