package com.android.msahakyan.fma.di.module;

import android.support.v7.app.AppCompatActivity;

import com.android.msahakyan.fma.di.scope.NavigationScope;
import com.android.msahakyan.fma.fragment.FragmentNavigationManager;
import com.android.msahakyan.fma.fragment.NavigationManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author msahakyan
 */

@Module
public class NavigationModule {

    private AppCompatActivity activity;

    public NavigationModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Provides instance of {@link NavigationManager}
     *
     * @return a instance of FragmentNavigationManager with @NavigationScope
     */
    @NavigationScope
    @Provides
    NavigationManager provideNavigationManager() {
        return new FragmentNavigationManager(activity);
    }
}
