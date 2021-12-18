package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.models.Project;
import com.example.taskmanagerandroid.models.Task;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryViewModel extends AndroidViewModel {
    private final int mProjectId;
    private final int mCategoryId;
    private MutableLiveData<Category> category;

    public CategoryViewModel(@NonNull Application application, int productID, int categoryId) {
        super(application);
        this.mProjectId = productID;
        this.mCategoryId = categoryId;

        loadCategory();
    }

    public MutableLiveData<Category> getCategory() {
        return category;
    }

    public void loadCategory() {
        MyRequest request = new MyRequest();
        request.setUrl(Route.getCategoriesShowRoute(mProjectId, mCategoryId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Category newCategory = new Category(data.getInt("id"), data.getString("title"), data.getString("description"));

                JSONArray tasks = data.getJSONArray("tasks");
                for (int i = 0; i < tasks.length(); i++) {
                    JSONObject task = tasks.getJSONObject(i);
                    Task tempTask = new Task(
                            task.getInt("id"),
                            task.getString("title"),
                            task.isNull("description") ? null : task.getString("description"),
                            task.isNull("end_at") ? null : task.getString("end_at")
                    );
                    tempTask.setCategoryId(mCategoryId);
                    tempTask.setProjectId(mProjectId);
                    newCategory.addTask(tempTask);
                }

                JSONObject project = data.getJSONObject("project");
                newCategory.setProject(new Project(project.getInt("id"), project.getString("title"), project.getString("description")));

                category.setValue(newCategory);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;

        private final int mProjectId;
        private final int mCategoryId;

        public Factory(@NonNull Application application, int projectId, int categoryId) {
            this.mApplication = application;
            this.mProjectId = projectId;
            this.mCategoryId = categoryId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CategoryViewModel(mApplication, mProjectId, mCategoryId);
        }
    }
}
