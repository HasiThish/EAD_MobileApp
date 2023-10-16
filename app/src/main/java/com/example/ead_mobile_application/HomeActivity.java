package com.example.ead_mobile_application;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    EditText startEditText, endEditText, stationEditText;
    Button searchButton;
    TextView dataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize references to UI elements
        startEditText = findViewById(R.id.startEditText);
        endEditText = findViewById(R.id.endEditText);
        stationEditText = findViewById(R.id.stationEditText);
        searchButton = findViewById(R.id.searchButton);

        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String start = startEditText.getText().toString();
                String end = endEditText.getText().toString();
                String station = stationEditText.getText().toString();

                // Implement logic to search data from MongoDB based on input values
                // Update the dataTextView with the retrieved data
                // dataTextView.setText("Data from MongoDB goes here");
            }
        });
    }
}