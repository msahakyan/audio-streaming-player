package com.android.msahakyan.fma.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.android.msahakyan.fma.util.Item;
import com.google.gson.annotations.SerializedName;

/**
 * Created by msahakyan on 12/07/16.
 */

public class Genre implements Item {

    @SerializedName("genre_id")
    private long id;

    @SerializedName("genre_title")
    private String title;

    @SerializedName("genre_url")
    private String url;

    @SerializedName("genre_color")
    private String color;

    public Genre() {
    }

    protected Genre(Parcel in) {
        id = in.readLong();
        title = in.readString();
        url = in.readString();
        color = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        dest.writeString(color);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof Genre)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        Genre other = (Genre) another;
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
