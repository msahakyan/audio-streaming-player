package com.android.msahakyan.fma.view;

/**
 * Created by msahakyan on 05/10/16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.android.msahakyan.fma.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchView extends android.support.v7.widget.SearchView {

    @Bind(R.id.search_close_btn)
    View closeButton;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);
    }


    public void showCloseButton(boolean show) {
        if (show) {
            closeButton.setVisibility(VISIBLE);
        } else {
            closeButton.setVisibility(GONE);
        }
    }
}