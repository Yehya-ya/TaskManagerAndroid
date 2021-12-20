package com.example.taskmanagerandroid.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountUtils {
    private static String mAccessToken;
    private static User mUser;

    public static void login(Application application, String email, String password, ActionListener listener, MyRequest.ErrorHandler errorHandler) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getLoginRoute());
        if (email != null)
            request.addParam("email", email);
        if (password != null)
            request.addParam("password", password);

        request.setResponse(response -> {
            try {
                String token = new JSONObject(response).getString("token");
                SharedPreferences.Editor editor = application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
                editor.putString(application.getString(R.string.access_token), token);
                editor.apply();
                listener.action(true);
            } catch (JSONException e) {
                listener.action(false);
                e.printStackTrace();
            }
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void handlingMessage(String massage) {
                errorHandler.handlingMessage(massage);
            }

            @Override
            public void handlingErrors(JSONObject errors) {
                errorHandler.handlingErrors(errors);
            }

            @Override
            public void action() {
                errorHandler.action();
            }
        });
        MyRequestQueue.getInstance(application).addToRequestQueue(request);
    }

    public static void register(Application application, String name, String email, String password, String passwordConfirmation, ActionListener listener, MyRequest.ErrorHandler errorHandler) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getRegisterRoute());
        if (name != null)
            request.addParam("name", name);
        if (email != null)
            request.addParam("email", email);
        if (password != null)
            request.addParam("password", password);
        if (passwordConfirmation != null)
            request.addParam("password_confirmation", passwordConfirmation);

        request.setResponse(response -> {
            try {
                String token = new JSONObject(response).getString("token");
                SharedPreferences.Editor editor = application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
                editor.putString(application.getString(R.string.access_token), token);
                editor.apply();
                listener.action(true);
            } catch (JSONException e) {
                listener.action(false);
                e.printStackTrace();
            }
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void handlingMessage(String massage) {
                errorHandler.handlingMessage(massage);
            }

            @Override
            public void handlingErrors(JSONObject errors) {
                errorHandler.handlingErrors(errors);
            }

            @Override
            public void action() {
                errorHandler.action();
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(application).addToRequestQueue(request);
    }

    public static void verifyToken(Application application) {
        verifyToken(application, success -> {
        });
    }

    public static void verifyToken(Application application, ActionListener listener) {
        String access_token = application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString(application.getString(R.string.access_token), null);
        MyRequest verifyRequest = new MyRequest();
        verifyRequest.setMethod(Request.Method.POST);
        verifyRequest.setUrl(Route.getTokenVerifyRoute());
        if (access_token != null)
            verifyRequest.addAuthorizationHeader(access_token);

        verifyRequest.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                mUser = new User(data.getInt("id"), data.getString("name"), data.getString("email"));
                mAccessToken = access_token;
                listener.action(true);
            } catch (JSONException e) {
                listener.action(false);
                e.printStackTrace();
            }
        });
        verifyRequest.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(application).addToRequestQueue(verifyRequest);
    }

    public static String getAccessToken() {
        if (mAccessToken == null) {
            return null;
        }
        return mAccessToken;
    }

    public static User getUser() {
        if (mUser == null) {
            return null;
        }
        return mUser;
    }

    public static void logout(Application application, ActionListener listener) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getLogoutRoute());
        request.addAuthorizationHeader(getAccessToken());
        request.setResponse(response -> {
            SharedPreferences.Editor editor = application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
            editor.remove(application.getString(R.string.access_token));
            editor.apply();
            mAccessToken = null;
            mUser = null;
            listener.action(true);
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(application).addToRequestQueue(request);
    }
}
