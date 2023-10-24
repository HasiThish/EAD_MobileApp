package com.example.ead_mobile_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    EditText startEditText, endEditText;
    Button searchButton;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize references to UI elements
        startEditText = findViewById(R.id.startEditText);
        endEditText = findViewById(R.id.endEditText);
        searchButton = findViewById(R.id.searchButton);
        tableLayout = findViewById(R.id.trainTable);

        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String start = startEditText.getText().toString();
                String end = endEditText.getText().toString();
                //String URL = "http://192.168.1.101/TRMSTest/api/Trains/searchedtrains";
                String URL = "http://172.28.3.223/TRMSTest/api/Trains/searchedtrains";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Create a JSON request body
                        String json = "{\"startStation\":\"" + start + "\",\"endStation\":\"" + end + "\"}";
                        RequestBody loginDetails = RequestBody.create(JSON, json);

                        Request request = new Request.Builder()
                                .url(URL)
                                .post(loginDetails)
                                .build();

                        OkHttpClient client = new OkHttpClient();

                        Call call = client.newCall(request);
                        Response response = null;

                        try {
                            response = call.execute();
                            String serverResponse = response.body().string();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Log the server response
                                    Log.d("ServerResponse", serverResponse);

                                    try {
                                        JSONArray jsonArray = new JSONArray(serverResponse);

                                        // Clear the existing rows in the table
                                        tableLayout.removeAllViews();

                                        // Create a header row
                                        TableRow headerRow = new TableRow(HomeActivity.this);
                                        TextView trainNumberHeading = createTextView("Train Number", true,null);
                                        TextView trainNameHeading = createTextView("Train Name", true, null);
                                        TextView stationHeading = createTextView("Start - End Station", true, null);
                                        headerRow.addView(trainNumberHeading);
                                        headerRow.addView(trainNameHeading);
                                        headerRow.addView(stationHeading);
                                        tableLayout.addView(headerRow);

                                        // Loop through the JSON array to create table rows
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject trainObject = jsonArray.getJSONObject(i);

                                            String trainNumber = trainObject.getString("trainNumber");
                                            String trainName = trainObject.getString("name");
                                            String startStation = trainObject.getString("departureStation");
                                            String endStation = trainObject.getString("arrivalStation");
                                            String id = trainObject.getString("id");

                                            // Create a new row for the table
                                            TableRow dataRow = new TableRow(HomeActivity.this);
                                            TextView trainNumberTextView = createTextView(trainNumber, false, id);
                                            TextView trainNameTextView = createTextView(trainName, false, id);
                                            TextView stationTextView = createTextView(startStation + " - " + endStation, false, id);

                                            dataRow.addView(trainNumberTextView);
                                            dataRow.addView(trainNameTextView);
                                            dataRow.addView(stationTextView);
                                            tableLayout.addView(dataRow);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });
    }

    // Helper method to create a TextView with custom attributes
    private TextView createTextView(String text, boolean isHeader, String id) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setPadding(8, 8, 8, 8);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        if (isHeader) {
            textView.setBackgroundColor(getResources().getColor(R.color.headerBackgroundColor));
            textView.setTextColor(getResources().getColor(android.R.color.white));
        }else {
            // Set a click listener to navigate to TrainDetailsActivity
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to start TrainDetailsActivity
                    Intent intent = new Intent(HomeActivity.this, TrainDetailsActivity.class);
                    // Pass the ID to TrainDetailsActivity
                    intent.putExtra("train_id", id);
                    startActivity(intent);
                }
            });
        }

        return textView;
    }
}