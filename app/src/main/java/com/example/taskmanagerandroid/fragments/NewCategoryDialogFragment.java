package com.example.taskmanagerandroid.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.utils.ActionListener;

public class NewCategoryDialogFragment extends DialogFragment {
    private EditText mTitle;
    private EditText mDescription;
    private ActionListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_project_dialog, null);
        this.mTitle = view.findViewById(R.id.editProjectFragment_title);
        this.mDescription = view.findViewById(R.id.editProjectFragment_description);

        builder.setView(view)
                .setTitle("Add New Category")
                .setPositiveButton("Create", (dialogInterface, i) -> {
                    listener.action(true);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dismiss();
                });

        return builder.create();
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getDescription() {
        return mDescription.getText().toString().equals("") ? null : mDescription.getText().toString();
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

}
