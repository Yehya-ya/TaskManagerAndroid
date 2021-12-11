package com.example.taskmanagerandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.taskmanagerandroid.utils.MyRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String access_token = sharedPref.getString("access_token", "");
        Log.v("Main", "access token :" + access_token);
        if (TextUtils.isEmpty(access_token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            StringRequest verifyRequest = new StringRequest(Request.Method.POST, Route.getTokenVerifyRoute(),
                    response -> {
                        Log.v("Main", response);
                    },
                    error -> {
                        Log.v("Main", error.toString());
                    }
            ) {
                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Connection", "keep-alive");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + access_token);

                    return headers;
                }

                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };

            MyRequest.getInstance(this).addToRequestQueue(verifyRequest);
        }
    }
}

