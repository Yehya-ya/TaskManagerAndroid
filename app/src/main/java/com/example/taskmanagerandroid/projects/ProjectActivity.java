package com.example.taskmanagerandroid.projects;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.CategoryCollectionAdapter;
import com.example.taskmanagerandroid.viewmodels.ProjectViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ProjectActivity extends AppCompatActivity {

    private static final String TAG = "ProjectActivity";

    private CategoryCollectionAdapter adapter;
    private Toolbar mToolbar;
    private TextView mTitle;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;

    private int project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle = mToolbar.findViewById(R.id.toolbar_title);

        project_id = getIntent().getIntExtra("project_id", -1);

        ProjectViewModel projectModel = new ViewModelProvider(this, new ProjectViewModel.Factory(getApplication(), project_id)).get(ProjectViewModel.class);
        projectModel.getProject().observe(this, project -> {
            mTitle.setText(project.getTitle());
        });

        adapter = new CategoryCollectionAdapter(this, project_id);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);

        TabLayoutMediator mediator = new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            tab.setText(adapter.getItemTitle(position));
        });
        mediator.attach();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}