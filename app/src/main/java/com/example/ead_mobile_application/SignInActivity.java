package com.example.ead_mobile_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {
    // Define references to UI elements
    EditText nicEditText, passwordEditText;
    Button signInButton, signUpButton;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in); // Set the layout

        // Initialize references to UI elements
        nicEditText = findViewById(R.id.nicEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        // Set click listener for the "Sign In" button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve data from input fields (nicEditText and passwordEditText)
                String username = nicEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                //String URL = "http://192.168.1.101/TRMSTest/api/travelers/login";
                String URL = "http://172.28.3.223/TRMSTest/api/travelers/login";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Create a JSON request body
                        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
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

                                    String userId = "";
                                    String userNic = "";
                                    String userUsername = "";

                                    JSONObject jsonResponse = null;
                                    try {
                                        jsonResponse = new JSONObject(serverResponse);
                                        userId = jsonResponse.getString("id");
                                        userNic = jsonResponse.getString("nic");
                                        userUsername = jsonResponse.getString("username");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }


                                    // Storing values in SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userId", userId);
                                    editor.putString("userNic", userNic);
                                    editor.putString("userUsername", userUsername);
                                    editor.apply();

                                    // Show a success message using Toast
                                    Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                    // Navigate to the home interface
                                    Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                                    startActivity(homeIntent);
                                }
                            });



                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }).start();

            }
        });

        // Set click listener for the "Sign Up" button to navigate to the signup page
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the signup page
                Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}