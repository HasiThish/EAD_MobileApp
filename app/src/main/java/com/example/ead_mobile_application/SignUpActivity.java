package com.example.ead_mobile_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEditText, usernameEditText, passwordEditText2, nicEditText, mobileEditText;
    private Button signUpButton;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText2 = findViewById(R.id.passwordEditText2);
        nicEditText = findViewById(R.id.nicEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        signUpButton = findViewById(R.id.signInButton);

        // Add an OnClickListener to the SignIn button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve data from input fields
                String name = nameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText2.getText().toString();
                String nic = nicEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();
                //String URL = "http://192.168.1.101/TRMSTest/api/travelers";
                String URL = "http://172.28.3.223/TRMSTest/api/travelers";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Create a JSON request body
                        JsonObject json = new JsonObject();
                        json.addProperty("name", name);
                        json.addProperty("username", username);
                        json.addProperty("password", password);
                        json.addProperty("nic", nic);
                        json.addProperty("mobile", mobile);

                        // Create a RequestBody
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody userDeatils = RequestBody.create(JSON, json.toString());
                        Log.d("user details", userDeatils.toString());
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(userDeatils)
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
                                    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    // Navigate to the home interface
                                    Intent homeIntent = new Intent(SignUpActivity.this, SignInActivity.class);
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
    }

}