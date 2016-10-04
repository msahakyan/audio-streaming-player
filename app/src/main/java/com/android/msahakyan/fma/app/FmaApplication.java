package com.android.msahakyan.fma.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.msahakyan.fma.BuildConfig;
import com.android.msahakyan.fma.component.DaggerNetworkComponent;
import com.android.msahakyan.fma.component.NetworkComponent;
import com.android.msahakyan.fma.module.AppModule;
import com.android.msahakyan.fma.module.NetworkModule;
import com.android.msahakyan.fma.module.StorageModule;
import com.android.msahakyan.fma.util.cache.BitmapLruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import io.branch.referral.Branch;
import timber.log.Timber;

/**
 * Created by msahakyan on 01/07/16.
 */

public class FmaApplication extends Application {

    private static final String TAG = FmaApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static FmaApplication sInstance;
    private static NetworkComponent mNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Timber new plant
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Initialize the Branch object
        Branch.getAutoInstance(this);


        sInstance = this;
        mNetworkComponent = DaggerNetworkComponent.builder()
            .appModule(new AppModule(this))
            .storageModule(new StorageModule())
            .networkModule(new NetworkModule())
            .build();
    }

    public static NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }

    public static synchronized FmaApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, BitmapLruCache.getInstance());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, null);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
