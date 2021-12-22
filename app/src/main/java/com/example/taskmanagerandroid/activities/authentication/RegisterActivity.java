package com.example.taskmanagerandroid.activities.authentication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private TextInputLayout mNameView;
    private TextInputLayout mEmailView;
    private TextInputLayout mPasswordView;
    private TextInputLayout mConfirmPasswordView;

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

        setupInputTextLayout(new TextInputLayout[]{this.mNameView}, mName);
        setupInputTextLayout(new TextInputLayout[]{this.mEmailView}, mEmail);
        setupInputTextLayout(new TextInputLayout[]{this.mPasswordView, this.mConfirmPasswordView}, mPassword);
        setupInputTextLayout(new TextInputLayout[]{this.mPasswordView, this.mConfirmPasswordView}, mConfirmPassword);

        TextView haveAccount = findViewById(R.id.haveAccount);

        haveAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        mName.requestFocus();
    }

    public void setupInputTextLayout(TextInputLayout[] textInputLayouts, EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (TextInputLayout l : textInputLayouts) {
                    l.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
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
                    public void handlingErrors(JSONObject errors) {
                        if (errors.has("name")) {
                            String error = "";
                            try {
                                JSONArray array = errors.getJSONArray("name");
                                for (int i = 0; i < array.length(); i++) {
                                    error += "* " + array.getString(i) + "\n";
                                }
                                mNameView.setError(error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (errors.has("email")) {
                            String error = "";
                            try {
                                JSONArray array = errors.getJSONArray("email");
                                for (int i = 0; i < array.length(); i++) {
                                    error += "* " + array.getString(i) + "\n";
                                }
                                mEmailView.setError(error);
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
                                mPasswordView.setError(error);
                                mConfirmPasswordView.setError(" ");
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