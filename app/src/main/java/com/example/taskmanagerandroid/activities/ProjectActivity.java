package com.example.taskmanagerandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.CategoryCollectionAdapter;
import com.example.taskmanagerandroid.viewmodels.ProjectViewModel;


public class ProjectActivity extends AbstractActivity {
    public static final int RESULT_TASK_UPDATED = 102;
    public static final int RESULT_TASK_DELETED = 102;
    private static final String TAG = "ProjectActivity";

    private ActivityResultLauncher<Intent> launchSomeActivity;
    private CategoryCollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        int project_id = getIntent().getIntExtra("project_id", -1);
        Log.v("project_id", String.valueOf(project_id));
        if (project_id < 0) {
            Log.e(TAG, "no project id");
            finish();
            return;
        }

        ViewPager2 mViewPager = findViewById(R.id.viewpager);
        mAdapter = new CategoryCollectionAdapter(this, project_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager.setAdapter(mAdapter);

        ProjectViewModel projectModel = new ViewModelProvider(this, new ProjectViewModel.Factory(getApplication(), project_id)).get(ProjectViewModel.class);
        projectModel.getProject().observe(this, project -> getSupportActionBar().setTitle(project.getTitle()));

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_TASK_UPDATED) {
                        Toast.makeText(this, "the task has been updated successfully.", Toast.LENGTH_SHORT).show();
                        mAdapter.reloadCategoryViewModel();
                    }

                    if (result.getResultCode() == RESULT_TASK_DELETED) {
                        Toast.makeText(this, "the task has been deleted successfully.", Toast.LENGTH_SHORT).show();
                        mAdapter.reloadCategoryViewModel();
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public ActivityResultLauncher<Intent> getLaunchSomeActivity() {
        return launchSomeActivity;
    }
}