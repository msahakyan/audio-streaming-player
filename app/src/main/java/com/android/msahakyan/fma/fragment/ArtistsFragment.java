package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemListAdapter;
import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.network.NetworkManager;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.util.InfiniteScrollListener;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.MoreDataLoaderView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsFragment extends BaseNetworkRequestFragment<Page<Item>> implements MoreDataLoaderView.LoadMoreDataCallback {

    private static final int DEFAULT_THRESHOLD = 1;

    @Bind(R.id.list_view)
    RecyclerView mListView;
    @Bind(R.id.loading_footer)
    MoreDataLoaderView mLoadingFooter;

    private ItemListAdapter mAdapter;
    private InfiniteScrollListener mInfiniteScrollListener;
    private int mPage;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GenresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsFragment newInstance() {
        return new ArtistsFragment();
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
    }

    private void setLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mListView.setLayoutManager(layoutManager);
        mInfiniteScrollListener.setLayoutManager(layoutManager);

        mListView.addOnScrollListener(mInfiniteScrollListener);
        mListView.setAdapter(mAdapter);
    }

    private void createAdapter() {
        mPage = 1;
        mAdapter = new ItemListAdapter(mActivity, new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentView(mListView);
        setLayoutManager();
        if (getView() != null && mAdapter != null) {
            if (mAdapter.getItems().isEmpty()) {
                showProgressView();
                refresh();
            } else {
                hideProgressView();
                Timber.d("Items already loaded -- skip");
            }
        }
        mLoadingFooter.setLoadDataCallback(this);
    }

    @Override
    protected void onSuccess(Page<Item> response, int statusCode) {
        super.onSuccess(response, statusCode);
        hideProgressView();

        if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
            showArtists(response.getItems());
        } else {
            Timber.e("Something went wrong! StatusCode: " + statusCode + ", response: " + response);
            super.onError(statusCode, "Something went wrong!");
        }
    }

    private void showArtists(List<Item> items) {
        mAdapter.addAll(items);
    }

    @Override
    protected void onError(int statusCode, String errorMessage) {
        super.onError(statusCode, errorMessage);
        hideProgressView();
        Toast.makeText(mActivity, "Status code: " + statusCode + " error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        super.refresh();
        setNetworkRequest(new NetworkManager().getArtists(getNetworkListener(), mPage));
    }

    @Override
    public void loadMoreData() {
        new NetworkManager().getArtists(new NetworkRequestListener<Page<Item>>() {
            @Override
            public void onSuccess(@Nullable Page<Item> response, int statusCode) {
                if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
                    ArtistsFragment.this.onLoadMoreDataSuccess(response.getItems());
                } else {
                    Timber.e("Something went wrong! StatusCode: " + statusCode + ", response: " + response);
                    onError(statusCode, "Something went wrong!");
                }
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                ArtistsFragment.this.onError(statusCode, errorMessage);
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
//    @Bind(R.id.list_view)
//    RecyclerView mListView;
//
//    private ItemListAdapter mAdapter;
//
//    public ArtistsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @return A new instance of fragment GenresFragment.
//     */
//    public static ArtistsFragment newInstance() {
//        return new ArtistsFragment();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_artists, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        setContentView(mListView);
//        refresh();
//    }
//
//    @Override
//    protected void onSuccess(Page<Item> response, int statusCode) {
//        super.onSuccess(response, statusCode);
//
//        if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
//            if (mListView != null) {
//                init(response.getItems());
//            }
//        }
//    }
//
//    private void init(List<Item> items) {
//        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
//        mListView.setLayoutManager(layoutManager);
//
//        mAdapter = new ItemListAdapter(mActivity, items);
//        mListView.setAdapter(mAdapter);
//        mListView.addItemDecoration(new ItemDecorator(10, 10));
//    }
//
//    @Override
//    protected void onError(int statusCode, String errorMessage) {
//        super.onError(statusCode, errorMessage);
//        Toast.makeText(mActivity, "Status code: " + statusCode + " error: " + errorMessage, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void refresh() {
//        super.refresh();
//        setNetworkRequest(new NetworkManager().getArtists(getNetworkListener(), 1));
//    }
}
