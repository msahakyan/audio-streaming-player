package com.android.msahakyan.fma.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private static FragmentNavigationManager sInstance;

    private FragmentManager mFragmentManager;
    private MainActivity mActivity;
    private long mLastBackPressed;

    public static FragmentNavigationManager obtain(MainActivity activity) {
        if (sInstance == null) {
            sInstance = new FragmentNavigationManager();
        }
        sInstance.configure(activity);
        return sInstance;
    }

    private void configure(MainActivity activity) {
        mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
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
        showFragmentWithTransition(AlbumDetailFragment.newInstance(album), holder.mImageView, "albumImage", true, false);
    }

    @Override
    public void showTrackPlayFragment(List<Item> tracks, int position) {
        showFragment(TrackDetailFragment.newInstance(new ArrayList<>(tracks), position), true, false);
    }

    @Override
    public void showMainPagerFragment() {
        showFragment(MainPagerFragment.newInstance(), true, true);
    }

    @Override
    public void showTrackDetailPagerFragment(List<Item> tracks, int position) {
        showFragment(TrackDetailPagerFragment.newInstance(new ArrayList<>(tracks), position), true, false);
    }

    private void showFragment(Fragment fragment, boolean allowStateLoss, boolean clearStack) {
        FragmentManager fm = mFragmentManager;

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
        FragmentManager fm = mFragmentManager;

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
        return mFragmentManager.findFragmentById(R.id.container);
    }

    @Override
    public void onBackPress() {
        int backStackSize = mFragmentManager.getBackStackEntryCount();

        // If there is only one fragment left, then show toast for exit
        if (backStackSize <= 1) {
            long currentTime = System.currentTimeMillis();
            if (mLastBackPressed + LEAVE_APP_INTERVAL > currentTime) {
                mActivity.finish();
            } else {
                Toast.makeText(mActivity, R.string.toast_back_to_exit, Toast.LENGTH_SHORT).show();
                mLastBackPressed = currentTime;
            }
            return;
        }

        mActivity.pressBack();
    }

    private void clearBackStack(FragmentManager fragmentManager) {
        try {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }
}
