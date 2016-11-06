package com.android.msahakyan.fma.network.parser;

import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.network.INetworkUtils;
import com.android.msahakyan.fma.util.Item;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msahakyan on 25/07/16.
 */

public class PageParser<T extends Item> implements INetworkUtils.NetworkResponseParser<Page<T>> {

    private static final String ATTR_TITLE = "title";
    private static final String ATTR_MESSAGE = "message";
    private static final String ATTR_ERRORS = "errors";
    private static final String ATTR_TOTAL = "total";
    private static final String ATTR_TOTAL_PAGES = "total_pages";
    private static final String ATTR_PAGE = "page";
    private static final String ATTR_LIMIT = "limit";
    private static final String ATTR_DATASET = "dataset";

    private final Gson mGson = new Gson();

    private final Type mPageType;

    public PageParser(Type pageType) {
        mPageType = pageType;
    }

    @Override
    public Page<T> parseNetworkResponse(JsonElement networkResponse) {
        JsonObject responseObject = networkResponse.getAsJsonObject();
        Page<T> page = new Page<>();

        if (responseObject.has(ATTR_TITLE)) {
            page.setTitle(responseObject.get(ATTR_TITLE).getAsString());
        }

        if (responseObject.has(ATTR_MESSAGE)) {
            page.setMessage(responseObject.get(ATTR_MESSAGE).getAsString());
        }

        if (responseObject.has(ATTR_ERRORS)) {
            JsonArray errorsArray = responseObject.get(ATTR_ERRORS).getAsJsonArray();
            List<String> errors = new ArrayList<>(errorsArray.size());
            page.setErrors(errors);
        }

        if (responseObject.has(ATTR_TOTAL)) {
            page.setTotal(responseObject.get(ATTR_TOTAL).getAsLong());
        }

        if (responseObject.has(ATTR_TOTAL_PAGES)) {
            page.setTotalPages(responseObject.get(ATTR_TOTAL_PAGES).getAsLong());
        }

        if (responseObject.has(ATTR_PAGE)) {
            page.setPage(responseObject.get(ATTR_PAGE).getAsLong());
        }

        if (responseObject.has(ATTR_LIMIT)) {
            page.setLimit(responseObject.get(ATTR_LIMIT).getAsInt());
        }

        JsonArray itemsArray = responseObject.get(ATTR_DATASET).getAsJsonArray();
        List<T> items = new ArrayList<>(itemsArray.size());

        for (JsonElement jsonItem : itemsArray) {
            JsonObject jsonObject = jsonItem.getAsJsonObject();

            T item = mGson.fromJson(jsonObject, mPageType);
            items.add(item);
        }
        page.setItems(items);

        return page;
    }
}
