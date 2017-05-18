package com.smk.view.showitemtouch;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自适应高度的布局管理
 */
public class MeasuredGridLayoutManager extends GridLayoutManager {

    private int maxHeight;
    private int spanCount;
    private int maxCount;


    public MeasuredGridLayoutManager(Context context, int spanCount, int maxCount) {
        super(context, spanCount);
        this.spanCount = spanCount;
        this.maxCount = maxCount;
    }


    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (maxHeight == 0 && getItemCount() > 0) {
            View child = recycler.getViewForPosition(0);
            RecyclerView.LayoutParams params = (LayoutParams) child.getLayoutParams();
            child.measure(widthSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int height = child.getMeasuredHeight() + getPaddingTop() + getPaddingBottom()
                    + params.topMargin + params.bottomMargin;
            if (height > maxHeight) {
                maxHeight = height;
            }
            maxHeight = maxHeight * (maxCount / spanCount + 1);
        }
        heightSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.EXACTLY);
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }
}
