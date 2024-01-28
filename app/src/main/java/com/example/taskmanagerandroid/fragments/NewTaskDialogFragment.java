package com.example.taskmanagerandroid.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.utils.ActionListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTaskDialogFragment extends DialogFragment {

    private EditText mTitle;
    private EditText mDescription;
    private Button mDateButton;
    private ActionListener listener;
    private Calendar mDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_task_dialog, null);
        this.mTitle = view.findViewById(R.id.editTaskFragment_title);
        this.mDescription = view.findViewById(R.id.editTaskFragment_description);
        this.mDateButton = view.findViewById(R.id.buttonTaskFragment_date);

        this.mDateButton.setOnClickListener(view1 -> {

            if (mDate == null) {
                mDate = Calendar.getInstance();
            }
            DatePickerDialog datePickerFragment = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                mDate.set(i, i1, i2);
                mDateButton.setText(getFormattedEndAt());
            }, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH));
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DATE, 1);
            datePickerFragment.getDatePicker().setMinDate(tomorrow.getTimeInMillis());
            datePickerFragment.show();
        });

        builder.setView(view).setTitle("Add New Task").setPositiveButton("Create", (dialogInterface, i) -> listener.action(true)).setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getDescription() {
        return mDescription.getText().toString();
    }

    public String getFormattedEndAt() {
        if (mDate == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        return formatter.format(new Date(mDate.getTimeInMillis()));
    }

    public String getFormattedForServerEndAt() {
        if (mDate == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");
        return formatter.format(new Date(mDate.getTimeInMillis()));
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }
}