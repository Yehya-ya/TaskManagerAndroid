package com.example.taskmanagerandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.TaskViewAdapter;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.viewmodels.TaskCollectionViewModel;

public class CategoryFragment extends Fragment {

    private final Category mCategory;
    private TaskCollectionViewModel mTaskModel;

    private TaskViewAdapter mTaskViewAdapter;

    public CategoryFragment(Category category) {
        this.mCategory = category;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTaskModel = new ViewModelProvider(
                this,
                new TaskCollectionViewModel.Factory(
                        getActivity().getApplication(),
                        mCategory.getProjectId(),
                        mCategory.getId()
                )
        ).get(TaskCollectionViewModel.class);

        this.mTaskViewAdapter = new TaskViewAdapter(getActivity(), success -> {
            NewTaskDialogFragment dialogFragment = new NewTaskDialogFragment();
            dialogFragment.setActionListener(success1 -> {
                mTaskModel.createTask(dialogFragment.getTitle(), dialogFragment.getDescription(), dialogFragment.getDate(), success2 -> {
                    if (success2) {
                        Toast.makeText(getActivity(), "Task has been created successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            dialogFragment.show(getParentFragmentManager(), "newTask");
        });

        mTaskModel.getTasks().observe(getActivity(), tasks -> {
            mTaskViewAdapter.setTasks(tasks);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mRecyclerView = view.findViewById(R.id.task_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mTaskViewAdapter);

        TextView mTitle = view.findViewById(R.id.category_title);
        mTitle.setText(mCategory.getTitle());
    }
}