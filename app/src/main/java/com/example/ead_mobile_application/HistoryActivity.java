package com.example.ead_mobile_application;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryActivity{

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
//
//        // Initialize the layout for booking history items
//        LinearLayout historyLayout = findViewById(R.id.historyLinearLayout);
//
//        // Populate booking history items dynamically (you need to implement this)
//        // For each booking, create a layout and add it to historyLayout
//        for (Booking booking : getBookingHistoryFromMongoDB()) {
//            View bookingView = getLayoutInflater().inflate(R.layout.booking_history_item, historyLayout, false);
//            TextView fromTextView = bookingView.findViewById(R.id.fromTextView);
//            TextView toTextView = bookingView.findViewById(R.id.toTextView);
//            TextView dateTextView = bookingView.findViewById(R.id.dateTextView);
//            Button cancelButton = bookingView.findViewById(R.id.cancelButton);
//
//            fromTextView.setText(booking.getFrom());
//            toTextView.setText(booking.getTo());
//            dateTextView.setText(booking.getDate());
//
//            // Check if the date is in the past, and disable the cancel button if needed
//            if (isDateInPast(booking.getDate())) {
//                cancelButton.setEnabled(false);
//            }
//
//            cancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Implement the logic to cancel the booking
//                    // You can use the booking data to identify and cancel the booking
//                }
//            });
//
//            historyLayout.addView(bookingView);
//        }
//    }
//
//    // Mock function to get booking history data from MongoDB
//    private List<Booking> getBookingHistoryFromMongoDB() {
//        // Replace this with your actual logic to fetch data from MongoDB
//        // Return a list of Booking objects
//        // For simplicity, you can create a Booking class to represent the data
//        // and return a list of Booking objects here.
//    }
//
//    // Check if a date is in the past
//    private boolean isDateInPast(String date) {
//        // Implement the logic to check if the date is in the past
//        // Return true if it's in the past, false otherwise
//    }
}
