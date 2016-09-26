package com.android.msahakyan.fma.adapter;

import android.content.Context;

import com.android.msahakyan.fma.adapter.delegates.AlbumAdapterDelegate;
import com.android.msahakyan.fma.adapter.delegates.ArtistAdapterDelegate;
import com.android.msahakyan.fma.adapter.delegates.GenreAdapterDelegate;
import com.android.msahakyan.fma.adapter.delegates.TrackAdapterDelegate;
import com.android.msahakyan.fma.adapter.delegates.TrackWithIconAdapterDelegate;
import com.android.msahakyan.fma.util.Item;

import java.util.Arrays;
import java.util.List;

/**
 * Created by msahakyan on 05/08/16.
 */

public class ItemListAdapter extends BaseListAdapter<Item> {

    public ItemListAdapter(Context context, List<Item> items) {
        super(context, items);

        setAdapterDelegates(Arrays.asList(
            new GenreAdapterDelegate(context),
            new ArtistAdapterDelegate(context),
            new AlbumAdapterDelegate(context),
            new TrackAdapterDelegate(context),
            new TrackWithIconAdapterDelegate(context)
        ));
    }
}
