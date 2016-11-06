package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.model.Genre;
import com.android.msahakyan.fma.util.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author msahakyan
 */

public class GenreAdapterDelegate extends BaseAdapterDelegate {

    private ItemClickListener<Item> listener;

    public GenreAdapterDelegate(Context ctx, ItemClickListener<Item> listener) {
        super(ctx, TYPE_GENRE);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new GenreViewHolder((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        Genre genre = (Genre) items.get(position);
        GenreViewHolder viewHolder = (GenreViewHolder) holder;
        viewHolder.mTitle.setText(genre.getTitle());
        viewHolder.mTitle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(genre, null);
            }
        });
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        return items.get(position) instanceof Genre && super.isForViewType(items, position);
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.genre_title)
        TextView mTitle;

        GenreViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
