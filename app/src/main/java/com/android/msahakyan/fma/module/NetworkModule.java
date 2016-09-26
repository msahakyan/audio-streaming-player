package com.android.msahakyan.fma.module;

import com.android.msahakyan.fma.network.NetworkChannel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by msahakyan on 01/07/16.
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    public NetworkChannel provideNetworkChannel() {
        return new NetworkChannel();
    }
}
