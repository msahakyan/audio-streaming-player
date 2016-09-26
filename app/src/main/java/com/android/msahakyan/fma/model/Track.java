
package com.android.msahakyan.fma.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.android.msahakyan.fma.util.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by msahakyan on 03/07/16.
 */

public class Track implements Item, Parcelable {

    @SerializedName("track_id")
    private long id;

    @SerializedName("track_title")
    private String title;

    @SerializedName("track_url")
    private String url;

    @SerializedName("track_image_file")
    private String image;

    @SerializedName("artist_id")
    private long artistId;

    @SerializedName("artist_name")
    private String artistName;

    @SerializedName("artist_url")
    private String artistUrl;

    @SerializedName("artist_website")
    private String artistWebsite;

    @SerializedName("album_id")
    private long albumId;

    @SerializedName("album_title")
    private String albumTitle;

    @SerializedName("album_url")
    private String albumUrl;

    @SerializedName("track_duration")
    private String duration;

    @SerializedName("track_file_url")
    private String fileUrl;

    @SerializedName("track_listen_url")
    private String listenUrl;

    @SerializedName("track_genres")
    private List<Genre> genres;

    @SerializedName("track_file")
    private String file;

    @SerializedName("track_date_created")
    private String creationDate;

    @SerializedName("track_comments")
    private int commentsCount;

    @SerializedName("track_favorites")
    private int favouritesCount;

    @SerializedName("track_listens")
    private int listensCount;

    @SerializedName("track_downloads")
    private int downloadsCount;

    private Bitmap imageBitmap;

    private String qualifier;

    public Track() {
    }

    protected Track(Parcel in) {
        id = in.readLong();
        title = in.readString();
        url = in.readString();
        image = in.readString();
        artistId = in.readLong();
        artistName = in.readString();
        artistUrl = in.readString();
        artistWebsite = in.readString();
        albumId = in.readLong();
        albumTitle = in.readString();
        albumUrl = in.readString();
        duration = in.readString();
        fileUrl = in.readString();
        listenUrl = in.readString();
        genres = in.createTypedArrayList(Genre.CREATOR);
        file = in.readString();
        creationDate = in.readString();
        commentsCount = in.readInt();
        favouritesCount = in.readInt();
        listensCount = in.readInt();
        downloadsCount = in.readInt();
        imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
    }

    public String getArtistWebsite() {
        return artistWebsite;
    }

    public void setArtistWebsite(String artistWebsite) {
        this.artistWebsite = artistWebsite;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getListenUrl() {
        return listenUrl;
    }

    public void setListenUrl(String listenUrl) {
        this.listenUrl = listenUrl;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getListensCount() {
        return listensCount;
    }

    public void setListensCount(int listensCount) {
        this.listensCount = listensCount;
    }

    public int getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(int downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(image);
        dest.writeLong(artistId);
        dest.writeString(artistName);
        dest.writeString(artistUrl);
        dest.writeString(artistWebsite);
        dest.writeLong(albumId);
        dest.writeString(albumTitle);
        dest.writeString(albumUrl);
        dest.writeString(duration);
        dest.writeString(fileUrl);
        dest.writeString(listenUrl);
        dest.writeTypedList(genres);
        dest.writeString(file);
        dest.writeString(creationDate);
        dest.writeInt(commentsCount);
        dest.writeInt(favouritesCount);
        dest.writeInt(listensCount);
        dest.writeInt(downloadsCount);
        dest.writeParcelable(imageBitmap, flags);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof Track)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        Track other = (Track) another;
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return this.qualifier;
    }
}
