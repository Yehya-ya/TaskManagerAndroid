package com.example.taskmanagerandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class User {
    private final int id;
    private final String name;
    private final String email;
    private LinkedList<Project> projects;
    private LinkedList<Project> projects_member_in;

    public User(JSONObject data) throws JSONException {
        this.id = data.getInt("id");
        this.name = data.getString("name");
        this.email = data.getString("email");
    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LinkedList<Project> getProjects() {
        return projects;
    }

    public LinkedList<Project> getProjectsMemberIn() {
        return projects_member_in;
    }

    public void addProject(Project project) {
        if (projects == null) {
            projects = new LinkedList<>();
        }
        projects.add(project);
    }

    public void addProjectToBeMemberIn(Project project) {
        if (projects_member_in == null) {
            projects_member_in = new LinkedList<>();
        }
        projects_member_in.add(project);
    }
}
