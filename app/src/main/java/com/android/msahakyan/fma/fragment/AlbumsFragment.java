package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.adapter.ItemListAdapter;
import com.android.msahakyan.fma.adapter.delegates.AlbumAdapterDelegate;
import com.android.msahakyan.fma.application.FmaApplication;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.network.FmaApiService;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.util.InfiniteScrollListener;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.util.ItemDecorator;
import com.android.msahakyan.fma.view.MoreDataLoaderView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumsFragment extends BaseNetworkRequestFragment<Page<Item>> implements
    MoreDataLoaderView.LoadMoreDataCallback, ItemClickListener<Item> {

    @Inject
    FmaApiService fmaApiService;

    private static final int DEFAULT_THRESHOLD = 1;

    @Bind(R.id.list_view)
    RecyclerView mListView;
    @Bind(R.id.loading_footer)
    MoreDataLoaderView mLoadingFooter;

    private ItemListAdapter mAdapter;
    private InfiniteScrollListener mInfiniteScrollListener;
    private int mPage = 2; // skip first page

    public AlbumsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GenresFragment.
     */
    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAdapter();
        mInfiniteScrollListener = new InfiniteScrollListener(DEFAULT_THRESHOLD) {
            @Override
            protected void onLoadMore() {
                mLoadingFooter.setLoadingShown(isResumed() && mAdapter.getItemCount() > 0);
                loadMoreData();
            }
        };
        setHasOptionsMenu(true);
    }

    private void setLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        mListView.setLayoutManager(layoutManager);
        mInfiniteScrollListener.setLayoutManager(layoutManager);

        mListView.addOnScrollListener(mInfiniteScrollListener);
        mListView.setAdapter(mAdapter);
        mListView.addItemDecoration(new ItemDecorator(3, 3));
    }

    private void createAdapter() {
        mPage = 2;
        mAdapter = new ItemListAdapter(activity, new ArrayList<>(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false);
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
            showAlbums(response.getItems());
        } else {
            Timber.e("Something went wrong! StatusCode: " + statusCode + ", response: " + response);
            super.onError(statusCode, "Something went wrong!");
        }
    }

    private void showAlbums(List<Item> items) {
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
        setNetworkRequest(fmaApiService.getAlbums(getNetworkListener(), mPage));
    }

    @Override
    public void loadMoreData() {
        fmaApiService.getAlbums(new NetworkRequestListener<Page<Item>>() {
            @Override
            public void onSuccess(@Nullable Page<Item> response, int statusCode) {
                if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
                    AlbumsFragment.this.onLoadMoreDataSuccess(response.getItems());
                } else {
                    Timber.e("Something went wrong! StatusCode: " + statusCode + ", response: " + response);
                    onError(statusCode, "Something went wrong!");
                }
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                AlbumsFragment.this.onError(statusCode, errorMessage);
            }
        }, ++mPage);
    }

    private void onLoadMoreDataSuccess(List<Item> result) {
        mInfiniteScrollListener.setVisibleThreshold(Math.max(DEFAULT_THRESHOLD, result.size() / 2));
        if (mLoadingFooter != null) {
            mLoadingFooter.setLoadingShown(false);
        }
        mAdapter.addAll(result);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (activity != null) {
            activity.showSearchIcon(true);
        }
    }

    @Override
    public void onItemClicked(Item item, RecyclerView.ViewHolder holder) {
        navigationManager.showAlbumDetailFragment((Album) item, (AlbumAdapterDelegate.AlbumViewHolder) holder);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FmaApplication.get(activity).getApplicationComponent().inject(this);
    }
}
