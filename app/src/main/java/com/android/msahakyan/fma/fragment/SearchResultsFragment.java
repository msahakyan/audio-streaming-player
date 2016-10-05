package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemListAdapter;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.util.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends BaseLceFragment {

    private static final String KEY_ITEM_LIST = "KEY_ITEM_LIST";
    private static final String KEY_QUERY = "KEY_QUERY";

    @Bind(R.id.list_view)
    RecyclerView mListView;

    private ItemListAdapter mAdapter;
    private List<Item> mSearchResults;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchResultsFragment.
     */
    public static SearchResultsFragment newInstance(ArrayList<SearchResultItem> response, String query) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_ITEM_LIST, response);
        args.putString(KEY_QUERY, query);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchResults = getArguments().getParcelableArrayList(KEY_ITEM_LIST);
        String mQuery = getArguments().getString(KEY_QUERY);
        createAdapter();
    }

    private void setLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mAdapter);
    }

    private void createAdapter() {
        mAdapter = new ItemListAdapter(mActivity, mSearchResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentView(mListView);
        setLayoutManager();
        if (getView() != null && mAdapter != null) {
            if (mAdapter.getItems().isEmpty()) {
                showProgressView();
            } else {
                hideProgressView();
                Timber.d("Items already loaded -- skip");
            }
        }
    }
}
