package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.taskmanagerandroid.models.Project;
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

public class ProjectCollectionViewModel extends AndroidViewModel {
    private static final String TAG = "ProjectCollectionViewModel";

    private final MutableLiveData<List<Project>> mProjects;

    public ProjectCollectionViewModel(@NonNull Application application) {
        super(application);
        mProjects = new MutableLiveData<>();

        loadProject();
    }

    public void loadProject() {
        MyRequest request = new MyRequest();
        request.setUrl(Route.getProjectsAllRoute());
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.setResponse(response -> {
            try {
                JSONArray data = new JSONObject(response).getJSONArray("data");
                List<Project> projectList = new LinkedList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject projectObject = data.getJSONObject(i);
                    Project tempProject = new Project(projectObject);
                    tempProject.setOwnerId(projectObject.getInt("user_id"));
                    projectList.add(tempProject);
                }
                mProjects.setValue(projectList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public MutableLiveData<List<Project>> getProjects() {
        return mProjects;
    }

    public void createProject(String title, String description, ActionListener listener) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getProjectsCreateRoute());
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.addParam("title", title);
        request.addParam("description", description);


        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Project project = new Project(data);
                project.setOwnerId(data.getInt("user_id"));
                mProjects.getValue().add(project);
                mProjects.setValue(mProjects.getValue());
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

    public void editProject(int position, String title, String description, ActionListener listener) {
        Project project;
        try {
            project = mProjects.getValue().get(position);
        } catch (Exception exception) {
            Log.e(TAG, "can not edit the project: " + exception.getMessage());
            listener.action(false);
            return;
        }

        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.PUT);
        request.setUrl(Route.getProjectsUpdateRoute(project.getId()));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        request.addParam("title", title);
        request.addParam("description", description);

        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                project.setTitle(title);
                project.setDescription(description);
                project.setUpdated_at(data.getString("updated_at"));
                mProjects.setValue(mProjects.getValue());
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

    public void deleteProject(int position, ActionListener listener) {
        Project project;
        try {
            project = mProjects.getValue().get(position);
        } catch (Exception exception) {
            Log.e(TAG, "can not delete the project: " + exception.getMessage());
            listener.action(false);
            return;
        }

        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.DELETE);
        request.setUrl(Route.getProjectsDeleteRoute(project.getId()));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());

        request.setResponse(response -> {
            mProjects.getValue().remove(position);
            mProjects.setValue(mProjects.getValue());
            listener.action(true);
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }
}
