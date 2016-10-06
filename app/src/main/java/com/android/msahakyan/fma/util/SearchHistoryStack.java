package com.android.msahakyan.fma.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by msahakyan on 05/10/16.
 */

public class SearchHistoryStack {

    private static final String SEARCH_HISTORY = "search_history";
    private static final String STACK_ITEMS = "stack_items";

    private static final int STACK_SIZE = 10;

    private SharedPreferences mPreferences;
    private final Gson mGson = new Gson();

    public SearchHistoryStack(Context context) {
        mPreferences = context.getApplicationContext()
            .getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
    }

    /**
     * Adds search term to search history stack
     *
     * @param searchTerm The search term
     */
    public void addToHistory(String searchTerm) {
        if (TextUtils.isEmpty(searchTerm.trim())) {
            return;
        }
        saveSearchTerms(acquireSearchTerms().push(searchTerm));
    }

    /**
     * Removes search term from search history stack
     *
     * @param searchTerm The search term
     */
    public void removeFromHistory(String searchTerm) {
        if (TextUtils.isEmpty(searchTerm.trim())) {
            return;
        }
        SearchTermsStack<String> searchTermsStack = acquireSearchTerms();
        if (!searchTermsStack.isEmpty()) {
            saveSearchTerms(searchTermsStack.remove(searchTerm));
        }
    }

    /**
     * Returns all elements from search history stack
     *
     * @return <code>java.util.List<String></code>
     */
    public List<String> getSearchTermsFromHistory() {
        return acquireSearchTerms().getStackElements();
    }

    /**
     * Clears history from search suggestions stack
     */
    public void clearHistory() {
        SearchTermsStack<String> searchTermsStack = acquireSearchTerms();
        if (!searchTermsStack.isEmpty()) {
            saveSearchTerms(searchTermsStack.clear());
        }
    }

    private void saveSearchTerms(SearchTermsStack<String> elements) {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        // Serializing stack
        String jsonSearchTerms = gson.toJson(elements);
        editor.putString(STACK_ITEMS, jsonSearchTerms);
        editor.apply();
    }

    private SearchTermsStack<String> acquireSearchTerms() {
        String jsonSearchItems = mPreferences.getString(STACK_ITEMS, null);
        if (jsonSearchItems != null) {
            return mGson.fromJson(jsonSearchItems, new TypeToken<SearchTermsStack<String>>() {
            }.getType());
        }
        return new SearchTermsStack<>(STACK_SIZE);
    }

    private static class SearchTermsStack<T> {

        private final LinkedList<T> mStorage;
        private int mSize;

        SearchTermsStack(int size) {
            mSize = size > 0 ? size : 1;
            mStorage = new LinkedList<>();
        }

        SearchTermsStack<T> push(T data) {
            if (mStorage.contains(data)) {
                mStorage.remove(data);
            }
            if (mStorage.size() == mSize) {
                T last = mStorage.removeLast();
                Timber.d("Search history stack is full, removing last element [" +
                    last + "]");
            }
            mStorage.addFirst(data);
            return this;
        }

        SearchTermsStack<T> remove(T element) {
            mStorage.remove(element);
            return this;
        }

        SearchTermsStack<T> clear() {
            mStorage.clear();
            return this;
        }

        public boolean isEmpty() {
            return mStorage.isEmpty();
        }

        List<T> getStackElements() {
            return mStorage;
        }

        @Override
        public String toString() {
            return mStorage.toString();
        }
    }
}
