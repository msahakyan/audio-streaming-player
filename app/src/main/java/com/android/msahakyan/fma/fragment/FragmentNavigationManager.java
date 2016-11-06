package com.android.msahakyan.fma.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.widget.Toast;

import com.android.msahakyan.fma.BuildConfig;
import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.adapter.delegates.AlbumAdapterDelegate;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.model.Genre;
import com.android.msahakyan.fma.model.SearchResultItem;
import com.android.msahakyan.fma.transition.DetailsTransition;
import com.android.msahakyan.fma.util.Item;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author msahakyan
 */

public class FragmentNavigationManager implements NavigationManager {

    private static final int LEAVE_APP_INTERVAL = 2000;

    private MainActivity activity;
    private FragmentManager fragmentManager;

    private long mLastBackPressed;

    public FragmentNavigationManager(AppCompatActivity activity) {
        if (activity instanceof MainActivity) {
            this.activity = (MainActivity) activity;
            this.fragmentManager = this.activity.getSupportFragmentManager();
        } else {
            throw new IllegalArgumentException("Navigation Manager instance can be obtained only from MainActivity instance!!");
        }
    }

    @Override
    public void showGenresFragment() {
        showFragment(GenresFragment.newInstance(), true, true);
    }

    @Override
    public void showAlbumsFragment() {
        showFragment(AlbumsFragment.newInstance(), true, true);
    }

    @Override
    public void showArtistsFragment() {
        showFragment(ArtistsFragment.newInstance(), true, true);
    }

    @Override
    public void showArtistDetailFragment(Artist artist) {
        showFragment(ArtistDetailFragment.newInstance(artist), true, false);
    }

    @Override
    public void showTracksFragmentByGenre(Genre genre) {
        showFragment(TracksFragment.newInstance(genre), true, false);
    }

    @Override
    public void showArtistDetailFragment(String artistUrl) {
        showFragment(ArtistDetailFragment.newInstance(artistUrl), true, false);
    }

    @Override
    public void showAlbumDetailFragment(Album album, AlbumAdapterDelegate.AlbumViewHolder holder) {
        showFragmentWithTransition(AlbumDetailFragment.newInstance(album), holder.imageView, "albumImage", true, false);
    }

    @Override
    public void showTrackPlayFragment(List<Item> tracks, int position) {
        showFragment(TrackDetailFragment.newInstance(new ArrayList<>(tracks), position), true, false);
    }

    @Override
    public void showMainPagerFragment() {
        showFragment(MainPagerFragment.newInstance(), true, true);
    }

    private void showFragment(Fragment fragment, boolean allowStateLoss, boolean clearStack) {
        FragmentManager fm = fragmentManager;

        if (clearStack) {
            clearBackStack(fm);
        }

        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = fm.beginTransaction()
            .replace(R.id.container, fragment);

        ft.addToBackStack(null);

        if (allowStateLoss || !BuildConfig.DEBUG) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

        fm.executePendingTransactions();
    }

    private void showFragmentWithTransition(Fragment fragment, View sharedView, String transitionName, boolean allowStateLoss, boolean clearStack) {
        FragmentManager fm = fragmentManager;

        if (clearStack) {
            clearBackStack(fm);
        }

        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = fm.beginTransaction()
            .replace(R.id.container, fragment);

        ft.addToBackStack(null);
        ft.addSharedElement(sharedView, transitionName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            fragment.setExitTransition(new Fade());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
        }

        if (allowStateLoss || !BuildConfig.DEBUG) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

        fm.executePendingTransactions();
    }

    @Override
    public Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(R.id.container);
    }

    @Override
    public void onBackPress() {
        int backStackSize = fragmentManager.getBackStackEntryCount();

        // If there is only one fragment left, then show toast for exit
        if (backStackSize <= 1) {
            long currentTime = System.currentTimeMillis();
            if (mLastBackPressed + LEAVE_APP_INTERVAL > currentTime) {
                activity.finish();
            } else {
                Toast.makeText(activity, R.string.toast_back_to_exit, Toast.LENGTH_SHORT).show();
                mLastBackPressed = currentTime;
            }
            return;
        }

        activity.pressBack();
    }

    @Override
    public void showSearchSuggestionsFragment() {
        showFragment(SearchSuggestionsFragment.newInstance(), true, false);
    }

    @Override
    public void showSearchResultsFragment(List<SearchResultItem> response, String query) {
        showFragment(SearchResultsFragment.newInstance(new ArrayList<>(response), query), true, false);
    }

    private void clearBackStack(FragmentManager fragmentManager) {
        try {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }
}
