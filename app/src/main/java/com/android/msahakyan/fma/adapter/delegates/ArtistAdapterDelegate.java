package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.FadeInNetworkImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author msahakyan
 */

public class ArtistAdapterDelegate extends BaseAdapterDelegate {

    private ItemClickListener<Item> listener;

    public ArtistAdapterDelegate(Context ctx, ItemClickListener<Item> listener) {
        super(ctx, TYPE_ARTIST);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ArtistViewHolder((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        Artist artist = (Artist) items.get(position);
        ArtistViewHolder viewHolder = (ArtistViewHolder) holder;

        viewHolder.mName.setText(artist.getName());

        viewHolder.mImageView.setErrorImageResId(R.drawable.img_placeholder);
        viewHolder.mImageView.setImageResource(R.drawable.img_placeholder);
        viewHolder.mImageView.setImageUrl(artist.getImage(), imageLoader);
        viewHolder.mImageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(artist, null);
            }
        });
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        return (items.get(position) instanceof Artist) && super.isForViewType(items, position);
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.artist_image)
        FadeInNetworkImageView mImageView;
        @Bind(R.id.artist_name)
        TextView mName;

        ArtistViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
