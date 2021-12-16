package com.example.taskmanagerandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class Category {
    private final int id;
    private final String title;
    private final String description;
    private LinkedList<Task> tasks;
    private Project project;
    private int project_id;

    public Category(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Category(JSONObject categoryObject) throws JSONException {
        this.id = categoryObject.getInt("id");
        this.title = categoryObject.getString("title");
        this.description = categoryObject.isNull("description") ? null : categoryObject.getString("description");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int project_id) {
        this.project_id = project_id;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.project_id = project.getId();
    }

    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new LinkedList<>();
        }
        this.tasks.add(task);
    }
}
