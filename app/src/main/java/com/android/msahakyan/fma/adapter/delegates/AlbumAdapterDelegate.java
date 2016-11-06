package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.FadeInNetworkImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author msahakyan
 */

public class AlbumAdapterDelegate extends BaseAdapterDelegate {

    private final ItemClickListener<Item> listener;

    public AlbumAdapterDelegate(Context ctx, ItemClickListener<Item> listener) {
        super(ctx, TYPE_ALBUM);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new AlbumViewHolder((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        final Album album = (Album) items.get(position);
        AlbumViewHolder viewHolder = (AlbumViewHolder) holder;

        viewHolder.title.setText(album.getTitle());

        viewHolder.imageView.setImageResource(R.drawable.img_placeholder);
        viewHolder.imageView.setImageUrl(album.getImageFile(), imageLoader);

        viewHolder.artistName.setText(album.getArtistName());
        viewHolder.imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(album, viewHolder);
            }
        });

        viewHolder.trackCount.setText(String.valueOf(album.getTracksCount()));

        // It is important that each shared element in the source screen has a unique transition name.
        // For example, we can't just give all the images in our grid the transition name "some_name"
        // because then we would have conflicting transition names.
        // By appending "_image" to the position, we can support having multiple shared elements in each
        // grid cell in the future.
        ViewCompat.setTransitionName(viewHolder.imageView, position + "_image");
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        return (items.get(position) instanceof Album) && super.isForViewType(items, position);
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.album_image)
        public FadeInNetworkImageView imageView;
        @Bind(R.id.album_title)
        TextView title;
        @Bind(R.id.artist_name)
        TextView artistName;
        @Bind(R.id.track_count)
        TextView trackCount;

        AlbumViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
