package com.android.msahakyan.fma.view;

/**
 * Created by msahakyan on 05/10/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.msahakyan.fma.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String NO_DECORATION = "no_decoration";

    private Paint mPaintBorder;

    public DividerItemDecoration(Context context) {
        int intrinsicWidth = context.getResources().getDimensionPixelSize(R.dimen.border_size);
        mPaintBorder = new Paint();
        mPaintBorder.setColor(ContextCompat.getColor(context, R.color.dark_grey));
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(intrinsicWidth);

    }


    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (NO_DECORATION.equals(child.getTag())) {
                continue;
            }
            canvas.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), mPaintBorder);
        }
    }
}
