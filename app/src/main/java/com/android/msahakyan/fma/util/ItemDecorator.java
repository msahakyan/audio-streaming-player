package com.android.msahakyan.fma.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author msahakyan
 */
public class ItemDecorator extends RecyclerView.ItemDecoration {

    private final int mHorizontalSpace;
    private final int mVerticalSpace;

    public ItemDecorator(int horizontalSpace, int verticalSpace) {
        this.mHorizontalSpace = horizontalSpace;
        this.mVerticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.top = outRect.bottom = mVerticalSpace;
        outRect.left = outRect.right = mHorizontalSpace;
    }
}
