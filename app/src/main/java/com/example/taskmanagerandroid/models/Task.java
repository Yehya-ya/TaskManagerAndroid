package com.example.taskmanagerandroid.models;

import java.sql.Date;

public class Task {
    private final long id;
    private final String title;
    private final String description;
    private final Date end_at;
    private Project project;
    private Category category;
    private User assigned_user;

    public Task(long id, String title, String description, String end_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.end_at = Date.valueOf(end_at);
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

    public Date getEnd_at() {
        return end_at;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getAssignedUser() {
        return assigned_user;
    }

    public void setAssignedUser(User assigned_user) {
        this.assigned_user = assigned_user;
    }
}
