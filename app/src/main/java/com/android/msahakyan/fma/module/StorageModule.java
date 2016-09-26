package com.android.msahakyan.fma.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by msahakyan on 01/07/16.
 */

@Module
public class StorageModule {

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }
}
