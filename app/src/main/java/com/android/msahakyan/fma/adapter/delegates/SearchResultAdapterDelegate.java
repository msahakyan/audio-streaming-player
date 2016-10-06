package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.fragment.FragmentNavigationManager;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.network.NetworkManager;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.util.Item;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author msahakyan
 */

public class SearchResultAdapterDelegate extends BaseAdapterDelegate {

    public SearchResultAdapterDelegate(Context ctx) {
        super(ctx, TYPE_SEARCH_RESULT);
    }

    public SearchResultAdapterDelegate(Context ctx, @ElementViewType int viewType) {
        super(ctx, viewType);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SearchItemViewHolder((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        SearchResultItem resultItem = (SearchResultItem) items.get(position);
        SearchItemViewHolder viewHolder = (SearchItemViewHolder) holder;
        viewHolder.mTitle.setText(resultItem.getTrackTitle());
        viewHolder.mArtistName.setText(getContext().getString(R.string.artist_name_wrapper, resultItem.getArtistName()));
        viewHolder.itemView.setOnClickListener(v ->
            new NetworkManager().getTrackById(new NetworkRequestListener<Item>() {
                @Override
                public void onSuccess(@Nullable Item response, int statusCode) {
                    List<Item> fakeItems = Collections.singletonList(response);
                    FragmentNavigationManager.obtain((MainActivity) getContext()).showTrackPlayFragment(fakeItems, 0);
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    Timber.e("Can't load track by id: " + resultItem.getTrackId());
                }
            }, resultItem.getTrackId()));
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        return items.get(position) instanceof SearchResultItem && super.isForViewType(items, position);
    }

    static class SearchItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.track_title)
        TextView mTitle;
        @Bind(R.id.track_artist_name)
        TextView mArtistName;

        SearchItemViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
