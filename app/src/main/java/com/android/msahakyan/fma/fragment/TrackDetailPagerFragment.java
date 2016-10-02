package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.pagers.TrackDetailPagerAdapter;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.util.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackDetailPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackDetailPagerFragment extends BasePagerFragment {

    private static final String KEY_ITEM_LIST = "KEY_ITEM_LIST";
    private static final String KEY_ITEM_POSITION = "KEY_ITEM_POSITION";

    @Bind(R.id.pts_main)
    PagerTabStrip mTabStrip;
    @Bind(R.id.vp_main)
    ViewPager mViewPager;

    private TrackDetailPagerAdapter mAdapter;
    private ViewPager.OnPageChangeListener mPageChangedListener;
    private List<Item> mItems;
    private int mCurrentPosition;

    public TrackDetailPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment TrackDetailPagerFragment.
     */
    public static TrackDetailPagerFragment newInstance(ArrayList<Item> tracks, int position) {
        TrackDetailPagerFragment fragment = new TrackDetailPagerFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_ITEM_LIST, tracks);
        args.putInt(KEY_ITEM_POSITION, position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = getArguments().getParcelableArrayList(KEY_ITEM_LIST);
        mCurrentPosition = getArguments().getInt(KEY_ITEM_POSITION);

        mPageChangedListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mAdapter.instantiateItem(mViewPager, position);
            }
        };

        mAdapter = createPagerAdapter();
    }

    @NonNull
    protected TrackDetailPagerAdapter createPagerAdapter() {
        return new TrackDetailPagerAdapter(getChildFragmentManager(), mItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager.setAdapter(mAdapter);
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mTabStrip.setVisibility(View.VISIBLE);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(mPageChangedListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager.setAdapter(null);
        mViewPager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        mPageChangedListener = null;
    }
}
