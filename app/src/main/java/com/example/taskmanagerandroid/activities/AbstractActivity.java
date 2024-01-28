package com.example.taskmanagerandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.activities.authentication.LoginActivity;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.switchmaterial.SwitchMaterial;

public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        AccountUtils.verifyToken(getApplication(), success -> {
            if (!success) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {
            View profile = findViewById(R.id.menu_profile);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.popup_account, null);
            PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

            MaterialLetterIcon icon = view.findViewById(R.id.materialLetterIcon);
            icon.setLetter(AccountUtils.getUser().getName());
            ((TextView) view.findViewById(R.id.userName)).setText(AccountUtils.getUser().getName());
            ((TextView) view.findViewById(R.id.userEmail)).setText(AccountUtils.getUser().getEmail());

            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

            boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

            SwitchMaterial switchMaterial = view.findViewById(R.id.popup_switch);
            switchMaterial.setChecked(isNightMode);
            switchMaterial.setOnClickListener(view1 -> {
                popupWindow.dismiss();
                SharedPreferences.Editor editor = getApplication().getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit();
                if (switchMaterial.isChecked()) {
                    editor.putInt("mode", AppCompatDelegate.MODE_NIGHT_YES);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    editor.putInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            });

            (view.findViewById(R.id.popup_logout)).setOnClickListener(view1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(AbstractActivity.this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("LogOut", (dialogInterface, i) -> {
                            AccountUtils.logout(getApplication(), success -> {
                                if (success) {
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                }
                            });
                            dialogInterface.dismiss();
                            popupWindow.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            popupWindow.dismiss();

                        })
                        .setIcon(R.drawable.ic_round_warning_24)
                        .show();
            });

            popupWindow.showAsDropDown(profile);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
