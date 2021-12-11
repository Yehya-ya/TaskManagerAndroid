package com.example.taskmanagerandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String access_token = sharedPref.getString("access_token", "");
        Log.v(TAG, "access token: " + access_token);
        if (TextUtils.isEmpty(access_token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            MyRequest request = new MyRequest();
            request.setMethod(Request.Method.POST);
            request.setUrl(Route.getTokenVerifyRoute());
            request.setResponse(response -> {

            });

            request.setErrorHandler(new MyRequest.ErrorHandler() {
                @Override
                public void action() {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });

            MyRequest verifyRequest = new MyRequest();
            verifyRequest.setMethod(Request.Method.POST);
            verifyRequest.setUrl(Route.getTokenVerifyRoute());
            verifyRequest.addHeader("Authorization", "Bearer " + access_token);
            verifyRequest.setResponse(response -> {
                Log.v(TAG, response);
            });
            verifyRequest.setErrorHandler(new MyRequest.ErrorHandler() {
                @Override
                public void action() {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });

            MyRequestQueue.getInstance(this).addToRequestQueue(verifyRequest);
        }
    }
}

