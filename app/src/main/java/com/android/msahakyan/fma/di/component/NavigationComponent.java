package com.android.msahakyan.fma.di.component;

import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.di.module.NavigationModule;
import com.android.msahakyan.fma.di.scope.NavigationScope;

import dagger.Subcomponent;

/**
 * @author msahakyan
 */

@NavigationScope
@Subcomponent(modules = {NavigationModule.class})
public interface NavigationComponent {

    /**
     * Injects NavigationManager instance into target = `MainActivity`
     *
     * @param target The injection target
     */
    void inject(MainActivity target);
}
