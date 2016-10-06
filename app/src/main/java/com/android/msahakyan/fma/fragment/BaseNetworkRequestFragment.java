package com.android.msahakyan.fma.fragment;

import android.os.Bundle;

import com.android.msahakyan.fma.network.CancelableRequest;
import com.android.msahakyan.fma.network.NetworkRequestListener;

import timber.log.Timber;

public abstract class BaseNetworkRequestFragment<T> extends BaseLceFragment {

    private CancelableRequest mCurrentRequest;

    private NetworkRequestListener<T> mNetworkRequestListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkRequestListener = new NetworkRequestListener<T>() {
            @Override
            public void onSuccess(T response, int statusCode) {
                if (mActivity == null || getView() == null) {
                    Timber.w("Received response when the fragment has already been destroyed");
                    return;
                }
                BaseNetworkRequestFragment.this.onSuccess(response, statusCode);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                BaseNetworkRequestFragment.this.onError(statusCode, errorMessage);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRequestActive()) {
            mCurrentRequest.cancel();
            Timber.d("Cancelling request");
        }
        mCurrentRequest = null;
    }

    protected boolean isRequestActive() {
        return mCurrentRequest != null && mCurrentRequest.isOngoing();
    }

    protected void onSuccess(T response, int statusCode) {
        showContentView();
    }

    protected void onError(int statusCode, String errorMessage) {
        Timber.w(errorMessage);
        hideProgressView();
        showErrorView();
    }

    public NetworkRequestListener<T> getNetworkListener() {
        return mNetworkRequestListener;
    }

    protected CancelableRequest getNetworkRequest() {
        return mCurrentRequest;
    }

    protected void setNetworkRequest(CancelableRequest request) {
        mCurrentRequest = request;
    }
}
