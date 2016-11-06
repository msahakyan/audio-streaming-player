package com.android.msahakyan.fma.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.application.FmaApplication;
import com.android.msahakyan.fma.di.module.NavigationModule;
import com.android.msahakyan.fma.fragment.NavigationManager;
import com.android.msahakyan.fma.fragment.SearchSuggestionsFragment;
import com.android.msahakyan.fma.view.SearchView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @Inject
    NavigationManager navigationManager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem mSearchItem;
    private SearchSuggestionsFragment mSearchFragment;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }

        navigationManager.showMainPagerFragment();
    }

    @Override
    protected void setupNavigationComponent() {
        FmaApplication.get(this).getApplicationComponent()
            .plus(new NavigationModule(this)).inject(this);
    }

    public void pressBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        navigationManager.onBackPress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        mSearchItem = menu.findItem(R.id.item_search);
        // Associate searchable configuration with the SearchView
        setSearchableConfiguration();

        return true;
    }

    private void setSearchableConfiguration() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(true);

        searchView.setOnSearchClickListener(v -> {
            searchView.showCloseButton(false);
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            showSearchSuggestionsFragment();
        });
    }

    private void showSearchSuggestionsFragment() {
        if (mSearchFragment == null) {
            navigationManager.showSearchSuggestionsFragment();
        }
    }

    public void showSearchIcon(boolean show) {
        if (mSearchItem != null) {
            mSearchItem.setVisible(show);
        }
    }

    public void setSearchFragment(SearchSuggestionsFragment searchFragment) {
        this.mSearchFragment = searchFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Timber.d("Home button pressed");
            navigationManager.onBackPress();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }
}
