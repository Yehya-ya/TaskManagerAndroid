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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText email;
    private EditText password;

    private TextView emailView;
    private TextView passwordView;

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
    }

    public void login(View view) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getLoginRoute());
        request.addParam("email", email.getText().toString());
        request.addParam("password", password.getText().toString());

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

        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void handlingErrors(JSONObject errors) {
                try {
                    if (errors.has("email")) {
                        Log.v(TAG, String.valueOf(errors.getJSONArray("email")));
                    }
                    if (errors.has("password")) {
                        Log.v(TAG, String.valueOf(errors.getJSONArray("password")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        MyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}

