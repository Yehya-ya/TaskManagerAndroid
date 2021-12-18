package com.example.taskmanagerandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.CategoryCollectionAdapter;

public class AddCategoryFragment extends Fragment {
    private final CategoryCollectionAdapter mCategoryAdapter;

    public AddCategoryFragment(CategoryCollectionAdapter adapter) {
        this.mCategoryAdapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.buttonAddCategory);
        button.setOnClickListener(view1 -> {
            NewCategoryDialogFragment categoryDialogFragment = new NewCategoryDialogFragment();
            categoryDialogFragment.setActionListener(success -> {
                if (success) {
                    mCategoryAdapter.createCategory(categoryDialogFragment.getTitle(), categoryDialogFragment.getDescription(), success1 -> {
                        if (success1) {
                            Toast.makeText(getContext(), "Category has been created successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            categoryDialogFragment.show(getParentFragmentManager(), "New fragment");
        });
    }
}
