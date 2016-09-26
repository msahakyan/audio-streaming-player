package com.android.msahakyan.fma.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.util.ConnectivityUtil;

import butterknife.ButterKnife;

/**
 * Created by msahakyan on 05/09/16.
 */
public class BasePagerFragment extends Fragment {

    protected MainActivity mActivity;
    protected NavigationManager mNavigationManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
        mNavigationManager = FragmentNavigationManager.obtain(mActivity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mNavigationManager = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ConnectivityUtil.isConnectedToNetwork(mActivity)) {
            ConnectivityUtil.notifyNoConnection(mActivity);
        }
    }
}
