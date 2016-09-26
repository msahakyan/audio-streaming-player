package com.android.msahakyan.fma.network;

import android.support.annotation.Nullable;

/**
 * Created by msahakyan on 24/07/16.
 */

public interface NetworkRequestListener<T> {

    /**
     * Success callback
     *
     * @param response
     * @param statusCode
     */
    void onSuccess(@Nullable T response, int statusCode);

    /**
     * Error callback
     *
     * @param statusCode
     * @param errorMessage
     */
    void onError(int statusCode, String errorMessage);
}
