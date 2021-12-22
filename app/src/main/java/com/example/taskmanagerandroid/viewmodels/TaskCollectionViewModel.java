package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.taskmanagerandroid.models.Task;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.ActionListener;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TaskCollectionViewModel extends AndroidViewModel {
    private final static String TAG = "TaskCollectionViewModel";

    private final int mProjectId;
    private final int mCategoryId;
    private final MutableLiveData<List<Task>> mTasks;

    public TaskCollectionViewModel(@NonNull Application application, int projectId, int categoryId) {
        super(application);
        this.mCategoryId = categoryId;
        this.mProjectId = projectId;
        this.mTasks = new MutableLiveData<>();
        loadTasks();
    }

    public void loadTasks() {
        MyRequest request = new MyRequest();
        request.setUrl(Route.getCategoriesShowRoute(mProjectId, mCategoryId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.setResponse(response -> {
            try {
                JSONArray tasks = new JSONObject(response).getJSONObject("data").getJSONArray("tasks");
                List<Task> taskList = new LinkedList<>();
                for (int i = 0; i < tasks.length(); i++) {
                    JSONObject task = tasks.getJSONObject(i);
                    Task tempTask = new Task(task);
                    tempTask.setProjectId(mProjectId);
                    tempTask.setCategoryId(mCategoryId);
                    taskList.add(tempTask);
                }
                mTasks.setValue(taskList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public MutableLiveData<List<Task>> getTasks() {
        return mTasks;
    }

    public void createTask(String title, String description, String end_at, ActionListener listener) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getTasksCreateRoute(mProjectId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());

        request.addParam("title", title);
        request.addParam("description", description);
        request.addParam("end_at", end_at);

        request.addParam("category_id", "" + mCategoryId);
        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Task task = new Task(data);
                task.setCategoryId(mCategoryId);
                task.setProjectId(mProjectId);
                mTasks.getValue().add(task);
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

    public void moveTask(int position, int categoryId, ActionListener listener) {
        Task task;
        try {
            task = mTasks.getValue().get(position);
        } catch (Exception exception) {
            Log.e(TAG, "can not move the category: " + exception.getMessage());
            listener.action(false);
            return;
        }
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.PUT);
        request.setUrl(Route.getTasksMoveRoute(mProjectId, task.getId()));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.addParam("category_id", "" + categoryId);

        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                mTasks.getValue().remove(task);
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
            return (T) new TaskCollectionViewModel(mApplication, mProjectId, mCategoryId);
        }
    }
}
