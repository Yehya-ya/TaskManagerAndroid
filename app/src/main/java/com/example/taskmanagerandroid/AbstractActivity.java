package com.example.taskmanagerandroid;

import android.content.Intent;
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

import com.example.taskmanagerandroid.utils.AccountUtils;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.card.MaterialCardView;

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
        switch (item.getItemId()) {
            case R.id.menu_profile:
                View profile = findViewById(R.id.menu_profile);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.popup_account, null);
                PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

                MaterialLetterIcon icon = view.findViewById(R.id.materialLetterIcon);
                icon.setLetter(AccountUtils.getUser().getName());
                ((TextView) view.findViewById(R.id.userName)).setText(AccountUtils.getUser().getName());
                ((TextView) view.findViewById(R.id.userEmail)).setText(AccountUtils.getUser().getEmail());

                ((MaterialCardView) view.findViewById(R.id.popup_logout)).setOnClickListener(view1 -> {
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
