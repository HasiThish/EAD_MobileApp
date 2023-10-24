package com.example.ead_mobile_application;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Retrieving values from SharedPreferences
        String UserNIC = sharedPreferences.getString("userNic", null);

        String url = "http://172.28.3.223/TRMSTest/api/bookings/traveller/"+UserNIC;

        Request request = new Request.Builder()
                .url(url)
                .build();

        String jsonString = "Your JSON data here"; // Replace with your JSON data

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final String responseBody = response.body().string();

                        // Parse the JSON data from the response
                        JSONArray bookingList = new JSONArray(responseBody);

                        // Get the LinearLayout where you want to add the card views
                        final LinearLayout historyLinearLayout = findViewById(R.id.historyLinearLayout);

                        // Run UI operations on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Iterate through the bookingList JSON array
                                    for (int i = 0; i < bookingList.length(); i++) {
                                        JSONObject booking = bookingList.getJSONObject(i);

                                        // Extract data from the JSON response
                                        String bookingId = booking.getString("bookingId");
                                        String departureStation = booking.getString("departureStation");
                                        String arrivalStation = booking.getString("arrivalStation");

                                        // Create a new CardView for each booking
                                        CardView cardView = new CardView(HistoryActivity.this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.card_margin));
                                        cardView.setLayoutParams(layoutParams);

                                        // Create the layout for the card content
                                        LinearLayout cardContent = new LinearLayout(HistoryActivity.this);
                                        cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        ));
                                        cardContent.setOrientation(LinearLayout.VERTICAL);

                                        // Add content to the card (e.g., TextViews)
                                        TextView bookingIdTextView = new TextView(HistoryActivity.this);
                                        bookingIdTextView.setText("Booking ID: " + bookingId);

                                        TextView routeTextView = new TextView(HistoryActivity.this);
                                        routeTextView.setText("Route: " + departureStation + " - " + arrivalStation);

                                        // Add TextViews to the card's content layout
                                        cardContent.addView(bookingIdTextView);
                                        cardContent.addView(routeTextView);

                                        // Add the content layout to the CardView
                                        cardView.addView(cardContent);

                                        // Add the CardView to the LinearLayout
                                        historyLinearLayout.addView(cardView);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

        });
    }
}