package com.example.taskmanagerandroid.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyRequest {
    private static final String TAG = "MyRequest";

    private final Map<String, String> headers;
    private int method;
    private final Map<String, String> params;
    private String url;
    private Response.Listener<String> response;
    private ErrorHandler errorHandler;

    public MyRequest() {
        this.method = Request.Method.GET;
        this.headers = new HashMap<>();
        this.headers.put("Connection", "keep-alive");
        this.headers.put("Accept", "application/json");
        this.params = new HashMap<>();
        this.errorHandler = new ErrorHandler() {
        };
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addAuthorizationHeader(String token) {
        this.addHeader("Authorization", "Bearer " + token);
    }

    public void addParam(String key, String value) {
        this.params.put(key, value == null ? "" : value);
    }

    public void setResponse(Response.Listener<String> response) {
        this.response = response;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public StringRequest getRequest() throws Exception {
        if (this.url == null) {
            Log.e(TAG, "try to create request without url");
            throw new Exception("set url value before send request");
        }
        if (this.response == null) {
            Log.e(TAG, "try to create request without response");
            throw new Exception("set response value before send request");
        }

        return new StringRequest(
                this.method,
                this.url,
                this.response,
                error -> {
                    if (!MyRequest.this.isServerOrClientErrors(error)) {
                        try {
                            JSONObject body = new JSONObject(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                            if (body.has("errors")) {
                                JSONObject errors = body.getJSONObject("errors");
                                errorHandler.handlingErrors(errors);
                            }
                            if (body.has("message")) {
                                String message = body.getString("message");
                                errorHandler.handlingMessage(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    errorHandler.action();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return MyRequest.this.headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return MyRequest.this.params;
            }
        };
    }

    private boolean isServerOrClientErrors(VolleyError error) {
        if (error.networkResponse == null) {
            Log.e(TAG, "no response. check network connectivity");
            return true;
        }

        final int statusCode = error.networkResponse.statusCode;
        if (500 <= statusCode) {
            Log.e(TAG, "could not connect to server.");
            Log.e(TAG, new String(error.networkResponse.data, StandardCharsets.UTF_8));
            return true;
        }

        if (300 <= statusCode && statusCode < 400) {
            Log.e(TAG, "bad request.");
            Log.e(TAG, new String(error.networkResponse.data, StandardCharsets.UTF_8));
            return true;
        }

        return false;
    }

    public interface ErrorHandler {
        default void handlingMessage(String massage) {
            Log.e(TAG, massage);
        }
        default void handlingErrors(JSONObject errors) {
            Log.e(TAG, errors.toString());
        }
        default void action() {
        }
    }
}
