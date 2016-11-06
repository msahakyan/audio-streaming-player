package com.android.msahakyan.fma.application;

import android.app.Application;
import android.content.Context;

import com.android.msahakyan.fma.BuildConfig;
import com.android.msahakyan.fma.di.component.ApplicationComponent;
import com.android.msahakyan.fma.di.component.DaggerApplicationComponent;
import com.android.msahakyan.fma.di.module.ApiModule;
import com.android.msahakyan.fma.di.module.ApplicationModule;
import com.android.msahakyan.fma.di.module.ImageLoaderModule;
import com.android.msahakyan.fma.di.module.StorageModule;

import io.branch.referral.Branch;
import timber.log.Timber;

/**
 * Created by msahakyan on 01/07/16.
 */

public class FmaApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Timber new plant
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Initialize the Branch object
        Branch.getAutoInstance(this);


        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .apiModule(new ApiModule())
            .imageLoaderModule(new ImageLoaderModule())
            .storageModule(new StorageModule())
            .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static FmaApplication get(Context context) {
        return (FmaApplication) context.getApplicationContext();
    }
}
