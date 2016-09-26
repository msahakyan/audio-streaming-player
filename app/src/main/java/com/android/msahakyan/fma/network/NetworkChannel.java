package com.android.msahakyan.fma.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.util.AppUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.android.msahakyan.fma.network.Endpoint.BASE_URL;

/**
 * Created by msahakyan on 01/07/16.
 */

public class NetworkChannel implements INetworkChannel {

    private static final int REQUEST_TIMEOUT = 5000; // milliseconds
    private static final int MAX_RETRIES = 3;
    private static final int BACKOFF_MULTIPLIER = 1;

    private RequestQueue mRequestQueue;

    public NetworkChannel() {
        mRequestQueue = FmaApplication.getInstance().getRequestQueue();
    }

    @Nullable
    @Override
    public <T> CancelableRequest<T> request(int method, @Nullable String endpoint, @NonNull NetworkRequestListener<T> networkRequestListener, @Nullable NetworkResponseParser<T> parser, @Nullable Map<String, String> params, Request.Priority priority, boolean appendBaseUrl) {
        String url = appendBaseUrl ? getUrl(method, BASE_URL, endpoint, params) :
            getUrl(method, endpoint, params);

        Map<String, String> headers = getDefaultHeaders();

        GsonRequest<T> request = new GsonRequest<>(method, url, networkRequestListener, parser);
        request.setTag(url);
        request.setParams(params);
        request.setHeaders(headers);
        request.setRetryPolicy(new CustomRetryPolicy(REQUEST_TIMEOUT, MAX_RETRIES, BACKOFF_MULTIPLIER));
        request.setPriority(priority == null ? Request.Priority.NORMAL : priority);
        mRequestQueue.add(request);

        Timber.d("HTTP Request: " + method + "\nURL: " + url);

        return new CancelableRequest<>(request);
    }

    @Override
    public <T> CancelableRequest<T> requestGet(@Nullable String endpoint, @NonNull NetworkRequestListener<T> requestListener, @NonNull NetworkResponseParser<T> parser, @Nullable Map<String, String> params, Request.Priority priority) {
        return request(Request.Method.GET, endpoint, requestListener, parser, params, priority, true);
    }

    public <T> CancelableRequest<T> specialRequestGet(@Nullable String endpoint, @NonNull NetworkRequestListener<T> requestListener, @NonNull NetworkResponseParser<T> parser, @Nullable Map<String, String> params, Request.Priority priority) {
        return request(Request.Method.GET, endpoint, requestListener, parser, params, priority, false);
    }

    private static String getUrl(int method, String baseUrl, String endpoint, Map<String, String> params) {
        if (params != null) {
            // Prevent null values
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                }
            }
        }

        if (method == Request.Method.GET && !AppUtils.isEmpty(params)) {
            final StringBuilder result = new StringBuilder(baseUrl + endpoint);
            for (String key : params.keySet()) {
                try {
                    final String encodedKey = URLEncoder.encode(key, "UTF-8");
                    final String encodedValue = URLEncoder.encode(params.get(key), "UTF-8");
                    if (result.toString().contains("?")) {
                        result.append("&");
                    } else {
                        result.append("?");
                    }
                    result.append(encodedKey);
                    result.append("=");
                    result.append(encodedValue);
                } catch (Exception e) {
                    Timber.w(e, "Something went wrong!");
                }
            }
            return result.toString();
        } else {
            return baseUrl + endpoint;
        }
    }

    private static String getUrl(int method, String endpoint, Map<String, String> params) {
        if (params != null) {
            // Prevent null values
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                }
            }
        }

        if (method == Request.Method.GET && !AppUtils.isEmpty(params)) {
            final StringBuilder result = new StringBuilder(endpoint);
            for (String key : params.keySet()) {
                try {
                    final String encodedKey = URLEncoder.encode(key, "UTF-8");
                    final String encodedValue = URLEncoder.encode(params.get(key), "UTF-8");
                    if (result.toString().contains("?")) {
                        result.append("&");
                    } else {
                        result.append("?");
                    }
                    result.append(encodedKey);
                    result.append("=");
                    result.append(encodedValue);
                } catch (Exception e) {
                    Timber.w(e, "Something went wrong!");
                }
            }
            return result.toString();
        } else {
            return endpoint;
        }
    }

    private Map<String, String> getDefaultHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return headers;
    }
}
