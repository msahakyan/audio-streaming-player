package com.android.msahakyan.fma.adapter.pagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.android.msahakyan.fma.fragment.AlbumsFragment;
import com.android.msahakyan.fma.fragment.ArtistsFragment;
import com.android.msahakyan.fma.fragment.GenresFragment;

import java.util.List;

import static com.android.msahakyan.fma.fragment.MainPagerFragment.*;

/**
 * @author msahakyan
 */

public class WheelAdapter extends CustomPagerAdapter {

    private int mSelectedIndex;
    private FragmentManager mFragmentManager;

    private boolean mShouldRecreateFragments;
    private List<String> mTabs;

    public WheelAdapter(@NonNull FragmentManager fragmentManager, @NonNull List<String> tabs, String selectedTitle) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mTabs = tabs;
        if (selectedTitle != null) {
            mSelectedIndex = Math.max(0, mTabs.indexOf(selectedTitle));
        } else {
            mSelectedIndex = mTabs.indexOf(mTabs.get(0));
        }
    }

    @Override
    public int getItemCount() {
        return mTabs.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getActualPageTitle(int position) {
        return mTabs.get(position);
    }

    @Override
    public Fragment getActualItem(int position) {
        String title = mTabs.get(position);
        switch (title) {
            case PAGE_ALBUMS:
                return AlbumsFragment.newInstance();
            case PAGE_ARTISTS:
                return ArtistsFragment.newInstance();
            case PAGE_GENRES:
                return GenresFragment.newInstance();
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.detach((Fragment) object);
        fragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }


    @Override
    public long getItemId(int position) {
        if (mShouldRecreateFragments) {
            return position;
        }
        return mTabs.get(getActualPosition(position)).hashCode();
    }

    public int getFirstPosition() {
        return getStartPosition(2) + mSelectedIndex;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        mShouldRecreateFragments = false;
    }
}
