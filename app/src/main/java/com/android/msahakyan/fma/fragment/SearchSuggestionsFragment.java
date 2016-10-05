package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.SearchHistoryAdapter;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.network.NetworkManager;
import com.android.msahakyan.fma.util.SearchHistoryStack;
import com.android.msahakyan.fma.view.DividerItemDecoration;
import com.android.msahakyan.fma.view.SearchView;

import java.net.HttpURLConnection;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by msahakyan on 05/10/16.
 */

public class SearchSuggestionsFragment extends BaseNetworkRequestFragment<List<SearchResultItem>> {

    private static final int SEARCH_QUERY_MAX_LENTH = 100;
    private static final String KEY_QUERY = "SEARCH_QUERY";

    @Bind(R.id.list_view)
    RecyclerView mRecyclerView;

    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchView mSearchView;
    private SearchHistoryStack mHistoryStack;

    private String mQuery;

    public static SearchSuggestionsFragment newInstance() {
        return new SearchSuggestionsFragment();
    }

    public static SearchSuggestionsFragment newInstance(String query) {
        SearchSuggestionsFragment fragment = new SearchSuggestionsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_suggestios, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setContentView(mRecyclerView);
        hideProgressView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        initSearchHistoryAdapter();
        mActivity.setSearchFragment(this);
    }


    private void initSearchHistoryAdapter() {
        mHistoryStack = new SearchHistoryStack(getActivity());
        List<String> searchHistoryTerms = mHistoryStack.getSearchTermsFromHistory();
        mSearchHistoryAdapter = new SearchHistoryAdapter(getActivity(), searchHistoryTerms);

        mSearchHistoryAdapter.setOnRemoveBtnClickListener(searchHistoryItem -> {
            mSearchView.clearFocus();
            mHistoryStack.removeFromHistory(searchHistoryItem);
        });

        mSearchHistoryAdapter.setOnItemClickListener(this::requestSearch);
        mRecyclerView.setAdapter(mSearchHistoryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        searchItem.setVisible(true);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconified(false);
        mSearchView.setOnSearchClickListener(null);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String input = s.trim();
                if (TextUtils.isEmpty(input)) {
                    Timber.d("Empty search query -> skip");
                } else {
                    requestSearch(input);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mSearchView.showCloseButton(!TextUtils.isEmpty(s));
                String input = truncateInput(s, SEARCH_QUERY_MAX_LENTH);
                if (s.length() > input.length()) {
                    mSearchView.setQuery(input, false);
                }
                return true;
            }
        });
        if (!TextUtils.isEmpty(mQuery)) {
            mSearchView.setQuery(mQuery, false);
        }
    }

    private void requestSearch(String query) {
        if (isRequestActive()) {
            if (query.equals(mQuery)) {
                Timber.d("A search request is already in progress");
                return;
            } else {
                getNetworkRequest().cancel();
            }
        }
        showProgressView();
        mQuery = query;
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        if (mSearchView != null) {
            mSearchView.clearFocus();
        }
        setNetworkRequest(new NetworkManager().loadSearchResultsByQuery(getNetworkListener(), mQuery));
    }

    @Override
    protected void onSuccess(List<SearchResultItem> response, int statusCode) {
        super.onSuccess(response, statusCode);
        hideProgressView();
        mSearchView.setIconified(true);
        if (response != null && statusCode == HttpURLConnection.HTTP_OK) {
            if (mQuery != null) {
                mHistoryStack.addToHistory(mQuery);
                mNavigationManager.showSearchResultsFragment(response, mQuery);
            }
        }
    }

    @Override
    public void onBeforeDestroyView() {
        super.onBeforeDestroyView();
        mActivity.setSearchFragment(null);
        if (mSearchView != null) {
            mSearchView.setQuery(null, false);
            mSearchView.setIconified(true);
        }
    }

    private String truncateInput(String src, int limit) {
        if (!TextUtils.isEmpty(src) && src.length() > limit) {
            return src.substring(0, limit);
        }
        return src;
    }
}

