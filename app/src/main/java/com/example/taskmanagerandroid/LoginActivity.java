package com.example.taskmanagerandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.MyRequest;

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
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
        this.email.requestFocus();
    }

    public void login(View view) {
        AccountUtils.login(
                getApplication(),
                email.getText().toString(),
                password.getText().toString(),
                success -> {
                    if (success) {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Could not login please try later.", Toast.LENGTH_SHORT).show();
                    }
                },
                new MyRequest.ErrorHandler() {
                    @Override
                    public void handlingMessage(String massage) {
                        Toast.makeText(getApplication(), massage, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

