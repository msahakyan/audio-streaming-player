package com.android.msahakyan.fma.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.android.msahakyan.fma.util.Item;
import com.google.gson.annotations.SerializedName;

/**
 * Created by msahakyan on 13/07/16.
 */

public class Curator implements Item {

    @SerializedName("curator_id")
    private long id;

    @SerializedName("curator_handle")
    private String handle;

    @SerializedName("curator_url")
    private String url;

    @SerializedName("curator_image_file")
    private String image;

    @SerializedName("curator_title")
    private String title;

    @SerializedName("curator_bio")
    private String bio;

    @SerializedName("curator_date_created")
    private String creationDate;

    @SerializedName("curator_favorites")
    private int favouritesCount;

    @SerializedName("curator_comments")
    private int commentsCount;

    @SerializedName("curator_playlists")
    private int playlistsCount;

    public Curator() {
    }

    protected Curator(Parcel in) {
        id = in.readLong();
        handle = in.readString();
        url = in.readString();
        image = in.readString();
        title = in.readString();
        bio = in.readString();
        creationDate = in.readString();
        favouritesCount = in.readInt();
        commentsCount = in.readInt();
        playlistsCount = in.readInt();
    }

    public static final Creator<Curator> CREATOR = new Creator<Curator>() {
        @Override
        public Curator createFromParcel(Parcel in) {
            return new Curator(in);
        }

        @Override
        public Curator[] newArray(int size) {
            return new Curator[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getPlaylistsCount() {
        return playlistsCount;
    }

    public void setPlaylistsCount(int playlistsCount) {
        this.playlistsCount = playlistsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(handle);
        dest.writeString(url);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(bio);
        dest.writeString(creationDate);
        dest.writeInt(favouritesCount);
        dest.writeInt(commentsCount);
        dest.writeInt(playlistsCount);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof Curator)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        Curator other = (Curator) another;
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
