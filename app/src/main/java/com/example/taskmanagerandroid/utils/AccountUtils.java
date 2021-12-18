package com.example.taskmanagerandroid.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountUtils {
    private static String mAccessToken;
    private static User mUser;

    public static void login(Application application) {
        login(application, success -> {
        });
    }

    public static void login(Application application, ActionListener listener) {
        String access_token = application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("access_token", "");
        MyRequest verifyRequest = new MyRequest();
        verifyRequest.setMethod(Request.Method.POST);
        verifyRequest.setUrl(Route.getTokenVerifyRoute());
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

    public void logout() {
        mAccessToken = null;
        mUser = null;
    }
}
