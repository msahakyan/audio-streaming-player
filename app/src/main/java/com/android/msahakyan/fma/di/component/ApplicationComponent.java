package com.android.msahakyan.fma.di.component;

import com.android.msahakyan.fma.adapter.delegates.BaseAdapterDelegate;
import com.android.msahakyan.fma.di.module.ApiModule;
import com.android.msahakyan.fma.di.module.ApplicationModule;
import com.android.msahakyan.fma.di.module.ImageLoaderModule;
import com.android.msahakyan.fma.di.module.NavigationModule;
import com.android.msahakyan.fma.di.module.StorageModule;
import com.android.msahakyan.fma.fragment.AlbumDetailFragment;
import com.android.msahakyan.fma.fragment.AlbumsFragment;
import com.android.msahakyan.fma.fragment.ArtistDetailFragment;
import com.android.msahakyan.fma.fragment.ArtistsFragment;
import com.android.msahakyan.fma.fragment.GenresFragment;
import com.android.msahakyan.fma.fragment.SearchSuggestionsFragment;
import com.android.msahakyan.fma.fragment.TrackDetailFragment;
import com.android.msahakyan.fma.fragment.TracksFragment;
import com.android.msahakyan.fma.network.FmaApiService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by msahakyan on 02/07/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class, ImageLoaderModule.class, StorageModule.class})
public interface ApplicationComponent {

    // Injects ApiService instance
    void inject(FmaApiService fmaApiService);

    void inject(BaseAdapterDelegate target);

    // Inject FmaApiService instance
    void inject(AlbumsFragment target);

    // Inject FmaApiService instance
    void inject(ArtistsFragment target);

    // Inject FmaApiService instance
    void inject(GenresFragment target);

    // Inject FmaApiService instance
    void inject(TracksFragment target);

    // Inject FmaApiService instance
    void inject(SearchSuggestionsFragment target);

    // Injects ImageLoader instance
    void inject(AlbumDetailFragment target);

    // Injects ImageLoader instance
    void inject(ArtistDetailFragment target);

    // Injects ImageLoader instance
    void inject(TrackDetailFragment target);

    NavigationComponent plus(NavigationModule navigationModule);
}
