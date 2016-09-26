package com.android.msahakyan.fma.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.fragment.FragmentNavigationManager;
import com.android.msahakyan.fma.fragment.NavigationManager;
import com.android.msahakyan.fma.network.NetworkChannel;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    NetworkChannel mNetworkChannel;

    private NavigationManager mFragmentNavManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        mFragmentNavManager = FragmentNavigationManager.obtain(this);
        setContentView(R.layout.activity_main);

        injectDependencies();
        ButterKnife.bind(this);

        mFragmentNavManager.showMainPagerFragment();
    }

    private void injectDependencies() {
        FmaApplication.getNetworkComponent().inject(this);
    }

    public void pressBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mFragmentNavManager.onBackPress();
    }
}
