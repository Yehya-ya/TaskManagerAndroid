package com.example.taskmanagerandroid.projects;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanagerandroid.AbstractActivity;
import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.CategoryCollectionAdapter;
import com.example.taskmanagerandroid.viewmodels.ProjectViewModel;


public class ProjectActivity extends AbstractActivity {

    private static final String TAG = "ProjectActivity";

    private CategoryCollectionAdapter mAdapter;
    private Toolbar mToolbar;
    private TextView mTitle;
    private ViewPager2 mViewPager;

    private int project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        project_id = getIntent().getIntExtra("project_id", -1);

        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewpager);
        mTitle = mToolbar.findViewById(R.id.toolbar_title);
        mAdapter = new CategoryCollectionAdapter(this, project_id);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager.setAdapter(mAdapter);

        ProjectViewModel projectModel = new ViewModelProvider(this, new ProjectViewModel.Factory(getApplication(), project_id)).get(ProjectViewModel.class);
        projectModel.getProject().observe(this, project -> {
            mTitle.setText(project.getTitle());
        });
    }
}