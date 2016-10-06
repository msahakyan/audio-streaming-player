package com.android.msahakyan.fma.model;

import android.os.Parcel;

import com.android.msahakyan.fma.util.Item;

/**
 * Created by msahakyan on 05/10/16.
 */

public class SearchResultItem implements Item {

    private long trackId;
    private String trackTitle;
    private String artistName;

    public SearchResultItem() {
    }

    protected SearchResultItem(Parcel in) {
        trackId = in.readLong();
        trackTitle = in.readString();
        artistName = in.readString();
    }

    public static final Creator<SearchResultItem> CREATOR = new Creator<SearchResultItem>() {
        @Override
        public SearchResultItem createFromParcel(Parcel in) {
            return new SearchResultItem(in);
        }

        @Override
        public SearchResultItem[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(trackId);
        dest.writeString(trackTitle);
        dest.writeString(artistName);
    }

    @Override
    public void setQualifier(String qualifier) {
        // do nothing
    }

    @Override
    public int compareTo(Object another) {
        if (!(another instanceof SearchResultItem)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        SearchResultItem other = (SearchResultItem) another;
        return this.trackTitle.compareToIgnoreCase(other.trackTitle);
    }
}
