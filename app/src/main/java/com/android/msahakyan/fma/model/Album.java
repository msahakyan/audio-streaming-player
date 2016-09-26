package com.android.msahakyan.fma.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.android.msahakyan.fma.util.Item;
import com.google.gson.annotations.SerializedName;

/**
 * Created by msahakyan on 12/07/16.
 */

public class Album implements Item {

    @SerializedName("album_id")
    private long id;

    @SerializedName("album_title")
    private String title;

    @SerializedName("album_url")
    private String url;

    @SerializedName("album_type")
    private String type;

    @SerializedName("artist_name")
    private String artistName;

    @SerializedName("artist_url")
    private String artistUrl;

    @SerializedName("album_producer")
    private String producer;

    @SerializedName("album_information")
    private String information;

    @SerializedName("album_date_released")
    private String releaseDate;

    @SerializedName("album_comments")
    private int commentsCount;

    @SerializedName("album_favorites")
    private int favouritesCount;

    @SerializedName("album_tracks")
    private int tracksCount;

    @SerializedName("album_listens")
    private int listensCount;

    @SerializedName("album_date_created")
    private String creationDate;

    @SerializedName("album_image_file")
    private String imageFile;

    public Album() {
    }

    protected Album(Parcel in) {
        id = in.readLong();
        title = in.readString();
        url = in.readString();
        type = in.readString();
        artistName = in.readString();
        artistUrl = in.readString();
        producer = in.readString();
        information = in.readString();
        releaseDate = in.readString();
        commentsCount = in.readInt();
        favouritesCount = in.readInt();
        tracksCount = in.readInt();
        listensCount = in.readInt();
        creationDate = in.readString();
        imageFile = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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

    public int getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(int tracksCount) {
        this.tracksCount = tracksCount;
    }

    public int getListensCount() {
        return listensCount;
    }

    public void setListensCount(int listensCount) {
        this.listensCount = listensCount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
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
        dest.writeString(type);
        dest.writeString(artistName);
        dest.writeString(artistUrl);
        dest.writeString(producer);
        dest.writeString(information);
        dest.writeString(releaseDate);
        dest.writeInt(commentsCount);
        dest.writeInt(favouritesCount);
        dest.writeInt(tracksCount);
        dest.writeInt(listensCount);
        dest.writeString(creationDate);
        dest.writeString(imageFile);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof Album)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        Album other = (Album) another;
        return this.title.compareToIgnoreCase(other.title);
    }

    private String qualifier;

    @Override
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return this.qualifier;
    }
}
