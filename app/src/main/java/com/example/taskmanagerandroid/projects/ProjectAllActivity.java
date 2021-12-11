package com.example.taskmanagerandroid.projects;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.adapters.ProjectAdapter;
import com.example.taskmanagerandroid.models.Project;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ProjectAllActivity extends AppCompatActivity {

    private static final String TAG = "ProjectAllActivity";

    private RecyclerView projectsView;
    private ProjectAdapter projectAdapter;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all);

        Log.v(TAG, "start activity");

        projectsView = findViewById(R.id.projectRecycler);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String access_token = sharedPref.getString("access_token", "");

        MyRequest request = new MyRequest();
        request.setUrl(Route.getProjectsAllRoute());
        request.addHeader("Authorization", "Bearer " + access_token);
        request.setResponse(response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                if (responseObject.has("data")) {
                    List<Project> projects = new LinkedList<>();
                    JSONArray data = responseObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject project = data.getJSONObject(i);
                        projects.add(new Project(project.getInt("id"), project.getString("title"), project.getString("description")));
                    }
                    projectAdapter.setProjects(projects);

                    projectAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(this).addToRequestQueue(request);

        projectAdapter = new ProjectAdapter(this);
        projectsView.setAdapter(projectAdapter);
        projectsView.setLayoutManager(new LinearLayoutManager(this));

        button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(view -> {
            MyRequest addProjectRequest = new MyRequest();
            addProjectRequest.setMethod(Request.Method.POST);
            addProjectRequest.setUrl(Route.getProjectsCreateRoute());
            addProjectRequest.addHeader("Authorization", "Bearer " + access_token);
            addProjectRequest.addParam("title", "this is title");
            addProjectRequest.addParam("description", "this is description");

            addProjectRequest.setResponse(response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    Log.v(TAG, response);
                    if (responseObject.has("data")) {
                        JSONObject data = responseObject.getJSONObject("data");
                        projectAdapter.addProject(new Project(data.getInt("id"), data.getString("title"), data.getString("description")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });


            MyRequestQueue.getInstance(this).addToRequestQueue(addProjectRequest);
        });
    }
}