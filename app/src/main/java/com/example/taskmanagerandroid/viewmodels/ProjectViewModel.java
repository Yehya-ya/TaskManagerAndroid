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
import com.example.taskmanagerandroid.models.User;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectViewModel extends AndroidViewModel {
    private final int mProjectId;
    private final MutableLiveData<Project> mProject;

    public ProjectViewModel(@NonNull Application application, int projectId) {
        super(application);
        this.mProjectId = projectId;
        this.mProject = new MutableLiveData<>();
        loadProject();
    }

    public void loadProject() {
        MyRequest request = new MyRequest();
        request.setUrl(Route.getProjectsShowRoute(mProjectId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                JSONObject ownerObject = data.getJSONObject("owner");
                Project project = new Project(data);
                User owner = new User(ownerObject);
                project.setOwner(owner);
                JSONArray categories = data.getJSONArray("categories");
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject categoryObject = categories.getJSONObject(i);
                    Category category = new Category(categoryObject);
                    category.setProjectId(categoryObject.getInt("project_id"));
                    project.addCategory(category);
                }
                JSONArray tasks = data.getJSONArray("tasks");
                for (int i = 0; i < tasks.length(); i++) {
                    JSONObject taskObject = tasks.getJSONObject(i);
                    Task task = new Task(taskObject);
                    task.setCategoryId(taskObject.getInt("category_id"));
                    task.setProjectId(taskObject.getInt("project_id"));
                    project.addTask(task);
                }
                JSONArray members = data.getJSONArray("members");
                for (int i = 0; i < members.length(); i++) {
                    JSONObject memberObject = members.getJSONObject(i);
                    User member = new User(memberObject);
                    project.addMember(member);
                }
                mProject.setValue(project);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public MutableLiveData<Project> getProject() {
        return mProject;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;

        private final int mProjectId;

        public Factory(@NonNull Application application, int projectId) {
            this.mApplication = application;
            this.mProjectId = projectId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ProjectViewModel(mApplication, mProjectId);
        }
    }
}
