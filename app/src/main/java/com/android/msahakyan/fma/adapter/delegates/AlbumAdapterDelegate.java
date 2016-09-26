package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.fragment.FragmentNavigationManager;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.FadeInNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author msahakyan
 */

public class AlbumAdapterDelegate extends BaseAdapterDelegate {

    private ImageLoader mImageLoader;

    public AlbumAdapterDelegate(Context ctx) {
        super(ctx, TYPE_ALBUM);
        mImageLoader = FmaApplication.getInstance().getImageLoader();
    }

    public AlbumAdapterDelegate(Context ctx, @ElementViewType int viewType) {
        super(ctx, viewType);
        mImageLoader = FmaApplication.getInstance().getImageLoader();
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

        viewHolder.mTitle.setText(album.getTitle());

        viewHolder.mImageView.setImageResource(R.drawable.img_placeholder);
        viewHolder.mImageView.setImageUrl(album.getImageFile(), mImageLoader);

        viewHolder.mArtistName.setText(album.getArtistName());
        viewHolder.mImageView.setOnClickListener(v -> {
            FragmentNavigationManager.obtain((MainActivity) getContext()).showAlbumDetailFragment(album, viewHolder);
        });

        viewHolder.mTrackCount.setText(String.valueOf(album.getTracksCount()));

        // It is important that each shared element in the source screen has a unique transition name.
        // For example, we can't just give all the images in our grid the transition name "some_name"
        // because then we would have conflicting transition names.
        // By appending "_image" to the position, we can support having multiple shared elements in each
        // grid cell in the future.
        ViewCompat.setTransitionName(viewHolder.mImageView, position + "_image");
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        return (items.get(position) instanceof Album) && super.isForViewType(items, position);
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.album_image)
        public FadeInNetworkImageView mImageView;
        @Bind(R.id.album_title)
        TextView mTitle;
        @Bind(R.id.artist_name)
        TextView mArtistName;
        @Bind(R.id.track_count)
        TextView mTrackCount;

        AlbumViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
