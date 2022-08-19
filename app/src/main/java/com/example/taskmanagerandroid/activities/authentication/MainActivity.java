package com.example.taskmanagerandroid.activities.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.taskmanagerandroid.activities.HomeActivity;
import com.example.taskmanagerandroid.utils.AccountUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int mode = sharedPreferences.getInt("mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (mode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        AccountUtils.verifyToken(getApplication(), success -> {
            if (success) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        });
    }
}

