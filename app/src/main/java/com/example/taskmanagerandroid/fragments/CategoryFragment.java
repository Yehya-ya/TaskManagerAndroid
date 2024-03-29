package com.example.taskmanagerandroid.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.CategoryCollectionAdapter;
import com.example.taskmanagerandroid.adapters.TaskViewAdapter;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.viewmodels.TaskCollectionViewModel;

public class CategoryFragment extends Fragment {
    private final CategoryCollectionAdapter mCategoryAdapter;
    private final Category mCategory;
    private final int mNextCategory;
    private final int mPreviousCategory;
    private TaskCollectionViewModel mTaskModel;
    private TaskViewAdapter mTaskViewAdapter;

    public CategoryFragment(CategoryCollectionAdapter adapter, int position) {
        this.mCategoryAdapter = adapter;
        this.mCategory = mCategoryAdapter.getCategory(position);
        this.mNextCategory = mCategoryAdapter.getCategory(position + 1) == null ? 0 : mCategoryAdapter.getCategory(position + 1).getId();
        this.mPreviousCategory = mCategoryAdapter.getCategory(position - 1) == null ? 0 : mCategoryAdapter.getCategory(position - 1).getId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTaskModel = new ViewModelProvider(this, new TaskCollectionViewModel.Factory(getActivity().getApplication(), mCategory.getProjectId(), mCategory.getId())).get(TaskCollectionViewModel.class);

        this.mTaskViewAdapter = new TaskViewAdapter(this, success -> {
            NewTaskDialogFragment dialogFragment = new NewTaskDialogFragment();
            dialogFragment.setActionListener(success1 -> mTaskModel.createTask(dialogFragment.getTitle(), dialogFragment.getDescription(), dialogFragment.getFormattedForServerEndAt(), success2 -> {
                if (success2) {
                    Toast.makeText(getActivity(), "Task has been created successfully.", Toast.LENGTH_SHORT).show();
                }
            }));
            dialogFragment.show(getParentFragmentManager(), "newTask");
        }, mPreviousCategory != 0, mNextCategory != 0);

        mTaskModel.getTasks().observe(getActivity(), tasks -> mTaskViewAdapter.setTasks(tasks));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mRecyclerView = view.findViewById(R.id.task_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mTaskViewAdapter);

        TextView mTitle = view.findViewById(R.id.category_title);
        mTitle.setText(mCategory.getTitle());

        ImageView menu = view.findViewById(R.id.menu);
        menu.setOnClickListener(this::showMenu);
    }

    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.project_card_menu, popupMenu.getMenu());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_edit) {
                EditCategoryDialogFragment fragment = new EditCategoryDialogFragment(mCategory);
                fragment.setActionListener(success -> {
                    if (success) {
                        mCategoryAdapter.editCategory(mCategoryAdapter.getPosition(mCategory), fragment.getTitle(), fragment.getDescription(), success1 -> {
                            Toast.makeText(getActivity(), "Category has been updated successfully.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
                fragment.show(getParentFragmentManager(), "edit fragment");
            } else if (itemId == R.id.menu_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert!!!").setMessage("Are you sure you want to delete this category?").setPositiveButton("yes", (dialogInterface, i) -> {
                    mCategoryAdapter.deleteCategory(mCategoryAdapter.getPosition(mCategory), success -> {
                        Toast.makeText(getActivity(), "Category has been deleted successfully.", Toast.LENGTH_SHORT).show();
                    });
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).setIcon(R.drawable.ic_round_warning_24);
                builder.show();
            }

            return false;
        });
        popupMenu.setOnDismissListener(menu -> {

        });
        popupMenu.show();
    }

    public void moveTask(int position, boolean isNext) {
        mTaskModel.moveTask(position, isNext ? mNextCategory : mPreviousCategory, success -> mCategoryAdapter.reloadCategoryViewModel());
    }
}