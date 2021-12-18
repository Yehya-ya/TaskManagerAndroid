package com.example.taskmanagerandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerandroid.projects.ProjectAllActivity;
import com.example.taskmanagerandroid.utils.AccountUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountUtils.login(getApplication(), success -> {
            if (success) {
                startActivity(new Intent(this, ProjectAllActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        });
    }
}

