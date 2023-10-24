package com.example.ead_mobile_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class TrainDetailsActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String TrainNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);

        // Retrieve the id from the intent's extras
        String trainId = getIntent().getStringExtra("train_id");
        Log.d("trainnnnnnnnnnn", trainId);
        // Get references to UI elements
        TextView trainNumberTextView = findViewById(R.id.trainNumberTextView);
        TextView trainNameTextView = findViewById(R.id.trainNameTextView);
        TextView departureStationTextView = findViewById(R.id.departureStationTextView);

        TableLayout scheduleTable = findViewById(R.id.scheduleTable);

        Button bookingButton = findViewById(R.id.bookButton);

        String url = "http://172.28.3.223/TRMSTest/api/Trains/" + trainId;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject data = new JSONObject(responseBody);

                        // Use runOnUiThread to update the UI on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Retrieve data from the JSON response
                                    trainNumberTextView.setText("Train Number: " + data.getString("trainNumber"));
                                    trainNameTextView.setText("Train Name: " + data.getString("name"));
                                    departureStationTextView.setText("Departure Station: " + data.getString("departureStation"));

                                    TrainNumber = data.getString("trainNumber");

                                    // Populate train schedule
                                    JSONArray scheduleArray = data.getJSONArray("schedule");
                                    for (int i = 0; i < scheduleArray.length(); i++) {
                                        JSONObject scheduleObject = scheduleArray.getJSONObject(i);
                                        String station = scheduleObject.getString("station");
                                        String arrivalTime = scheduleObject.getString("arrivalTime");
                                        String departureTime = scheduleObject.getString("departureTime");
                                        String stopTime = scheduleObject.getString("stopTime");

                                        addScheduleRow(scheduleTable, station, arrivalTime, departureTime, stopTime);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case when the request is not successful
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
            }
        });

        bookingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create an Intent to start TrainDetailsActivity
                Intent intent = new Intent(TrainDetailsActivity.this, ReservationActivity.class);
                // Pass the ID to TrainDetailsActivity
                intent.putExtra("train_id", TrainNumber);
                startActivity(intent);
            }
        });
    }

    // Helper method to add a row to the schedule table
    private void addScheduleRow(TableLayout scheduleTable, String station, String arrivalTime, String departureTime, String stopTime) {
        TableRow row = new TableRow(this);

        row.setBackgroundResource(R.drawable.row_border);

        TextView stationTextView = createScheduleTextView(station);
        TextView arrivalTimeTextView = createScheduleTextView(arrivalTime);
        TextView departureTimeTextView = createScheduleTextView(departureTime);
        TextView stopTimeTextView = createScheduleTextView(stopTime);

        row.addView(stationTextView);
        row.addView(arrivalTimeTextView);
        row.addView(departureTimeTextView);
        row.addView(stopTimeTextView);

        scheduleTable.addView(row);
    }

    // Helper method to create a TextView for the schedule table
    private TextView createScheduleTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Set layout parameters to distribute columns evenly
        return textView;
    }
}