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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private TextView mNameView;
    private TextView mEmailView;
    private TextView mPasswordView;
    private TextView mConfirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mNameView = findViewById(R.id.textViewUsername);
        this.mEmailView = findViewById(R.id.textViewEmail);
        this.mPasswordView = findViewById(R.id.textViewPassword);
        this.mConfirmPasswordView = findViewById(R.id.textViewPasswordConfirmation);

        this.mName = findViewById(R.id.editTextUsername);
        this.mEmail = findViewById(R.id.editTextEmailAddress);
        this.mPassword = findViewById(R.id.editTextPassword);
        this.mConfirmPassword = findViewById(R.id.editTextPasswordConfirmation);

        this.mNameView.setVisibility(View.GONE);
        this.mEmailView.setVisibility(View.GONE);
        this.mPasswordView.setVisibility(View.GONE);
        this.mConfirmPasswordView.setVisibility(View.GONE);

        this.mName.setOnFocusChangeListener((view, b) -> {
            if (b) {
                mNameView.setVisibility(View.VISIBLE);
            } else {
                mNameView.setVisibility(View.GONE);
            }
        });

        this.mEmail.setOnFocusChangeListener((view, b) -> {
            if (b) {
                mEmailView.setVisibility(View.VISIBLE);
            } else {
                mEmailView.setVisibility(View.GONE);
            }
        });

        this.mPassword.setOnFocusChangeListener((view, b) -> {
            if (b) {
                mPasswordView.setVisibility(View.VISIBLE);
            } else {
                mPasswordView.setVisibility(View.GONE);
            }
        });

        this.mConfirmPassword.setOnFocusChangeListener((view, b) -> {
            if (b) {
                mConfirmPasswordView.setVisibility(View.VISIBLE);
            } else {
                mConfirmPasswordView.setVisibility(View.GONE);
            }
        });

        TextView haveAccount = findViewById(R.id.haveAccount);

        haveAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    public void register(View view) {
        AccountUtils.register(
                getApplication(),
                mName.getText().toString(),
                mEmail.getText().toString(),
                mPassword.getText().toString(),
                mConfirmPassword.getText().toString(),
                success -> {
                    if (success) {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplication(), "could not register please try again later.", Toast.LENGTH_SHORT).show();
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