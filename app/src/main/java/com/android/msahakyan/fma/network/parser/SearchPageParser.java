package com.android.msahakyan.fma.network.parser;

import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.network.INetworkChannel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msahakyan on 05/10/16.
 */
public class SearchPageParser implements INetworkChannel.NetworkResponseParser<List<SearchResultItem>> {

    private static final String ATTR_A_ROWS = "aRows";
    private final Gson mGson = new Gson();

    @Override
    public List<SearchResultItem> parseNetworkResponse(JsonElement networkResponse) {
        JsonObject responseObject = networkResponse.getAsJsonObject();

        List<String> results = null;

        if (responseObject.has(ATTR_A_ROWS)) {
            JsonArray jsonArray = responseObject.getAsJsonArray(ATTR_A_ROWS);
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            results = mGson.fromJson(jsonArray.toString(), listType);
        }

        List<SearchResultItem> searchResults = null;
        if (results != null) {
            searchResults = new ArrayList<>(results.size());

            for (String row : results) {
                SearchResultItem item = new SearchResultItem();
                item.setTrackId(fetchTrackId(row));
                item.setTrackTitle(fetchTrackTitle(row));
                item.setArtistName(fetchArtistName(row));

                searchResults.add(item);
            }
        }

        return searchResults;
    }

    private long fetchTrackId(String row) {
        if (row == null) {
            return 0;
        }
        return Long.parseLong(row.substring(row.lastIndexOf('(') + 1, row.lastIndexOf(')')));
    }

    private String fetchTrackTitle(String row) {
        if (row == null) {
            return null;
        }
        return row.substring(row.indexOf(']') + 2, row.lastIndexOf('(') - 1);
    }

    private String fetchArtistName(String row) {
        if (row == null) {
            return null;
        }
        return row.substring(row.indexOf('[') + 1, row.indexOf(']'));
    }
}
