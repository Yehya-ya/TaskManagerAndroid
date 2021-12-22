package com.example.taskmanagerandroid.activities.authentication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mEmail;
    private EditText mPassword;

    private TextInputLayout mEmailLayout;
    private TextInputLayout mPasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mEmailLayout = findViewById(R.id.textViewEmail);
        this.mPasswordLayout = findViewById(R.id.textViewPassword);

        this.mEmail = findViewById(R.id.editTextEmailAddress);
        this.mPassword = findViewById(R.id.editTextPassword);

        TextView createNewAccount = findViewById(R.id.createNewAccount);

        createNewAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
        this.mEmail.requestFocus();
    }

    public void login(View view) {
        AccountUtils.login(
                getApplication(),
                mEmail.getText().toString(),
                mPassword.getText().toString(),
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
                    public void handlingErrors(JSONObject errors) {
                        if (errors.has("email")) {
                            String error = "";
                            try {
                                JSONArray array = errors.getJSONArray("email");
                                for (int i = 0; i < array.length(); i++) {
                                    error += "* " + array.getString(i) + "\n";
                                }
                                mEmailLayout.setError(error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (errors.has("password")) {
                            String error = "";
                            try {
                                JSONArray array = errors.getJSONArray("password");
                                for (int i = 0; i < array.length(); i++) {
                                    error += "* " + array.getString(i) + "\n";
                                }
                                mPasswordLayout.setError(error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void handlingMessage(String massage) {
                        Toast.makeText(getApplication(), massage, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}

