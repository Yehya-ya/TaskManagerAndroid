package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;

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
                    Project tempProject = new Project(
                            projectObject.getInt("id"),
                            projectObject.getString("title"),
                            projectObject.isNull("description") ? null : projectObject.getString("description")
                    );
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
        if (title != null)
            request.addParam("title", title);
        if (description != null)
            request.addParam("description", description);


        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Project project = new Project(data.getInt("id"), title, description);
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
}
