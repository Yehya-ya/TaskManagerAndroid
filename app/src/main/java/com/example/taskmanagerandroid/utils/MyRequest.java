package com.example.taskmanagerandroid.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyRequest {
    private static MyRequest instance;
    private static Context ctx;
    private RequestQueue requestQueue;

    private MyRequest(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MyRequest getInstance(Context context) {
        if (instance == null) {
            instance = new MyRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}