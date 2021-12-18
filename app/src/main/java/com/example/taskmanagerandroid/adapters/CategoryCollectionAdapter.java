package com.example.taskmanagerandroid.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.example.taskmanagerandroid.fragments.AddCategoryFragment;
import com.example.taskmanagerandroid.fragments.CategoryFragment;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.utils.ActionListener;
import com.example.taskmanagerandroid.viewmodels.CategoryCollectionViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryCollectionAdapter extends FragmentStateAdapter {

    private final CategoryCollectionViewModel mCategoryViewModel;
    private final Map<Integer, Long> mKeyMap;
    private final AddCategoryFragment addCategoryFragment;

    public CategoryCollectionAdapter(FragmentActivity fragment, int projectId) {
        super(fragment);
        CategoryCollectionViewModel.Factory factory = new CategoryCollectionViewModel.Factory(
                fragment.getApplication(),
                projectId
        );
        this.mKeyMap = new HashMap<>();
        this.addCategoryFragment = new AddCategoryFragment(this);
        mKeyMap.put(0, (long) addCategoryFragment.hashCode());

        this.mCategoryViewModel = new ViewModelProvider(fragment, factory).get(CategoryCollectionViewModel.class);
        this.mCategoryViewModel.getCategories().observe(fragment, categories -> {
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                mKeyMap.put(i, (long) category.hashCode());
            }
            mKeyMap.put(categories.size(), (long) addCategoryFragment.hashCode());
            Log.v("test", mKeyMap.toString());
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (mCategoryViewModel.getCategories().getValue().size() == position) {
            return addCategoryFragment;
        }
        return new CategoryFragment(this, position);
    }

    @Override
    public long getItemId(int position) {
        return mKeyMap.get(position);
    }

    @Override
    public int getItemCount() {
        return this.mCategoryViewModel.getCategories().getValue().size() + 1;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int getPosition(Long l) {
        Log.v("test", "search for: " + l.longValue());
        for (int i = 0; i < mKeyMap.size(); i++) {
            Log.v("test", "item " + i + ": " + mKeyMap.get(i).longValue());

            if (mKeyMap.get(i).longValue() == l.longValue()) {
                return i;
            }
        }

        return 0;
    }

    public Category getCategory(int position) {
        return this.mCategoryViewModel.getCategories().getValue().get(position);
    }

    public void createCategory(String title, String description, ActionListener listener) {
        this.mCategoryViewModel.createCategory(title, description, listener);
    }

    public void editCategory(int position, String title, String description, ActionListener listener) {
        this.mCategoryViewModel.editCategory(position, title, description, listener);
    }

    public void deleteCategory(int position, ActionListener listener) {
        this.mCategoryViewModel.deleteCategory(position, listener);
    }
}
