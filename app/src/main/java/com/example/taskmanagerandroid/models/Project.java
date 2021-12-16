package com.example.taskmanagerandroid.models;

import java.util.LinkedList;

public class Project {
    private final int id;
    private final String title;
    private final String description;
    private User owner;
    private int owner_id;
    private LinkedList<User> members;
    private LinkedList<Task> tasks;
    private LinkedList<Category> categories;

    public Project(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public int getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(int owner_id) {
        this.owner_id = owner_id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        this.owner_id = owner.getId();
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
