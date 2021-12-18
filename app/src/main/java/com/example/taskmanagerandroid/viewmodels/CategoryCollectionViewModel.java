package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.utils.AccountUtils;
import com.example.taskmanagerandroid.utils.ActionListener;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class CategoryCollectionViewModel extends AndroidViewModel {
    private static final String TAG = "CategoryCollectionViewModel";

    private final MutableLiveData<List<Category>> mCategories;
    private final int mProjectId;

    public CategoryCollectionViewModel(@NonNull Application application, int projectId) {
        super(application);
        mProjectId = projectId;
        mCategories = new MutableLiveData<>();
        mCategories.setValue(new LinkedList<>());
        loadCategories();
    }

    public void loadCategories() {
        MyRequest request = new MyRequest();
        request.setUrl(Route.getCategoriesAllRoute(mProjectId));
        String access_token = AccountUtils.getAccessToken();
        request.addAuthorizationHeader(access_token);
        request.setResponse(response -> {
            try {
                JSONArray data = new JSONObject(response).getJSONArray("data");
                List<Category> categoryList = new LinkedList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject categoryObject = data.getJSONObject(i);
                    Category tempCategory = new Category(
                            categoryObject.getInt("id"),
                            categoryObject.getString("title"),
                            categoryObject.isNull("description") ? null : categoryObject.getString("description")
                    );
                    tempCategory.setProjectId(mProjectId);
                    categoryList.add(tempCategory);
                }
                mCategories.setValue(categoryList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public MutableLiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public void createCategory(String title, String description, ActionListener listener) {
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.POST);
        request.setUrl(Route.getCategoriesCreateRoute(mProjectId));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        if (title != null)
            request.addParam("title", title);
        if (description != null)
            request.addParam("description", description);

        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                Category category = new Category(
                        data.getInt("id"),
                        data.getString("title"),
                        data.isNull("description") ? null : data.getString("description")
                );
                category.setProjectId(mProjectId);
                mCategories.getValue().add(category);
                mCategories.setValue(mCategories.getValue());

                listener.action(true);
            } catch (JSONException e) {
                listener.action(false);
                e.printStackTrace();
            }
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public void editCategory(int position, String title, String description, ActionListener listener) {
        Category category;
        try {
            category = mCategories.getValue().get(position);
        } catch (Exception exception) {
            Log.e(TAG, "can not edit the category: " + exception.getMessage());
            listener.action(false);
            return;
        }
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.PUT);
        request.setUrl(Route.getCategoriesUpdateRoute(mProjectId, category.getId()));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());
        if (title != null)
            request.addParam("title", title);
        if (description != null)
            request.addParam("description", description);

        request.setResponse(response -> {
            try {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                category.setTitle(data.getString("title"));
                category.setDescription(data.has("description") ? data.getString("description") : null);
                mCategories.setValue(mCategories.getValue());
                listener.action(true);
            } catch (JSONException e) {
                listener.action(false);
                e.printStackTrace();
            }
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public void deleteCategory(int position, ActionListener listener) {
        Category category;
        try {
            category = mCategories.getValue().get(position);
        } catch (Exception exception) {
            Log.e(TAG, "can not delete the category: " + exception.getMessage());
            listener.action(false);
            return;
        }
        MyRequest request = new MyRequest();
        request.setMethod(Request.Method.DELETE);
        request.setUrl(Route.getCategoriesDeleteRoute(mProjectId, category.getId()));
        request.addAuthorizationHeader(AccountUtils.getAccessToken());

        request.setResponse(response -> {
            mCategories.getValue().remove(position);
            mCategories.setValue(mCategories.getValue());
            listener.action(true);
        });
        request.setErrorHandler(new MyRequest.ErrorHandler() {
            @Override
            public void action() {
                listener.action(false);
            }
        });
        MyRequestQueue.getInstance(getApplication()).addToRequestQueue(request);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;

        private final int mProjectId;

        public Factory(@NonNull Application application, int projectId) {
            this.mApplication = application;
            this.mProjectId = projectId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CategoryCollectionViewModel(mApplication, mProjectId);
        }
    }
}
