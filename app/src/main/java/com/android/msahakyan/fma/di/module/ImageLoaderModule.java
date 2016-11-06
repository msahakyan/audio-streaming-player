package com.android.msahakyan.fma.di.module;

import com.android.msahakyan.fma.util.cache.BitmapLruCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author msahakyan
 */

@Module
public class ImageLoaderModule {

    /**
     * Provides singleton instance of ImageCache
     *
     * @return a instance of {@link BitmapLruCache}
     */
    @Singleton
    @Provides
    ImageLoader.ImageCache provideImageCache() {
        return new BitmapLruCache();
    }

    /**
     * Provides singleton instance of Volley {@link ImageLoader}
     *
     * @param requestQueue The volley RequestQueue instance
     * @param imageCache   The instance of ImageCache (see: BitmapLruCache)
     * @return a instance of ImageLoader
     */
    @Singleton
    @Provides
    ImageLoader provideImageLoader(RequestQueue requestQueue, ImageLoader.ImageCache imageCache) {
        return new ImageLoader(requestQueue, imageCache);
    }
}
