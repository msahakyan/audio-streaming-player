package com.android.msahakyan.fma.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by msahakyan on 24/07/16.
 */

class GsonRequest<T> extends Request<GsonRequest.ProtocolObject<T>> {

    private final Gson mGson = new Gson();
    private final NetworkRequestListener<T> mListener;
    private ResponseEnvelopeParser<T> mNetworkResponseEnvelopeParser;
    private final ApiService.NetworkResponseParser<T> mParser;

    private Priority mPriority = Priority.NORMAL;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;


    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     */
    GsonRequest(int method, String url, NetworkRequestListener<T> listener, ApiService.NetworkResponseParser<T> parser) {
        super(method, url, null);
        mListener = listener;
        mParser = parser;

        mNetworkResponseEnvelopeParser = new DefaultEnvelopeParser();
    }

    public void setNetworkResponseEnvelopeParser(@NonNull ResponseEnvelopeParser<T> networkResponseEnvelopeParser) {
        mNetworkResponseEnvelopeParser = networkResponseEnvelopeParser;
    }

    @Override
    protected void deliverResponse(ProtocolObject<T> response) {
        if (mListener != null)
            mListener.onSuccess(response.data, response.statusCode);

    }

    @Override
    public void deliverError(VolleyError error) {
        Timber.e(error, null);
        markDelivered();
        int statusCode = 0;
        String errorMessage = error.getMessage();
        if (error.networkResponse != null) {
            statusCode = error.networkResponse.statusCode;
            try {
                ProtocolObject<T> responseObject = mNetworkResponseEnvelopeParser.parseResponseEnvelope(error.networkResponse, null);
                if (responseObject == null) {
                    errorMessage = "Unknown Error";
                } else {
                    errorMessage = responseObject.errorMessage;
                }
            } catch (UnsupportedEncodingException e) {
                errorMessage = e.getMessage();
            }
        }
        if (mListener != null) {
            mListener.onError(statusCode, errorMessage);
        } else {
            throw new IllegalArgumentException("A a request has failed and has NO listener");
        }
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    protected Response<ProtocolObject<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            ProtocolObject<T> responseObject = mNetworkResponseEnvelopeParser.parseResponseEnvelope(response, mParser);
            if (responseObject == null) {
                Timber.w("Error parsing data - responseObject is null");
                return Response.error(new ParseError());
            }
            return Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            Timber.w(e, null);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders == null ? super.getHeaders() : mHeaders;
    }

    void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    void setParams(Map<String, String> params) {
        mParams = params;
    }

    static class ProtocolObject<T> {

        @Nullable
        T data;

        int statusCode;

        String errorMessage;

        ProtocolObject() {
        }
    }

    private class DefaultEnvelopeParser implements ResponseEnvelopeParser<T> {

        @Override
        public ProtocolObject<T> parseResponseEnvelope(NetworkResponse response, INetworkUtils.NetworkResponseParser<T> parser)
            throws UnsupportedEncodingException {
            String json = new String(response.data, "UTF-8");
            Timber.v("Network response json : " + json);
            JsonObject jsonObject = null;
            try {
                jsonObject = mGson.fromJson(json, JsonObject.class);
            } catch (Exception e) {
                Timber.e(e, "Parse errorMessage. Url: " + getUrl());
            }
            if (jsonObject == null) {
                return null;
            }
            ProtocolObject<T> responseObject = new ProtocolObject<>();
            responseObject.statusCode = response.statusCode;

            if (parser != null) {
                responseObject.data = parser.parseNetworkResponse(jsonObject);
            }

            return responseObject;
        }
    }

    interface ResponseEnvelopeParser<T> {
        ProtocolObject<T> parseResponseEnvelope(NetworkResponse response, INetworkUtils.NetworkResponseParser<T> parser)
            throws UnsupportedEncodingException;
    }
}
