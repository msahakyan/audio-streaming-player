package com.android.msahakyan.fma.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * @author msahakyan
 *         <p>
 *         Simple interface which is responsible for delegating adapter item
 *         click events to the corresponding fragments
 */

public interface ItemClickListener<T> {

    /**
     * Fires when adapter item is clicked
     *
     * @param item   The adapter item which was clicked
     * @param holder The viewHolder of the adapter item
     */
    void onItemClicked(T item, RecyclerView.ViewHolder holder);
}
