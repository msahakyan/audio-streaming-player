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
    void setQualifier(String qualifier);
}
