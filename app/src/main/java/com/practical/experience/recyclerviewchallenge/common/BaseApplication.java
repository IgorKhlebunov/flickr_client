package com.practical.experience.recyclerviewchallenge.common;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class BaseApplication extends Application{
    private static BaseApplication sInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
               mRequestQueue = Volley.newRequestQueue(this);

        return mRequestQueue;
    }

    public synchronized static BaseApplication getInstance() {
        return sInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
