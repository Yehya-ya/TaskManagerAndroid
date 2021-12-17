package com.example.taskmanagerandroid.models;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Task {
    private final int id;
    private final String title;
    private final String description;
    private Date end_at;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && end_at != null) {
            DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            this.end_at = Date.valueOf(output.format(LocalDate.parse(end_at, input)));
        }
    }

    public Task(JSONObject taskObject) throws JSONException {
        this.id = taskObject.getInt("id");
        this.title = taskObject.getString("title");
        this.description = taskObject.isNull("description") ? null : taskObject.getString("description");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !taskObject.isNull("end_at")) {
            DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            this.end_at = Date.valueOf(output.format(LocalDate.parse(taskObject.getString("end_at"), input)));
        }
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
