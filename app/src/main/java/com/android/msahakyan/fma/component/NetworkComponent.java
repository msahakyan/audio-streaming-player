package com.android.msahakyan.fma.component;

import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.module.AppModule;
import com.android.msahakyan.fma.module.NetworkModule;
import com.android.msahakyan.fma.module.StorageModule;
import com.android.msahakyan.fma.network.NetworkManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by msahakyan on 02/07/16.
 */

@Singleton
@Component(modules = {AppModule.class, StorageModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject(MainActivity activity);

    void inject(NetworkManager networkManager);
}
