package com.example.taskmanagerandroid.models;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class Task {
    private final int id;
    private String title;
    private String description;
    private Date end_at;
    private String updated_at;
    private Project project;
    private int project_id;
    private Category category;
    private int category_id;
    private User assigned_user;
    private int assigned_user_id;

    public Task(@NonNull JSONObject taskObject) throws JSONException {
        this.id = taskObject.getInt("id");
        this.title = taskObject.getString("title");
        this.description = taskObject.isNull("description") ? null : taskObject.getString("description");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !taskObject.isNull("end_at")) {
            DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            this.end_at = Date.valueOf(output.format(LocalDate.parse(taskObject.getString("end_at"), input)));
        }
        this.updated_at = taskObject.getString("updated_at");
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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnd_at(String end_at) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && end_at != null) {
            DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            this.end_at = Date.valueOf(output.format(LocalDate.parse(end_at, input)));
        }
    }

    public void setEnd_at(Date end_at) {
        this.end_at = end_at;
    }

    public String getFormattedEndAt() {
        if (end_at == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        return formatter.format(this.end_at);
    }

    public String getFormattedForServerEndAt() {
        if (end_at == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");
        return formatter.format(this.end_at);
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public void setProject(@NonNull Project project) {
        this.project = project;
        this.project_id = project.getId();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(@NonNull Category category) {
        this.category = category;
        this.category_id = category.getId();
    }

    public User getAssignedUser() {
        return assigned_user;
    }

    public void setAssignedUser(@NonNull User assigned_user) {
        this.assigned_user = assigned_user;
        this.assigned_user_id = assigned_user.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && updated_at.equals(task.updated_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, updated_at);
    }
}
