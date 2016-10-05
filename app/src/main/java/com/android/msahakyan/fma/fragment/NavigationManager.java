package com.android.msahakyan.fma.fragment;

import android.support.v4.app.Fragment;

import com.android.msahakyan.fma.adapter.delegates.AlbumAdapterDelegate;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.model.Genre;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.util.Item;

import java.util.List;

/**
 * Created by msahakyan on 01/07/16.
 */

public interface NavigationManager {

    void showGenresFragment();

    void showAlbumsFragment();

    void showArtistsFragment();

    void showArtistDetailFragment(Artist artist);

    void showTracksFragmentByGenre(Genre genre);

    void showArtistDetailFragment(String artistUrl);

    void showAlbumDetailFragment(Album album, AlbumAdapterDelegate.AlbumViewHolder holder);

    void showTrackPlayFragment(List<Item> tracks, int position);

    void showMainPagerFragment();

    Fragment getCurrentFragment();

    void onBackPress();

    void showSearchSuggestionsFragment();

    void showSearchResultsFragment(List<SearchResultItem> response, String mQuery);
}
