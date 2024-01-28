package com.example.taskmanagerandroid.utils;

public class Route {
    private static final String BASE_URL = "https://task-manager.yehya.dev/api";
    private static final String PROJECTS = BASE_URL + "/projects";
    private static final String USERS = BASE_URL + "/users";

    public static String getRegisterRoute() {
        return BASE_URL + "/register";
    }

    public static String getLoginRoute() {
        return BASE_URL + "/login";
    }

    public static String getTokenVerifyRoute() {
        return BASE_URL + "/verify_access_token";
    }

    public static String getLogoutRoute() {
        return BASE_URL + "/logout";
    }

    public static String getProjectsAllRoute() {
        return PROJECTS;
    }

    public static String getProjectsShowRoute(int project_id) {
        return PROJECTS + "/" + project_id;
    }

    public static String getProjectsCreateRoute() {
        return PROJECTS;
    }

    public static String getProjectsUpdateRoute(int project_id) {
        return PROJECTS + "/" + project_id;
    }

    public static String getProjectsDeleteRoute(int project_id) {
        return PROJECTS + "/" + project_id;
    }

    public static String getProjectsAddMemberRoute(int project_id) {
        return PROJECTS + "/" + project_id + "/add-member";
    }

    public static String getProjectsRemoveMemberRoute(int project_id) {
        return PROJECTS + "/" + project_id + "/remove-member";
    }

    public static String getUsersAllRoute() {
        return USERS;
    }

    public static String getUsersShowRoute(int user_id) {
        return USERS + "/" + user_id;
    }

    public static String getUsersUpdateRoute(int user_id) {
        return USERS + "/" + user_id;
    }

    public static String getUsersDeleteRoute(int user_id) {
        return USERS + "/" + user_id;
    }

    public static String getCategoriesAllRoute(int project_id) {
        return PROJECTS + "/" + project_id + "/categories";
    }

    public static String getCategoriesShowRoute(int project_id, int category_id) {
        return getCategoriesAllRoute(project_id) + "/" + category_id;
    }

    public static String getCategoriesCreateRoute(int project_id) {
        return getCategoriesAllRoute(project_id);
    }

    public static String getCategoriesUpdateRoute(int project_id, int category_id) {
        return getCategoriesAllRoute(project_id) + "/" + category_id;
    }

    public static String getCategoriesDeleteRoute(int project_id, int category_id) {
        return getCategoriesAllRoute(project_id) + "/" + category_id;
    }

    public static String getTasksAllRoute(int project_id) {
        return PROJECTS + "/" + project_id + "/tasks";
    }

    public static String getTasksShowRoute(int project_id, int task_id) {
        return getTasksAllRoute(project_id) + "/" + task_id;
    }

    public static String getTasksCreateRoute(int project_id) {
        return getTasksAllRoute(project_id);
    }

    public static String getTasksMoveRoute(int project_id, int task_id) {
        return getTasksAllRoute(project_id) + "/" + task_id + "/move";
    }

    public static String getTasksUpdateRoute(int project_id, int task_id) {
        return getTasksAllRoute(project_id) + "/" + task_id;
    }

    public static String getTasksDeleteRoute(int project_id, int task_id) {
        return getTasksAllRoute(project_id) + "/" + task_id;
    }
}
