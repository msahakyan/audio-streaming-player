package com.android.msahakyan.fma.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import timber.log.Timber;

/**
 * Created by msahakyan on 17/08/16.
 */

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private VisibleItemFinder mItemFinder;

    private boolean mEnabled;
    private int mPreviousTotal = 0;
    private int mVisibleThreshold;

    public InfiniteScrollListener(int visibleThreshold) {
        mVisibleThreshold = visibleThreshold;
    }

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mItemFinder == null) {
            Timber.d("Visible item finder isn't set");
            return;
        }
        onScrolled(recyclerView, mItemFinder, dx, dy);
    }

    private void onScrolled(RecyclerView recyclerView, VisibleItemFinder itemFinder, int dx, int dy) {

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getAdapter().getItemCount();
        int firstVisibleItem = itemFinder.getFirstVisibleItem();

        if (!mEnabled) {
            if (totalItemCount > mPreviousTotal) {
                mEnabled = true;
                mPreviousTotal = totalItemCount;
            }
        }
        if (mEnabled && (totalItemCount - visibleItemCount <= firstVisibleItem + mVisibleThreshold)) {
            onLoadMore();
            mEnabled = false;
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            mItemFinder = new LinearVisibleItemFinder((LinearLayoutManager) layoutManager);
        }
    }

    public void setVisibleThreshold(int visibleThreshold) {
        mVisibleThreshold = visibleThreshold;
    }

    protected abstract void onLoadMore();


    public void reset() {
        mPreviousTotal = 0;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    private static class LinearVisibleItemFinder implements VisibleItemFinder {

        private LinearLayoutManager mLayoutManager;

        public LinearVisibleItemFinder(LinearLayoutManager layoutManager) {
            mLayoutManager = layoutManager;
        }

        @Override
        public int getFirstVisibleItem() {
            return mLayoutManager.findFirstVisibleItemPosition();
        }
    }

    public interface VisibleItemFinder {
        int getFirstVisibleItem();
    }
}

