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
import com.android.msahakyan.fma.application.FmaApplication;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.network.FmaApiService;
import com.android.msahakyan.fma.util.SearchHistoryStack;
import com.android.msahakyan.fma.view.DividerItemDecoration;
import com.android.msahakyan.fma.view.SearchView;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by msahakyan on 05/10/16.
 */

public class SearchSuggestionsFragment extends BaseNetworkRequestFragment<List<SearchResultItem>> {

    private static final int SEARCH_QUERY_MAX_LENTH = 100;
    private static final String KEY_QUERY = "SEARCH_QUERY";

    @Inject
    FmaApiService fmaApiService;

    @Bind(R.id.list_view)
    RecyclerView recyclerView;

    private SearchView searchView;
    private SearchHistoryStack historyStack;

    private String searchQuery;

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

        setContentView(recyclerView);
        hideProgressView();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        initSearchHistoryAdapter();
        activity.setSearchFragment(this);
    }


    private void initSearchHistoryAdapter() {
        historyStack = new SearchHistoryStack(getActivity());
        List<String> searchHistoryTerms = historyStack.getSearchTermsFromHistory();
        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(getActivity(), searchHistoryTerms);

        searchHistoryAdapter.setOnRemoveBtnClickListener(searchHistoryItem -> {
            searchView.clearFocus();
            historyStack.removeFromHistory(searchHistoryItem);
        });

        searchHistoryAdapter.setOnItemClickListener(this::requestSearch);
        recyclerView.setAdapter(searchHistoryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        searchItem.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setOnSearchClickListener(null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String input = s.trim();
                if (TextUtils.isEmpty(input)) {
                    Timber.d("Empty search searchQuery -> skip");
                } else {
                    requestSearch(input);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchView.showCloseButton(!TextUtils.isEmpty(s));
                String input = truncateInput(s, SEARCH_QUERY_MAX_LENTH);
                if (s.length() > input.length()) {
                    searchView.setQuery(input, false);
                }
                return true;
            }
        });
        if (!TextUtils.isEmpty(searchQuery)) {
            searchView.setQuery(searchQuery, false);
        }
    }

    private void requestSearch(String query) {
        if (isRequestActive()) {
            if (query.equals(this.searchQuery)) {
                Timber.d("A search request is already in progress");
                return;
            } else {
                getNetworkRequest().cancel();
            }
        }
        showProgressView();
        this.searchQuery = query;
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        if (searchView != null) {
            searchView.clearFocus();
        }
        setNetworkRequest(fmaApiService.loadSearchResultsByQuery(getNetworkListener(), searchQuery));
    }

    @Override
    protected void onSuccess(List<SearchResultItem> response, int statusCode) {
        super.onSuccess(response, statusCode);
        hideProgressView();
        searchView.setIconified(true);
        if (response != null && statusCode == HttpURLConnection.HTTP_OK) {
            if (searchQuery != null) {
                historyStack.addToHistory(searchQuery);
                navigationManager.showSearchResultsFragment(response, searchQuery);
            }
        }
    }

    @Override
    public void onBeforeDestroyView() {
        super.onBeforeDestroyView();
        activity.setSearchFragment(null);
        if (searchView != null) {
            searchView.setQuery(null, false);
            searchView.setIconified(true);
        }
    }

    private String truncateInput(String src, int limit) {
        if (!TextUtils.isEmpty(src) && src.length() > limit) {
            return src.substring(0, limit);
        }
        return src;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FmaApplication.get(activity).getApplicationComponent().inject(this);
    }
}

