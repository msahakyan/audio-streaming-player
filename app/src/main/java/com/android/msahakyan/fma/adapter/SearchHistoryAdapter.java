package com.android.msahakyan.fma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.msahakyan.fma.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by msahakyan on 05/10/16.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchItemViewHolder> {

    private SearchHistoryAdapter.SearchHistoryItemClickListener mItemClickListener;
    private SearchHistoryAdapter.SearchHistoryItemClickListener mRemoveBtnClickListener;

    private Context context;
    private List<String> searchItems;

    public SearchHistoryAdapter(Context context, List<String> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
    }

    public void setOnItemClickListener(SearchHistoryAdapter.SearchHistoryItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnRemoveBtnClickListener(SearchHistoryAdapter.SearchHistoryItemClickListener listener) {
        mRemoveBtnClickListener = listener;
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_search_history, parent, false);
        return new SearchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchItemViewHolder holder, int position) {
        final String searchItem = searchItems.get(position);

        holder.searchTerm.setText(searchItem);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(searchItem);
            }
        });

        holder.removeItem.setOnClickListener(v -> {
                searchItems.remove(position);
                notifyDataSetChanged();
                if (mRemoveBtnClickListener != null) {
                    mRemoveBtnClickListener.onItemClick(searchItem);
                }
            }
        );
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    static class SearchItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.search_term)
        TextView searchTerm;
        @Bind(R.id.action_remove_history_item)
        ImageView removeItem;

        SearchItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface SearchHistoryItemClickListener {
        void onItemClick(String searchHistoryItem);
    }
}
