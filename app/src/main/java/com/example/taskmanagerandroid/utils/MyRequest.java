package com.example.taskmanagerandroid.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
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
    private final String TAG = "MyRequest";
    private final Map<String, String> additionalHeaders;
    private final Map<String, String> additionalParams;
    private int method;
    private String url;
    private Response.Listener<String> response;
    private ErrorHandler errorHandler;

    public MyRequest() {
        this.method = Request.Method.GET;
        this.additionalHeaders = new HashMap<>();
        this.additionalParams = new HashMap<>();
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResponse(Response.Listener<String> response) {
        this.response = response;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void addHeader(String key, String value) {
        this.additionalHeaders.put(key, value);
    }

    public void addParam(String key, String value) {
        this.additionalParams.put(key, value);
    }

    public StringRequest getRequest() {
        if (this.url == null) {
            Log.e(TAG, "set url value before send request");
        }
        if (this.response == null) {
            Log.e(TAG, "set url value before send request");
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
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null) {
                    headers = new HashMap<>();
                }
                headers.put("Connection", "keep-alive");
                headers.put("Accept", "application/json");
                headers.putAll(MyRequest.this.additionalHeaders);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = super.getParams();
                if (params == null) {
                    params = new HashMap<>();
                }
                params.putAll(MyRequest.this.additionalParams);
                return super.getParams();
            }
        };
    }

    private boolean isServerOrClientErrors(VolleyError error) {
        final int statusCode = error.networkResponse.statusCode;
        if (500 <= statusCode) {
            Log.v(TAG, "could not connect to server.");
            Log.v(TAG, error.networkResponse.toString());
            return true;
        }

        if (300 <= statusCode && statusCode < 400) {
            Log.v(TAG, "bad request.");
            Log.v(TAG, error.networkResponse.toString());
            return true;
        }

        return false;
    }

    public interface ErrorHandler {
        default void handlingMessage(String massage) {
            Log.e(this.getClass().toString(), massage);
        }

        default void handlingErrors(JSONObject errors) {

        }

        default void action() {

        }
    }
}
