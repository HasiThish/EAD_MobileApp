package com.example.ead_mobile_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReservationActivity extends AppCompatActivity {

    EditText trainNumber, nicText, dateText, seatsText;

    Button reserveButton, dateTimePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Button checkButton = findViewById(R.id.checkButton);
        CheckBox checkBox = findViewById(R.id.checkBox);
        reserveButton = findViewById(R.id.reserveButton);
        trainNumber = findViewById(R.id.fromEditText);
        nicText = findViewById(R.id.toEditText);
        dateText = findViewById(R.id.dateEditText);
        seatsText = findViewById(R.id.toAddSeats);
        dateTimePickerButton = findViewById(R.id.date_time_picker_button);
        // Retrieve the id from the intent's extras
        String trainId = getIntent().getStringExtra("train_id");

        // Retrieving values from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String UserNic = sharedPreferences.getString("userNic", null);
        Log.d("UserNic", UserNic);

        nicText.setText(UserNic);
        trainNumber.setText(trainId);

        // Set an OnClickListener for the Check Button
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Set an OnClickListener for the Reserve Button
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve data from input fields
                String train = trainNumber.getText().toString();
                String nic = nicText.getText().toString();
                String date = dateText.getText().toString();
                String seats = seatsText.getText().toString();
                //String URL = "http://192.168.1.101/TRMSTest/api/bookings";
                String URL = "http://172.28.3.223/TRMSTest/api/travelers";

                // Define the input date format
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date dateText2 = inputFormat.parse(date); // Parse the content of dateText
                    // Define the desired output date format
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = outputFormat.format(dateText2);

                    // Now, formattedDate contains the date in the desired format
                    Log.d("date", formattedDate);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Create a JSON request body
                            JsonObject json = new JsonObject();
                            json.addProperty("bookingId", "");
                            json.addProperty("trainNumber", train);
                            json.addProperty("travelNIC", nic);
                            json.addProperty("date", formattedDate);
                            json.addProperty("seatCount", seats);

                            // Create a RequestBody
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody bookingDetails = RequestBody.create(JSON, json.toString());
                            Log.d("user details", bookingDetails.toString());
                            Request request = new Request.Builder()
                                    .url(URL)
                                    .post(bookingDetails)
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

                                        // Show a success message using Toast
                                        Toast.makeText(ReservationActivity.this, "Booking Done", Toast.LENGTH_SHORT).show();

                                        // Navigate to the home interface
                                        Intent homeIntent = new Intent(ReservationActivity.this, HistoryActivity.class);
                                        startActivity(homeIntent);
                                    }
                                });

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();

                } catch (ParseException e) {
                    // Handle the parsing error, e.g., show an error message or log it
                    e.printStackTrace();
                }
            }
        });



        // Set an OnClickListener for the date/time picker button
        dateTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a date picker dialog
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // The selected date is set in the 'year', 'month', and 'dayOfMonth' variables
                // You can use these values to display the selected date in the dateEditText
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth; // Example format
                dateText.setText(selectedDate);
            }
        }, year, month, day); // Initial date values (year, month, day)
        datePickerDialog.show();
    }


}