package com.android.msahakyan.fma.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.google.gson.JsonElement;

import java.util.Map;

/**
 * Created by msahakyan on 01/07/16.
 */

public interface INetworkChannel {

    /**
     * A result of the execution will be returned to the registered callback {@link NetworkRequestListener}
     *
     * @param method          HTTP method {@link com.android.volley.Request.Method}
     * @param endpoint        Request URL (where to do the request)
     * @param requestListener Network callback method {@link NetworkRequestListener}
     * @param parser          Network response parser {@link NetworkResponseParser}
     * @param params          Map of request parameters
     * @param priority        Request priority {@link com.android.volley.Request.Priority}
     * @param <T>             Data type
     * @param appendBaseUrl   Should append endpoint to BaseUrl or not
     * @return <code>com.android.msahakyan.fma.network.CancelableRequest</code>
     */
    @Nullable
    <T> CancelableRequest<T> request(int method, @Nullable String endpoint, @NonNull NetworkRequestListener<T> requestListener,
                                     @Nullable NetworkResponseParser<T> parser, @Nullable Map<String, String> params, Request.Priority priority, boolean appendBaseUrl);

    /**
     * Simplified method for executing GET requests
     *
     * @param endpoint        Request URL (where to do the request)
     * @param requestListener Network callback method {@link NetworkRequestListener}
     * @param parser          Network response parser {@link NetworkResponseParser}
     * @param params          Map of request parameters
     * @param priority        Request priority {@link com.android.volley.Request.Priority}
     * @param <T>             Data type
     * @return <code>com.android.msahakyan.fma.network.CancelableRequest</code>
     */
    <T> CancelableRequest<T> requestGet(@Nullable String endpoint, @NonNull NetworkRequestListener<T> requestListener,
                                        @NonNull NetworkResponseParser<T> parser, @Nullable Map<String, String> params, Request.Priority priority);

    /**
     * Simplified method for executing GET requests
     *
     * @param endpoint        Request URL (where to do the request)
     * @param requestListener Network callback method {@link NetworkRequestListener}
     * @param parser          Network response parser {@link NetworkResponseParser}
     * @param params          Map of request parameters
     * @param priority        Request priority {@link com.android.volley.Request.Priority}
     * @param <T>             Data type
     * @return <code>com.android.msahakyan.fma.network.CancelableRequest</code>
     */
    <T> CancelableRequest<T> specialRequestGet(@Nullable String endpoint, @NonNull NetworkRequestListener<T> requestListener,
                                               @NonNull NetworkResponseParser<T> parser, @Nullable Map<String, String> params, Request.Priority priority);

    interface NetworkResponseParser<T> {
        T parseNetworkResponse(JsonElement networkResponse);
    }
}
