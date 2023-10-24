package com.example.ead_mobile_application;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.ead_mobile_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar appBar;
    private ImageView profilePictureImageView;
    private Button editButton;
    private Button deactivateButton;

    private OkHttpClient client = new OkHttpClient();
    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView nicTextView;
    private TextView mobileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        appBar = findViewById(R.id.appBar);
        profilePictureImageView = findViewById(R.id.profilePictureImageView);
        nameTextView = findViewById(R.id.nameTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        nicTextView = findViewById(R.id.nicTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        editButton = findViewById(R.id.editButton);
        deactivateButton = findViewById(R.id.deactivateButton);

        // You can set click listeners for buttons here and perform actions when they are clicked.
        editButton.setOnClickListener(view -> {
            // Handle edit button click
        });

        deactivateButton.setOnClickListener(view -> {

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            // Retrieving values from SharedPreferences
            String UserId = sharedPreferences.getString("userId", null);
            // Handle deactivate button click
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deactivation")
                    .setMessage("Are you sure you want to deactivate your account?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle account deactivation
                            deactivateAccount(UserId);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Make the network request to fetch user's details for textviews
        fetchDataFromService();
    }
    private void fetchDataFromService() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Retrieving values from SharedPreferences
        String UserId = sharedPreferences.getString("userId", null);

        String url = "http://172.28.3.223/TRMSTest/api/travelers/"+UserId;

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

                        // Retrieve data from the JSON response
                        final String name = data.getString("name");
                        final String username = data.getString("username");
                        final String nic = data.getString("nic");
                        final String mobile = data.getString("mobile");

                        // Update the TextViews on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nameTextView.setText(name);
                                usernameTextView.setText(username);
                                nicTextView.setText(nic);
                                mobileTextView.setText(mobile);
                            }
                        });
                    } catch (JSONException | IOException e) {
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
    }

    private void deactivateAccount(String userId) {
        String url = "http://172.28.3.223/TRMSTest/api/travelers/" + userId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    // Account deactivated successfully
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // You can display a message to the user or navigate to a different screen.
                            // For example, you might want to show a toast message.
                            Toast.makeText(ProfileActivity.this, "Account deactivated.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case when the request is not successful
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
            }
        });
    }

}