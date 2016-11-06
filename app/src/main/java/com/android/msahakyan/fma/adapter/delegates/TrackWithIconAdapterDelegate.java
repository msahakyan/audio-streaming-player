package com.android.msahakyan.fma.adapter.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.model.Track;
import com.android.msahakyan.fma.util.Item;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.android.msahakyan.fma.fragment.TracksFragment.QUALIFIER;

/**
 * @author msahakyan
 */

public class TrackWithIconAdapterDelegate extends BaseAdapterDelegate {

    private final ItemClickListener<Item> listener;

    public TrackWithIconAdapterDelegate(Context ctx, ItemClickListener<Item> listener) {
        super(ctx, TYPE_TRACK_WITH_ICON);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TrackWithIconViewHolder((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_track_with_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        final Track track = (Track) items.get(position);
        TrackWithIconViewHolder viewHolder = (TrackWithIconViewHolder) holder;

        viewHolder.trackImage.setImageResource(R.drawable.img_placeholder);
        viewHolder.trackImage.setImageUrl(track.getImage(), imageLoader);
        viewHolder.title.setText(track.getTitle());

        viewHolder.trackImage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(track, viewHolder);
            }
        });
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        Item item = items.get(position);
        return item instanceof Track && ((Track) item).getQualifier().equals(QUALIFIER) && super.isForViewType(items, position);
    }

    static class TrackWithIconViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.track_image)
        NetworkImageView trackImage;
        @Bind(R.id.track_title)
        TextView title;

        TrackWithIconViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
