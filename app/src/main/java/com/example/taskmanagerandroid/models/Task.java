package com.example.taskmanagerandroid.models;

import java.sql.Date;

public class Task {
    private final int id;
    private final String title;
    private final String description;
    private final Date end_at;
    private Project project;
    private int project_id;
    private Category category;
    private int category_id;
    private User assigned_user;
    private int assigned_user_id;

    public Task(int id, String title, String description, String end_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.end_at = end_at != null ? Date.valueOf(end_at) : null;
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

    public Date getEndAt() {
        return end_at;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int project_id) {
        this.project_id = project_id;
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    public int getAssignedUserId() {
        return assigned_user_id;
    }

    public void setAssignedUserId(int assigned_user_id) {
        this.assigned_user_id = assigned_user_id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.project_id = project.getId();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.category_id = category.getId();
    }

    public User getAssignedUser() {
        return assigned_user;
    }

    public void setAssignedUser(User assigned_user) {
        this.assigned_user = assigned_user;
        this.assigned_user_id = assigned_user.getId();
    }
}
