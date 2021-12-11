package com.example.taskmanagerandroid.models;

import java.util.LinkedList;

public class Project {
    private final long id;
    private final String title;
    private final String description;
    private User owner;
    private LinkedList<User> members;
    private LinkedList<Task> tasks;
    private LinkedList<Category> categories;

    public Project(long id, String title, String description) {
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LinkedList<User> getMembers() {
        return members;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public LinkedList<Category> getCategories() {
        return categories;
    }

    public void addMember(User user) {
        if (members == null) {
            members = new LinkedList<>();
        }
        members.add(user);
    }

    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new LinkedList<>();
        }
        tasks.add(task);
    }

    public void addCategory(Category category) {
        if (categories == null) {
            categories = new LinkedList<>();
        }
        categories.add(category);
    }
}
