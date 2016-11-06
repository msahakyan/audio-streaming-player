package com.android.msahakyan.fma.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by msahakyan on 01/07/16.
 */

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    /**
     * Provides singleton instance of the {@link Application} object
     *
     * @return a application instance
     */
    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }
}
