package com.android.msahakyan.fma.util;

import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.model.Curator;
import com.android.msahakyan.fma.model.Genre;
import com.android.msahakyan.fma.model.Track;

import java.util.Map;

/**
 * Created by msahakyan on 25/07/16.
 */

public interface Item extends Parcelable, Comparable {

    Map<String, Class<? extends Item>> TYPE_MAP = new ArrayMap<String, Class<? extends Item>>() {{
        put(ItemType.GENRE.type, Genre.class);
        put(ItemType.CURATOR.type, Curator.class);
        put(ItemType.ARTIST.type, Artist.class);
        put(ItemType.ALBUM.type, Album.class);
        put(ItemType.TRACK.type, Track.class);
    }};

    enum ItemType {
        GENRE("genre"),
        CURATOR("curator"),
        ARTIST("artist"),
        ALBUM("album"),
        TRACK("track");

        String type;

        ItemType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static ItemType byTypeName(String type) {
            for (ItemType itemType : values()) {
                if (itemType.type.equals(type))
                    return itemType;
            }
            Log.w("Item", "Could not find ItemType: " + type);
            return null;
        }
    }

    void setQualifier(String qualifier);
}
