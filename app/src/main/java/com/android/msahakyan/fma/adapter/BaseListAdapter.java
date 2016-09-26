package com.android.msahakyan.fma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.android.msahakyan.fma.adapter.delegates.AdapterDelegate;
import com.android.msahakyan.fma.adapter.delegates.AdapterDelegatesManager;
import com.android.msahakyan.fma.util.AppUtils;
import com.android.msahakyan.fma.util.Item;

import java.util.List;

/**
 * Created by msahakyan on 15/06/16.
 */

public abstract class BaseListAdapter<T extends Item> extends RecyclerView.Adapter {

    private AdapterDelegatesManager<List<T>> mDelegatesManager;
    private List<T> mItems;

    public BaseListAdapter(Context ctx, List<T> items) {
        mItems = items;
        mDelegatesManager = new AdapterDelegatesManager<>();
    }

    protected void setAdapterDelegates(List<AdapterDelegate<List<T>>> delegates) {
        mDelegatesManager.clear();
        for (AdapterDelegate<List<T>> delegate : delegates) {
            mDelegatesManager.addDelegate(delegate);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegatesManager.getItemViewType(mItems, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mDelegatesManager.onBindViewHolder(mItems, position, holder);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAll(List<T> items) {
        if (items != null) {
            mItems.addAll(items);
            final int startIndex = Math.max(0, mItems.size() - items.size() - 1);
            notifyItemRangeChanged(startIndex, mItems.size());
        }
    }

    public void clear() {
        if (!AppUtils.isEmpty(mItems)) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    public boolean contains(T item) {
        return mItems.contains(item);
    }

    public List<T> getItems() {
        return mItems;
    }
}
