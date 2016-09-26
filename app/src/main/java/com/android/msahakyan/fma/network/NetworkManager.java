package com.android.msahakyan.fma.network;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.model.Genre;
import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.model.Track;
import com.android.msahakyan.fma.network.parser.PageParser;
import com.android.msahakyan.fma.network.parser.TrackDetailParser;
import com.android.msahakyan.fma.util.Constants;
import com.android.msahakyan.fma.util.Item;
import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Created by msahakyan on 25/07/16.
 */

public class NetworkManager {

    private static final int DEFAULT_PAGE_SIZE = 100;

    @Inject
    NetworkChannel mNetworkChannel;

    public NetworkManager() {
        FmaApplication.getNetworkComponent().inject(this);
    }

    // Genres
    public CancelableRequest<Page<Item>> getGenres(@NonNull NetworkRequestListener<Page<Item>> requestListener, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(3);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Genre>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.GENRES, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    // Artists
    public CancelableRequest<Page<Item>> getArtists(@NonNull NetworkRequestListener<Page<Item>> requestListener, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(3);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Artist>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ARTISTS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getArtistById(@NonNull NetworkRequestListener<Page<Item>> requestListener, long artistId) {
        ArrayMap<String, String> params = new ArrayMap<>(2);
        params.put("artist_id", String.valueOf(artistId));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Artist>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ARTISTS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getArtistByName(@NonNull NetworkRequestListener<Page<Item>> requestListener, String artistName) {
        ArrayMap<String, String> params = new ArrayMap<>(2);
        params.put("artist_name", artistName);
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Artist>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ARTISTS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    // Albums
    public CancelableRequest<Page<Item>> getAlbums(@NonNull NetworkRequestListener<Page<Item>> requestListener, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(3);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Album>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ALBUMS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getAlbumById(@NonNull NetworkRequestListener<Page<Item>> requestListener, long albumId) {
        ArrayMap<String, String> params = new ArrayMap<>(2);
        params.put("album_id", String.valueOf(albumId));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Album>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ALBUMS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getAlbumByName(@NonNull NetworkRequestListener<Page<Item>> requestListener, String albumName) {
        ArrayMap<String, String> params = new ArrayMap<>(2);
        params.put("album_title", albumName);
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Album>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ALBUMS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getAlbumsByArtistName(@NonNull NetworkRequestListener<Page<Item>> requestListener, String artistName) {
        ArrayMap<String, String> params = new ArrayMap<>(2);
        params.put("artist_name", artistName);
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Album>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.ALBUMS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    // Tracks
    public CancelableRequest<Page<Item>> getTracks(@NonNull NetworkRequestListener<Page<Item>> requestListener, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(3);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.TRACKS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getTracksByAlbumId(@NonNull NetworkRequestListener<Page<Item>> requestListener, long albumId, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(4);
        params.put("album_id", String.valueOf(albumId));
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.TRACKS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getTracksByAlbumName(@NonNull NetworkRequestListener<Page<Item>> requestListener, String albumName, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(4);
        params.put("album_title", albumName);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.TRACKS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getTracksByArtistId(@NonNull NetworkRequestListener<Page<Item>> requestListener, long artistId, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(4);
        params.put("artist_id", String.valueOf(artistId));
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.TRACKS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getTracksByArtistName(@NonNull NetworkRequestListener<Page<Item>> requestListener, String artistName, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(4);
        params.put("artist_name", artistName);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.TRACKS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Item> getTrackById(@NonNull NetworkRequestListener<Item> requestListener, long trackId) {
        ArrayMap<String, String> params = new ArrayMap<>(1);
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.specialRequestGet(Endpoint.TRACK_DETAIL + trackId + ".json", requestListener, new TrackDetailParser<>(pageType), params, Request.Priority.NORMAL);
    }

    public CancelableRequest<Page<Item>> getTracksByGenreId(@NonNull NetworkRequestListener<Page<Item>> requestListener, long genreId, int page) {
        ArrayMap<String, String> params = new ArrayMap<>(4);
        params.put("genre_id", String.valueOf(genreId));
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(DEFAULT_PAGE_SIZE));
        params.put("api_key", Constants.FMA_API_KEY);
        Type pageType = new TypeToken<Track>() {
        }.getType();

        return mNetworkChannel.requestGet(Endpoint.TRACKS, requestListener, new PageParser<>(pageType), params, Request.Priority.NORMAL);
    }
}
