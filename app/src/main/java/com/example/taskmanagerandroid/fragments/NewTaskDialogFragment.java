package com.example.taskmanagerandroid.fragments;


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

import java.util.Calendar;
import java.util.TimeZone;

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

        mDateButton.setOnClickListener(view1 -> {

            if (mDate == null) {
                mDate = Calendar.getInstance();
            }
            DatePickerDialog datePickerFragment = new DatePickerDialog(
                    getContext(),
                    (datePicker, i, i1, i2) -> {
                        mDate.set(i, i1, i2);
                        mDateButton.setText(i + "/" + (i1 + 1) + "/" + i2);
                    },
                    mDate.get(Calendar.YEAR),
                    mDate.get(Calendar.MONTH),
                    mDate.get(Calendar.DAY_OF_MONTH)
            );
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 1);
            datePickerFragment.getDatePicker().setMinDate(date.getTimeInMillis());
            datePickerFragment.show();
        });

        builder.setView(view)
                .setTitle("Add New Task")
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
        return mDescription.getText().toString();
    }

    public String getDate() {
        if (mDate == null) {
            return null;
        }
        Calendar temp = (Calendar) mDate.clone();
        temp.setTimeZone(TimeZone.getTimeZone("UTC"));
        return String.format("%1$s-%2$s-%3$sT00:00:00.000000Z",
                mDate.get(Calendar.YEAR),
                mDate.get(Calendar.MONTH) + 1,
                mDate.get(Calendar.DAY_OF_MONTH)
        );
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }
}