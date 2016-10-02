package com.android.msahakyan.fma.adapter.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.android.msahakyan.fma.fragment.TrackDetailFragment;
import com.android.msahakyan.fma.model.Track;
import com.android.msahakyan.fma.util.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author msahakyan
 */
public class TrackDetailPagerAdapter extends FragmentStatePagerAdapter {

    private List<Item> mItems;

    public TrackDetailPagerAdapter(FragmentManager fragmentManager, List<Item> items) {
        super(fragmentManager);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return TrackDetailFragment.newInstance(new ArrayList<>(mItems), position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((Track) mItems.get(position)).getTitle();
    }
}
