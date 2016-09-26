package com.android.msahakyan.fma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.util.ConnectivityUtil;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by msahakyan on 03/08/16.
 */

public class BaseLceFragment extends BaseFragment {

    @Bind(R.id.progress_view)
    protected CircularProgressView mProgressView;

    @Bind(R.id.error_view)
    View mErrorView;

    @Nullable
    @Bind(R.id.empty_view)
    View mEmptyView;

    @Nullable
    @Bind(R.id.error_message)
    TextView mErrorMessage;

    private View mContentView;

    @Override
    public void onBeforeDestroyView() {
        super.onBeforeDestroyView();
        mContentView = null;
    }

    @OnClick(R.id.error_view)
    protected void onClickError(View view) {
        if (ConnectivityUtil.isConnectedToNetwork(mActivity)) {
            refresh();
        } else {
            ConnectivityUtil.notifyNoConnection(mActivity);
        }
    }

    public void refresh() {
//        showProgressView();
    }

    protected void setContentView(View contentView) {
        mContentView = contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEmptyView(mActivity.getString(R.string.no_data), null);
    }

    protected void initEmptyView(String text, View.OnClickListener onClickListener) {
        if (mEmptyView != null) {
            mEmptyView.setOnClickListener(onClickListener);
            if (mErrorMessage != null) {
                mErrorMessage.setText(text);
            }
        }
    }

    public void showEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void showProgressView() {
        if (mContentView == null) {
            Timber.w("Can't show progress view because contentView is not set.");
        }
        hideEmptyView();
        mContentView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        mProgressView.startAnimation();
    }

    public void hideProgressView() {
        if (mProgressView != null) {
            mProgressView.stopAnimation();
            mProgressView.setVisibility(View.GONE);
        }
    }

    public void showContentView() {
        hideEmptyView();
        hideProgressView();
        mErrorView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    public void showErrorView() {
        hideEmptyView();
        hideProgressView();
        mContentView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
