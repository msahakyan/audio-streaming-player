package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.util.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author msahakyan
 */

public class SearchResultAdapterDelegate extends BaseAdapterDelegate {

    private ItemClickListener<Item> listener;

    public SearchResultAdapterDelegate(Context ctx, ItemClickListener<Item> listener) {
        super(ctx, TYPE_SEARCH_RESULT);
        this.listener = listener;
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
        viewHolder.title.setText(resultItem.getTrackTitle());
        viewHolder.artistName.setText(getContext().getString(R.string.artist_name_wrapper, resultItem.getArtistName()));
        viewHolder.itemView.setOnClickListener(v ->
            fmaApiService.getTrackById(new NetworkRequestListener<Item>() {
                @Override
                public void onSuccess(@Nullable Item item, int statusCode) {
                    if (listener != null) {
                        listener.onItemClicked(item, null);
                    }
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
        TextView title;
        @Bind(R.id.track_artist_name)
        TextView artistName;

        SearchItemViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
