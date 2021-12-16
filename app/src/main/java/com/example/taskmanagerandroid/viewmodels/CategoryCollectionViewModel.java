package com.example.taskmanagerandroid.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.Category;
import com.example.taskmanagerandroid.utils.MyRequest;
import com.example.taskmanagerandroid.utils.MyRequestQueue;
import com.example.taskmanagerandroid.utils.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class CategoryCollectionViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Category>> mCategories;
    private final int mProjectId;

    public CategoryCollectionViewModel(@NonNull Application application, int projectId) {
        super(application);
        mProjectId = projectId;
        mCategories = new MutableLiveData<>();
        MyRequest request = new MyRequest();
        request.setUrl(Route.getCategoriesAllRoute(mProjectId));
        String access_token = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("access_token", "");
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
                    Log.v("fd", categoryObject.toString());
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
