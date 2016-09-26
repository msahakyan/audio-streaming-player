package com.android.msahakyan.fma.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author msahakyan
 */

public class MoreDataLoaderView extends FrameLayout {

    @Bind(R.id.loading_failed_text)
    TextView mLoadingFailedView;
    @Bind(R.id.loading_spinner)
    CircularProgressView mLoadingSpinner;

    private LoadMoreDataCallback mListener;

    public MoreDataLoaderView(Context context) {
        super(context);
        init(context, null);
    }

    public MoreDataLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MoreDataLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MoreDataLoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @OnClick(R.id.loading_failed_text)
    public void onClickLoadingFailed() {
        mListener.loadMoreData();
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_load_more_data, this, true);
        ButterKnife.bind(MoreDataLoaderView.this);
    }

    public void setLoadDataCallback(LoadMoreDataCallback listener) {
        mListener = listener;
    }

    public void setLoadingShown(boolean show) {
        mLoadingFailedView.setVisibility(View.GONE);
        if (show) {
            showLoadingSpinner();
        } else {
            hideLoadingSpinner();
        }
    }

    public boolean isLoadingShown() {
        return mLoadingSpinner.getVisibility() == VISIBLE;
    }

    public void onError() {
        mLoadingFailedView.setVisibility(View.VISIBLE);
        hideLoadingSpinner();
    }

    private void showLoadingSpinner() {
        if (mLoadingSpinner != null) {
            mLoadingSpinner.setVisibility(VISIBLE);
            mLoadingSpinner.startAnimation();
        }
    }

    private void hideLoadingSpinner() {
        if (mLoadingSpinner != null) {
            mLoadingSpinner.setVisibility(GONE);
            mLoadingSpinner.stopAnimation();
        }
    }

    public interface LoadMoreDataCallback {
        void loadMoreData();
    }
}
