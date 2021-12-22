package com.example.taskmanagerandroid.adapters;

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

import org.jetbrains.annotations.Nullable;

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
                int previousId = 0;
                int nextId = 0;
                if (i > 0) {
                    previousId = categories.get(i - 1).getId();
                }
                if (i < categories.size() - 1) {
                    nextId = categories.get(i + 1).getId();
                }
                mKeyMap.put(i, (long) category.hashCode() + nextId + previousId);
            }
            mKeyMap.put(categories.size(), (long) addCategoryFragment.hashCode());
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

    public int getPosition(Category category) {
        List<Category> categories = mCategoryViewModel.getCategories().getValue();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equals(category)) {
                return i;
            }
        }

        return 0;
    }

    @Nullable
    public Category getCategory(int position) {
        if (position < 0 || this.mCategoryViewModel.getCategories().getValue().size() <= position) {
            return null;
        }
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

    public void reloadCategoryViewModel() {
        this.mCategoryViewModel.loadCategories();
    }
}
