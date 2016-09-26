package com.android.msahakyan.fma.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.android.msahakyan.fma.util.Item;
import com.google.gson.annotations.SerializedName;

/**
 * Created by msahakyan on 13/07/16.
 */

public class Artist implements Item {

    @SerializedName("artist_id")
    private long id;

    @SerializedName("artist_url")
    private String url;

    @SerializedName("artist_name")
    private String name;

    @SerializedName("artist_bio")
    private String bio;

    @SerializedName("artist_website")
    private String website;

    @SerializedName("artist_contact")
    private String contact;

    @SerializedName("artist_comments")
    private int commentsCount;

    @SerializedName("artist_favorites")
    private int favouritesCount;

    @SerializedName("artist_date_created")
    private String creationDate;

    @SerializedName("artist_image_file")
    private String image;

    @SerializedName("artist_location")
    private String location;

    public Artist() {
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        url = in.readString();
        name = in.readString();
        bio = in.readString();
        website = in.readString();
        contact = in.readString();
        commentsCount = in.readInt();
        favouritesCount = in.readInt();
        creationDate = in.readString();
        image = in.readString();
        location = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(bio);
        dest.writeString(website);
        dest.writeString(contact);
        dest.writeInt(commentsCount);
        dest.writeInt(favouritesCount);
        dest.writeString(creationDate);
        dest.writeString(image);
        dest.writeString(location);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof Artist)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        Artist other = (Artist) another;
        return this.name.compareToIgnoreCase(other.name);
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
