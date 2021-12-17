package com.example.taskmanagerandroid.projects;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.ProjectAdapter;
import com.example.taskmanagerandroid.fragments.NewProjectDialogFragment;
import com.example.taskmanagerandroid.viewmodels.ProjectCollectionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProjectAllActivity extends AppCompatActivity {

    private static final String TAG = "ProjectAllActivity";

    private RecyclerView projectsView;
    private ProjectAdapter projectAdapter;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all);

        projectsView = findViewById(R.id.projectRecycler);
        projectAdapter = new ProjectAdapter(this);
        projectsView.setAdapter(projectAdapter);
        projectsView.setLayoutManager(new LinearLayoutManager(this));

        ProjectCollectionViewModel projectsModel = new ViewModelProvider(this).get(ProjectCollectionViewModel.class);

        projectsModel.getProjects().observe(this, projects -> {
            projectAdapter.setProjects(projects);
        });

        button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(view -> {
            NewProjectDialogFragment dialogFragment = new NewProjectDialogFragment();

            dialogFragment.setActionListener(success -> {
                if (success) {
                    projectsModel.createProject(dialogFragment.getTitle(), dialogFragment.getDescription(), success1 -> {
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