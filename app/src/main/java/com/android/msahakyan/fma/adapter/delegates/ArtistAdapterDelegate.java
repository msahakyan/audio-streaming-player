package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.fragment.FragmentNavigationManager;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.FadeInNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author msahakyan
 */

public class ArtistAdapterDelegate extends BaseAdapterDelegate {

    private ImageLoader mImageLoader;

    public ArtistAdapterDelegate(Context ctx) {
        super(ctx, TYPE_ARTIST);
        mImageLoader = FmaApplication.getInstance().getImageLoader();
    }

    public ArtistAdapterDelegate(Context ctx, @ElementViewType int viewType) {
        super(ctx, viewType);
        mImageLoader = FmaApplication.getInstance().getImageLoader();
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
        viewHolder.mImageView.setImageUrl(artist.getImage(), mImageLoader);
        viewHolder.mImageView.setOnClickListener(v ->
            FragmentNavigationManager.obtain((MainActivity) getContext()).showArtistDetailFragment(artist));
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
