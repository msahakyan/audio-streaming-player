package com.android.msahakyan.fma.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.fragment.FragmentNavigationManager;
import com.android.msahakyan.fma.fragment.NavigationManager;
import com.android.msahakyan.fma.fragment.SearchSuggestionsFragment;
import com.android.msahakyan.fma.network.NetworkChannel;
import com.android.msahakyan.fma.view.SearchView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    NetworkChannel mNetworkChannel;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private NavigationManager mFragmentNavManager;
    private MenuItem mSearchItem;
    private SearchSuggestionsFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        mFragmentNavManager = FragmentNavigationManager.obtain(this);
        setContentView(R.layout.activity_main);
        injectDependencies();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        handleIntent(getIntent());

        mFragmentNavManager.showMainPagerFragment();
    }

    private void injectDependencies() {
        FmaApplication.getNetworkComponent().inject(this);
    }

    public void pressBack() {
        super.onBackPressed();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
        mFragmentNavManager.onBackPress();
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            showSearchSuggestionsFragment();
        });
    }

    private void showSearchSuggestionsFragment() {
        if (mSearchFragment == null) {
            mFragmentNavManager.showSearchSuggestionsFragment();
        }
    }

    public void showSearchIcon(boolean show) {
        if (mSearchItem != null) {
            mSearchItem.setVisible(show);
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    public void setSearchFragment(SearchSuggestionsFragment searchFragment) {
        this.mSearchFragment = searchFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Timber.d("Home pressed");
            mFragmentNavManager.onBackPress();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
