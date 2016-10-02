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
import com.android.msahakyan.fma.adapter.pagers.WheelAdapter;
import com.android.msahakyan.fma.util.ZoomOutPageTransformer;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPagerFragment extends BasePagerFragment {

    public static final String PAGE_GENRES = "Genres";
    public static final String PAGE_ALBUMS = "Albums";
    public static final String PAGE_ARTISTS = "Artists";

    @Bind(R.id.pts_main)
    PagerTabStrip mTabStrip;
    @Bind(R.id.vp_main)
    ViewPager mViewPager;

    protected WheelAdapter mAdapter;
    private ViewPager.OnPageChangeListener mPageChangedListener;

    public MainPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MainPagerFragment.
     */
    public static MainPagerFragment newInstance() {
        return new MainPagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPageChangedListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        };

        mAdapter = createPagerAdapter();
    }

    @NonNull
    protected WheelAdapter createPagerAdapter() {
        List<String> tabs = Arrays.asList(PAGE_ALBUMS, PAGE_GENRES, PAGE_ARTISTS);
        return new WheelAdapter(getChildFragmentManager(), tabs, tabs.get(0));
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

        mTabStrip.setVisibility(View.VISIBLE);
        mViewPager.setCurrentItem(mAdapter.getFirstPosition());
        mViewPager.addOnPageChangeListener(mPageChangedListener);
        mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        mPageChangedListener = null;
    }
}
