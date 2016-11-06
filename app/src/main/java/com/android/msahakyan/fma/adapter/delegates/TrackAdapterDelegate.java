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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author msahakyan
 */

public class TrackAdapterDelegate extends BaseAdapterDelegate {

    private final ItemClickListener<Item> listener;

    public TrackAdapterDelegate(Context ctx, ItemClickListener<Item> listener) {
        super(ctx, TYPE_TRACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TrackViewHolder((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_track, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        final Track track = (Track) items.get(position);
        TrackViewHolder viewHolder = (TrackViewHolder) holder;

        viewHolder.trackNumber.setText(String.valueOf(position + 1));
        viewHolder.title.setText(track.getTitle());

        viewHolder.duration.setText(track.getDuration());
        viewHolder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(track, viewHolder);
            }
        });
    }

    @Override
    public boolean isForViewType(@NonNull List<Item> items, int position) {
        Item item = items.get(position);
        return item instanceof Track && ((Track) item).getQualifier() == null && super.isForViewType(items, position);
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.track_number)
        TextView trackNumber;
        @Bind(R.id.track_title)
        TextView title;
        @Bind(R.id.track_duration)
        TextView duration;

        TrackViewHolder(ViewGroup view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
