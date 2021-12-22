package com.example.taskmanagerandroid.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.models.Task;
import com.example.taskmanagerandroid.utils.ActionListener;
import com.example.taskmanagerandroid.viewmodels.CategoryCollectionViewModel;
import com.example.taskmanagerandroid.viewmodels.TaskViewModel;

import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private static final String TAG = "TaskActivity";
    private int mProjectId;
    private int mTaskId;
    private TaskViewModel mTaskViewModel;
    private CategoryCollectionViewModel mCategoryCollectionViewModel;
    private List<Category> mCategories;
    private Task mTask;
    private EditText mTitle;
    private EditText mDescription;
    private TextView mEndAtView;
    private TextView mProjectTitle;
    private TextView mCategoryTitle;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mProjectId = getIntent().getIntExtra("projectId", -1);
        mTaskId = getIntent().getIntExtra("taskId", -1);
        mCategories = new LinkedList<>();
        if (mProjectId < 0 || mTaskId < 0) {
            Log.e(TAG, "no project id or task id.");
            finish();
            return;
        }

        mTitle = findViewById(R.id.task_page_title);
        mDescription = findViewById(R.id.task_page_description);
        mEndAtView = findViewById(R.id.task_page_end_at);
        mProjectTitle = findViewById(R.id.task_page_project_title);
        mCategoryTitle = findViewById(R.id.task_page_category_title);

        setupEditTextListener(mTitle, success -> {
            mTask.setTitle(mTitle.getText().toString());
        });
        setupEditTextListener(mDescription, success -> {
            mTask.setDescription(mDescription.getText().toString());
        });

        mTaskViewModel = new ViewModelProvider(this, new TaskViewModel.Factory(getApplication(), mProjectId, mTaskId)).get(TaskViewModel.class);
        mTaskViewModel.getTask().observe(this, task -> {
            mTask = task;
            update();
        });

        mCategoryCollectionViewModel = new ViewModelProvider(this, new CategoryCollectionViewModel.Factory(getApplication(), mProjectId)).get(CategoryCollectionViewModel.class);
        mCategoryCollectionViewModel.getCategories().observe(this, categories -> {
            mCategories = categories;
        });

        mEndAtView.setOnClickListener(view1 -> {
            Calendar endAt;
            if (mTask.getEndAt() == null) {
                endAt = Calendar.getInstance();
            } else {
                Calendar.Builder builder = new Calendar.Builder();
                endAt = builder.setInstant(mTask.getEndAt()).build();
            }

            DatePickerDialog datePickerFragment = new DatePickerDialog(
                    this,
                    (datePicker, i, i1, i2) -> {
                        endAt.set(i, i1, i2);
                        mTask.setEnd_at(new Date(endAt.getTimeInMillis()));
                        update();
                    },
                    endAt.get(Calendar.YEAR),
                    endAt.get(Calendar.MONTH),
                    endAt.get(Calendar.DAY_OF_MONTH)
            );
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DATE, 1);
            datePickerFragment.getDatePicker().setMinDate(tomorrow.getTimeInMillis());
            datePickerFragment.show();
        });

        (findViewById(R.id.task_page_categories_spinner)).setOnClickListener(this::showMenu);

        (findViewById(R.id.clear_date)).setOnClickListener(view -> {
            mEndAtView.setText("");
            mTask.setEnd_at((Date) null);
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_clear_24);
    }

    public void setupEditTextListener(EditText editText, ActionListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.action(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        for (int i = 0; i < mCategories.size(); i++) {
            Category category = mCategories.get(i);
            popupMenu.getMenu().add(0, i, i, category.getTitle());
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            mTask.setCategory(mCategories.get(item.getItemId()));
            update();
            return false;
        });
        popupMenu.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update() {
        getSupportActionBar().setTitle(mTask.getTitle());
        mTitle.setText(mTask.getTitle());
        if (mTask.getDescription() != null) {
            mDescription.setText(mTask.getDescription());
        }
        if (mTask.getEndAt() != null) {
            mEndAtView.setText(mTask.getFormattedEndAt());
        }
        mProjectTitle.setText(mTask.getProject().getTitle());
        mCategoryTitle.setText(mTask.getCategory().getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.confirm_edit:
                mTaskViewModel.updateTask(
                        mTask.getTitle(),
                        mTask.getDescription(),
                        mTask.getFormattedForServerEndAt(),
                        mTask.getCategoryId(),
                        success -> {
                            if (success) {
                                setResult(RESULT_OK);
                                finish();
                                return;
                            }
                            Toast.makeText(this, "the given data is invalid.", Toast.LENGTH_SHORT).show();
                        }
                );
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
