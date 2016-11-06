package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.adapter.ItemListAdapter;
import com.android.msahakyan.fma.application.FmaApplication;
import com.android.msahakyan.fma.model.Genre;
import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.network.FmaApiService;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.util.InfiniteScrollListener;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.MoreDataLoaderView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends BaseNetworkRequestFragment<Page<Item>> implements
    MoreDataLoaderView.LoadMoreDataCallback, ItemClickListener<Item> {

    private static final int THRESHOLD = 1;

    @Inject
    FmaApiService fmaApiService;

    @Bind(R.id.list_view)
    RecyclerView mListView;
    @Bind(R.id.loading_footer)
    MoreDataLoaderView mLoadingFooter;

    private ItemListAdapter mAdapter;
    private InfiniteScrollListener mInfiniteScrollListener;
    private int mPage;
    private boolean mCanLoadMore = true;

    public GenresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GenresFragment.
     */
    public static GenresFragment newInstance() {
        return new GenresFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAdapter();
        mInfiniteScrollListener = new InfiniteScrollListener(THRESHOLD) {
            @Override
            protected void onLoadMore() {
                loadMoreData();
            }
        };
    }

    private void setLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
        mListView.setLayoutManager(layoutManager);
        mInfiniteScrollListener.setLayoutManager(layoutManager);

        mListView.addOnScrollListener(mInfiniteScrollListener);
        mListView.setAdapter(mAdapter);
    }

    private void createAdapter() {
        mPage = 1;
        mAdapter = new ItemListAdapter(activity, new ArrayList<>(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentView(mListView);
        setLayoutManager();
        mLoadingFooter.setLoadDataCallback(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null && mAdapter != null) {
            if (mAdapter.getItems().isEmpty()) {
                showProgressView();
                refresh();
            } else {
                hideProgressView();
                Timber.d("Items already loaded -- skip");
            }
        }
    }

    @Override
    protected void onSuccess(Page<Item> response, int statusCode) {
        super.onSuccess(response, statusCode);
        hideProgressView();

        if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
            List<Item> items = response.getItems();
            Collections.sort(items);
            showGenres(items);
        } else {
            Timber.e("Something went wrong! StatusCode: " + statusCode + ", response: " + response);
            super.onError(statusCode, "Something went wrong!");
        }
    }

    private void showGenres(List<Item> items) {
        mAdapter.addAll(items);
    }

    @Override
    protected void onError(int statusCode, String errorMessage) {
        super.onError(statusCode, errorMessage);
        hideProgressView();
        Toast.makeText(activity, "Status code: " + statusCode + " error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        super.refresh();
        setNetworkRequest(fmaApiService.getGenres(getNetworkListener(), mPage));
    }

    @Override
    public void loadMoreData() {
        if (mCanLoadMore) {
            mLoadingFooter.setLoadingShown(isResumed() && mAdapter.getItemCount() > 0);
            fmaApiService.getGenres(new NetworkRequestListener<Page<Item>>() {
                @Override
                public void onSuccess(@Nullable Page<Item> response, int statusCode) {
                    if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
                        if (response.getPage() == response.getTotalPages()) {
                            mCanLoadMore = false;
                        }
                        GenresFragment.this.onLoadMoreDataSuccess(response.getItems());
                    } else {
                        Timber.e("Something went wrong! StatusCode: " + statusCode + ", response: " + response);
                        onError(statusCode, "Something went wrong!");
                    }
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    GenresFragment.this.onError(statusCode, errorMessage);
                }
            }, ++mPage);
        } else {
            Timber.d("Can't load more data. All " + mPage + " pages are loaded -- skip");
        }
    }

    private void onLoadMoreDataSuccess(List<Item> newItems) {
        mInfiniteScrollListener.setVisibleThreshold(Math.max(THRESHOLD, newItems.size() / 2));
        if (mLoadingFooter != null) {
            mLoadingFooter.setLoadingShown(false);
        }
        mAdapter.addAll(newItems);
        List<Item> items = mAdapter.getItems();
        Collections.sort(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Item item, RecyclerView.ViewHolder holder) {
        navigationManager.showTracksFragmentByGenre((Genre) item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FmaApplication.get(activity).getApplicationComponent().inject(this);
    }
}
