package com.android.msahakyan.fma.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.android.msahakyan.fma.util.Item;

/**
 * Created by msahakyan on 05/10/16.
 */

public class SearchItem implements Item {

    private String term;

    protected SearchItem(Parcel in) {
        term = in.readString();
    }

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public void setQualifier(String qualifier) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(term);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof SearchItem)) {
            throw new IllegalArgumentException("Object: " + this.getClass().getSimpleName() + " can't compare to: " + another.getClass().getSimpleName());
        }
        SearchItem other = (SearchItem) another;
        return this.term.compareToIgnoreCase(other.term);
    }
}
