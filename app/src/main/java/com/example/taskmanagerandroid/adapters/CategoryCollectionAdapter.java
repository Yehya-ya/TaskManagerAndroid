package com.example.taskmanagerandroid.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskmanagerandroid.fragments.CategoryFragment;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.viewmodels.CategoryCollectionViewModel;

import java.util.List;

public class CategoryCollectionAdapter extends FragmentStateAdapter {

    private List<Category> mCategories;

    public CategoryCollectionAdapter(FragmentActivity fragment, int projectId) {
        super(fragment);
        CategoryCollectionViewModel.Factory factory = new CategoryCollectionViewModel.Factory(
                fragment.getApplication(),
                projectId
        );

        final CategoryCollectionViewModel model = new ViewModelProvider(fragment, factory).get(CategoryCollectionViewModel.class);
        model.getCategories().observe(fragment, categories -> {
            mCategories = categories;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new CategoryFragment(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    public String getItemTitle(int position) {
        return mCategories == null ? "test" : mCategories.get(position).getTitle();
    }
}
