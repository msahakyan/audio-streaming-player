package com.android.msahakyan.fma.adapter.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author msahakyan
 */
abstract class CustomPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_SIZE = Integer.MAX_VALUE;

    CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public final int getCount() {
        return PAGE_SIZE;
    }

    @Override
    public final Fragment getItem(int position) {
        return getActualItem(getActualPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return getActualPosition(position);
    }

    @Override
    public final CharSequence getPageTitle(int position) {
        return getActualPageTitle(getActualPosition(position));
    }

    public abstract int getItemCount();

    public abstract Fragment getActualItem(int position);

    public CharSequence getActualPageTitle(int position) {
        return null;
    }

    int getActualPosition(int position) {
        return position % getItemCount();
    }

    int getStartPosition(int position) {
        return PAGE_SIZE / 2 - PAGE_SIZE / position % getItemCount();
    }
}
