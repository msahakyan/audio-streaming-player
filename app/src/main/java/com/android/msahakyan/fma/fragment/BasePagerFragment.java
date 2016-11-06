package com.android.msahakyan.fma.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.msahakyan.fma.activity.MainActivity;
import com.android.msahakyan.fma.util.ConnectivityUtil;

import butterknife.ButterKnife;

/**
 * Created by msahakyan on 05/09/16.
 */
public class BasePagerFragment extends Fragment {

    protected MainActivity activity;
    protected NavigationManager navigationManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationManager = activity.getNavigationManager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
        navigationManager = null;
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
        if (!ConnectivityUtil.isConnectedToNetwork(activity)) {
            ConnectivityUtil.notifyNoConnection(activity);
        }
    }
}
