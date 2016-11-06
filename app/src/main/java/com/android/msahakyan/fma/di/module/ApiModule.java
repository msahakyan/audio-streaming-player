package com.android.msahakyan.fma.di.module;

import android.app.Application;

import com.android.msahakyan.fma.network.ApiService;
import com.android.msahakyan.fma.network.FmaApiService;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by msahakyan on 01/07/16.
 */

@Module
public class ApiModule {

    /**
     * Provides singleton instance of Volley {@link RequestQueue}
     *
     * @param application The application instance
     * @return a instance of request queue
     */
    @Singleton
    @Provides
    RequestQueue provideRequestQueue(Application application) {
        return Volley.newRequestQueue(application);
    }

    /**
     * Provides singleton instance of {@link ApiService} class
     *
     * @return a instance of ApiService
     */
    @Provides
    @Singleton
    ApiService provideApiService(RequestQueue requestQueue) {
        return new ApiService(requestQueue);
    }

    @Singleton
    @Provides
    FmaApiService provideFmaApiService(ApiService apiService) {
        return new FmaApiService(apiService);
    }
}
