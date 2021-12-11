package com.example.taskmanagerandroid.models;

import java.util.LinkedList;

public class Category {
    private final long id;
    private final String title;
    private final String description;
    private LinkedList<Task> tasks;
    private Project project;

    public Category(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new LinkedList<>();
        }
        this.tasks.add(task);
    }
}
