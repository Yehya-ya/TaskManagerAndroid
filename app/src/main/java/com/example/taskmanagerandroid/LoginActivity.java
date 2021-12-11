package com.example.taskmanagerandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private TextView emailView;
    private TextView passwordView;

    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        this.emailView = findViewById(R.id.textViewEmail);
        this.passwordView = findViewById(R.id.textViewPassword);


        this.email = findViewById(R.id.editTextEmailAddress);
        this.password = findViewById(R.id.editTextPassword);


        this.emailView.setVisibility(View.GONE);
        this.passwordView.setVisibility(View.GONE);


        this.email.setOnFocusChangeListener((view, b) -> {
            if (b) {
                emailView.setVisibility(View.VISIBLE);
            } else {
                emailView.setVisibility(View.GONE);
            }
        });

        this.password.setOnFocusChangeListener((view, b) -> {
            if (b) {
                passwordView.setVisibility(View.VISIBLE);
            } else {
                passwordView.setVisibility(View.GONE);
            }
        });

        TextView createNewAccount = findViewById(R.id.createNewAccount);

        createNewAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });


        this.queue = Volley.newRequestQueue(getApplicationContext());
    }

    public void login(View view) {
        StringRequest request = new StringRequest(Request.Method.POST, Route.getLoginRoute(),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("token")) {
                            String token = object.getString("token");
                            token = token.substring(2);
                            SharedPreferences.Editor editor = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
                            editor.putString("access_token", token);
                            editor.apply();

                            startActivity(new Intent(this, MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    final int statusCode = error.networkResponse.statusCode;
                    Log.v("Login", new String(error.networkResponse.data, StandardCharsets.UTF_8));
                    if (500 <= statusCode) {
                        return;
                    }

                    if (300 <= statusCode && statusCode < 400) {
                        return;
                    }

                    try {
                        JSONObject body = new JSONObject(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                        try {
                            JSONObject errors = body.getJSONObject("errors");
                            boolean hasEmail = errors.has("email");
                            boolean hasPassword = errors.has("password");
                            Log.v("Login", errors.toString());
                            if (hasEmail) {
                                Log.v("Login", String.valueOf(errors.getJSONArray("email")));
                            }
                            if (hasPassword) {
                                Log.v("Login", String.valueOf(errors.getJSONArray("password")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", "" + email.getText());
                params.put("password", "" + password.getText());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Connection", "keep-alive");
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        queue.add(request);
    }
}

