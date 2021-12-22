package com.example.taskmanagerandroid.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.ProjectAdapter;
import com.example.taskmanagerandroid.fragments.NewProjectDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AbstractActivity {
    private RecyclerView projectsView;
    private ProjectAdapter projectAdapter;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(getString(R.string.app_name));

        projectsView = findViewById(R.id.projectRecycler);
        projectAdapter = new ProjectAdapter(this);
        projectsView.setAdapter(projectAdapter);
        projectsView.setLayoutManager(new LinearLayoutManager(this));

        button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(view -> {
            NewProjectDialogFragment dialogFragment = new NewProjectDialogFragment();

            dialogFragment.setActionListener(success -> {
                if (success) {
                    projectAdapter.addProject(dialogFragment.getTitle(), dialogFragment.getDescription(), success1 -> {
                        if (success1) {
                            Toast.makeText(this, "Project has been created successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            dialogFragment.show(getSupportFragmentManager(), "new Project");
        });

        projectsView.setOnScrollChangeListener((view, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY + 12 && button.isShown()) {
                button.hide();
            }

            if (scrollY < oldScrollY - 12 && !button.isShown()) {
                button.show();
            }
        });
    }
}