package com.example.taskmanagerandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryFragment extends Fragment {

    private final Category mCategory;
    private TaskCollectionViewModel mTaskModel;

    private TaskViewAdapter mTaskViewAdapter;

    private FloatingActionButton mButton;
    private RecyclerView mRecyclerView;

    public CategoryFragment(Category category) {
        this.mCategory = category;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTaskViewAdapter = new TaskViewAdapter(getActivity());
        this.mTaskModel = new ViewModelProvider(
                this,
                new TaskCollectionViewModel.Factory(
                        getActivity().getApplication(),
                        mCategory.getProjectId(),
                        mCategory.getId()
                )
        ).get(TaskCollectionViewModel.class);

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
        mButton = view.findViewById(R.id.create_new_task);
        mButton.setOnClickListener(view1 -> {
            NewTaskDialogFragment dialogFragment = new NewTaskDialogFragment();
            dialogFragment.setActionListener(success -> {
                mTaskModel.createTask(dialogFragment.getTitle(), dialogFragment.getDescription(), dialogFragment.getDate(), success2 -> {
                    if (success2) {
                        Toast.makeText(getActivity(), "Task has been created successfully.", Toast.LENGTH_LONG).show();
                    }
                });
            });
            dialogFragment.show(getParentFragmentManager(), "newTask");
        });

        mRecyclerView = view.findViewById(R.id.task_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mTaskViewAdapter);
        mRecyclerView.setOnScrollChangeListener((view1, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && mButton.isShown()) {
                mButton.hide();
            }

            if (scrollY < oldScrollY && !mButton.isShown()) {
                mButton.show();
            }
        });
    }
}