package com.android.msahakyan.fma.fragment;

/**
 * Created by msahakyan on 03/08/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.util.ConnectivityUtil;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected MainActivity activity;
    protected NavigationManager navigationManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationManager = activity.getNavigationManager();
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
        if (!ConnectivityUtil.isConnectedToNetwork(activity)) {
            ConnectivityUtil.notifyNoConnection(activity);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationManager = null;
        activity = null;
    }

    /**
     * Override in child classes in order to clear data (listeners,
     * adapters etc) before the view will be destroyed
     */
    public void onBeforeDestroyView() {
    }
}
