package com.example.taskmanagerandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    private TextView usernameView;
    private TextView emailView;
    private TextView passwordView;
    private TextView confirmPasswordView;

    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.usernameView = findViewById(R.id.textViewUsername);
        this.emailView = findViewById(R.id.textViewEmail);
        this.passwordView = findViewById(R.id.textViewPassword);
        this.confirmPasswordView = findViewById(R.id.textViewPasswordConfirmation);

        this.username = findViewById(R.id.editTextUsername);
        this.email = findViewById(R.id.editTextEmailAddress);
        this.password = findViewById(R.id.editTextPassword);
        this.confirmPassword = findViewById(R.id.editTextPasswordConfirmation);

        this.usernameView.setVisibility(View.GONE);
        this.emailView.setVisibility(View.GONE);
        this.passwordView.setVisibility(View.GONE);
        this.confirmPasswordView.setVisibility(View.GONE);

        this.username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    usernameView.setVisibility(View.VISIBLE);
                } else {
                    usernameView.setVisibility(View.GONE);
                }
            }
        });

        this.email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    emailView.setVisibility(View.VISIBLE);
                } else {
                    emailView.setVisibility(View.GONE);
                }
            }
        });

        this.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    passwordView.setVisibility(View.VISIBLE);
                } else {
                    passwordView.setVisibility(View.GONE);
                }
            }
        });

        this.confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    confirmPasswordView.setVisibility(View.VISIBLE);
                } else {
                    confirmPasswordView.setVisibility(View.GONE);
                }
            }
        });

        this.queue = Volley.newRequestQueue(getApplicationContext());
    }

    public void register(View view) {
        StringRequest request = new StringRequest(Request.Method.POST, Route.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("register", object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        final int statusCode = error.networkResponse.statusCode;
                        if (500 <= statusCode) {
                            return;
                        }

                        if (300 <= statusCode && statusCode < 400) {
                            return;
                        }

                        try {
                            JSONObject body = new JSONObject(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                            try {
                                JSONObject errors =  body.getJSONObject("errors");
                                boolean hasName = errors.has("name");
                                boolean hasEmail = errors.has("email");
                                boolean hasPassword = errors.has("password");
                                Log.v("register", errors.toString());
                                if (hasName) {
                                    Log.v("register", String.valueOf(errors.getJSONArray("name")));
                                }
                                if (hasEmail) {
                                    Log.v("register", String.valueOf(errors.getJSONArray("email")));
                                }
                                if (hasPassword) {
                                    Log.v("register", String.valueOf(errors.getJSONArray("password")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "" + username.getText());
                params.put("email", "" + email.getText());
                params.put("password", "" + password.getText());
                params.put("password_confirmation", "" + confirmPassword.getText());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();

                headers.put("Connection", "keep-alive");
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        queue.add(request);
    }
}

