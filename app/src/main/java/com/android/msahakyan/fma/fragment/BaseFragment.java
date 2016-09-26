package com.android.msahakyan.fma.fragment;

/**
 * Created by msahakyan on 03/08/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.util.ConnectivityUtil;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected MainActivity mActivity;
    protected NavigationManager mNavigationManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        mNavigationManager = FragmentNavigationManager.obtain(mActivity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onBeforeDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ConnectivityUtil.isConnectedToNetwork(mActivity)) {
            ConnectivityUtil.notifyNoConnection(mActivity);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mNavigationManager = null;
    }

    /**
     * Override in child classes in order to clear data (listeners,
     * adapters etc) before the view will be destroyed
     */
    public void onBeforeDestroyView() {
    }
}
