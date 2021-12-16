package com.example.taskmanagerandroid.projects;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.CategoryCollectionAdapter;
import com.example.taskmanagerandroid.models.Category;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.LinkedList;
import java.util.List;


public class ProjectActivity extends AppCompatActivity {

    private static final String TAG = "ProjectActivity";

    private CategoryCollectionAdapter adapter;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;

    private List<Category> mCategories;

    private int project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        mCategories = new LinkedList<>();
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        project_id = getIntent().getIntExtra("project_id", -1);

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