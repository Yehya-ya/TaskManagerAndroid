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
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.utils.ActionListener;

public class EditCategoryDialogFragment extends DialogFragment {
    private final Category mCategory;
    private EditText mTitle;
    private ActionListener listener;

    public EditCategoryDialogFragment(Category category) {
        this.mCategory = category;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_project_dialog, null);
        this.mTitle = view.findViewById(R.id.editProjectFragment_title);
        this.mTitle.setText(mCategory.getTitle());
        view.findViewById(R.id.description).setVisibility(View.GONE);

        builder.setView(view)
                .setTitle("Edit The Category")
                .setPositiveButton("Update", (dialogInterface, i) -> listener.action(true))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getDescription() {
        return null;
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }
}
