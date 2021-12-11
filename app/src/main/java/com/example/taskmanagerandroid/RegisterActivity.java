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

import com.android.volley.Request;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    private TextView usernameView;
    private TextView emailView;
    private TextView passwordView;
    private TextView confirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        this.username.setOnFocusChangeListener((view, b) -> {
            if (b) {
                usernameView.setVisibility(View.VISIBLE);
            } else {
                usernameView.setVisibility(View.GONE);
            }
        });

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

        this.confirmPassword.setOnFocusChangeListener((view, b) -> {
            if (b) {
                confirmPasswordView.setVisibility(View.VISIBLE);
            } else {
                confirmPasswordView.setVisibility(View.GONE);
            }
        });

        TextView haveAccount = findViewById(R.id.haveAccount);

        haveAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void register(View view) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getRegisterRoute());
        request.addParam("name", username.getText().toString());
        request.addParam("email", email.getText().toString());
        request.addParam("password", password.getText().toString());
        request.addParam("password_confirmation", confirmPassword.getText().toString());

        request.setResponse(response -> {
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
        });

        request.setErrorHandler(
                new MyRequest.ErrorHandler() {
                    @Override
                    public void handlingErrors(JSONObject errors) {
                        try {
                            boolean hasName = errors.has("name");
                            boolean hasEmail = errors.has("email");
                            boolean hasPassword = errors.has("password");
                            if (hasName) {
                                Log.v(TAG, String.valueOf(errors.getJSONArray("name")));
                            }
                            if (hasEmail) {
                                Log.v(TAG, String.valueOf(errors.getJSONArray("email")));
                            }
                            if (hasPassword) {
                                Log.v(TAG, String.valueOf(errors.getJSONArray("password")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}