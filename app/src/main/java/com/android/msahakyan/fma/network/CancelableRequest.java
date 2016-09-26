package com.android.msahakyan.fma.network;

import com.android.volley.Request;

/**
 * Created by msahakyan on 24/07/16.
 */

public class CancelableRequest<T> {
    private final Request<GsonRequest.ProtocolObject<T>> mRequest;

    CancelableRequest(Request<GsonRequest.ProtocolObject<T>> request) {
        mRequest = request;
    }

    public void cancel() {
        mRequest.cancel();
    }

    public boolean isOngoing() {
        return !mRequest.hasHadResponseDelivered();
    }

    @Override
    public String toString() {
        return mRequest.toString();
    }
}
