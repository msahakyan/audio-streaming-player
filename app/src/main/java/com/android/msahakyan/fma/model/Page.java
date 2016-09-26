package com.android.msahakyan.fma.model;

import com.android.msahakyan.fma.util.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by msahakyan on 25/07/16.
 */

public class Page<T extends Item> {

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("errors")
    private List<String> errors;

    @SerializedName("total")
    private long total;

    @SerializedName("total_pages")
    private long totalPages;

    @SerializedName("page")
    private long page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("dataset")
    private List<T> items;

    public Page() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

