package com.android.msahakyan.fma.network;

/**
 * @author msahakyan
 */
public interface Endpoint {
    String BASE_URL = "https://freemusicarchive.org/api";
    String CURATORS = "/get/curators.json";
    String ALBUMS = "/get/albums.json";
    String TRACKS = "/get/tracks.json";
    String ARTISTS = "/get/artists.json";
    String GENRES = "/get/genres.json";

    String TRACK_DETAIL = "http://freemusicarchive.org/services/track/single/";
}
