package com.android.msahakyan.fma.network.parser;

import com.android.msahakyan.fma.network.INetworkChannel;
import com.android.msahakyan.fma.util.Item;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * Created by msahakyan on 25/07/16.
 */

public class TrackDetailParser<T extends Item> implements INetworkChannel.NetworkResponseParser<T> {

    private final Gson mGson = new Gson();

    private final Type mPageType;

    public TrackDetailParser(Type pageType) {
        mPageType = pageType;
    }

    @Override
    public T parseNetworkResponse(JsonElement networkResponse) {
        JsonObject responseObject = networkResponse.getAsJsonObject();
        T item = mGson.fromJson(responseObject, mPageType);
        return item;
    }
}
