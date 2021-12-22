package com.example.taskmanagerandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Category {
    private final int id;
    private String title;
    private String description;
    private Project project;
    private int project_id;
    private String updated_at;

    public Category(JSONObject categoryObject) throws JSONException {
        this.id = categoryObject.getInt("id");
        this.title = categoryObject.getString("title");
        this.description = categoryObject.isNull("description") ? null : categoryObject.getString("description");
        this.updated_at = categoryObject.getString("updated_at");
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.project_id = project.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id && updated_at.equals(category.updated_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, updated_at);
    }
}
