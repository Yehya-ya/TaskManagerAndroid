package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.models.Project;
import com.example.taskmanagerandroid.models.Task;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.ActionListener;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskViewModel extends AndroidViewModel {

    private final static String TAG = "TaskViewModel";

    private final int mProjectId;
    private final int mTaskId;
    private final MutableLiveData<Task> mTasks;

    public TaskViewModel(@NonNull Application application, int projectId, int taskId) {
        super(application);
        this.mTaskId = taskId;
        this.mProjectId = projectId;
        this.mTasks = new MutableLiveData<>();
        loadTask();
    }

    public void loadTask() {
        MyRequest request = new MyRequest();
        request.setUrl(Route.getTasksShowRoute(mProjectId, mTaskId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Task task = new Task(data);
                Category category = new Category(data.getJSONObject("category"));
                Project project = new Project(data.getJSONObject("project"));
                task.setProject(project);
                task.setCategory(category);
                mTasks.setValue(task);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public MutableLiveData<Task> getTask() {
        return mTasks;
    }

    public void updateTask(String title, String description, String end_at, int categoryId, ActionListener listener) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.PUT);
        request.setUrl(Route.getTasksUpdateRoute(mProjectId, mTaskId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.addParam("category_id", "" + categoryId);
        request.addParam("title", title);
        request.addParam("description", description);
        request.addParam("end_at", end_at);

        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Task task = mTasks.getValue();
                task.setTitle(data.getString("title"));
                task.setDescription(data.isNull("description") ? null : data.getString("description"));
                task.setEnd_at(data.isNull("end_at") ? null : data.getString("end_at"));
                task.setUpdated_at(data.getString("updated_at"));
                mTasks.setValue(mTasks.getValue());
                listener.action(true);
            } catch (JSONException e) {
                listener.action(false);
                e.printStackTrace();
            }
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;

        private final int mProjectId;
        private final int mTaskId;

        public Factory(@NonNull Application application, int projectId, int taskId) {
            this.mApplication = application;
            this.mProjectId = projectId;
            this.mTaskId = taskId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TaskViewModel(mApplication, mProjectId, mTaskId);
        }
    }
}
