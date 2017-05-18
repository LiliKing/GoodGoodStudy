package com.smk.view.showitemtouch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 解决listview嵌套recycleview高度不能显示问题
 */
public class MeasuredRecyclerView extends RecyclerView {
    public MeasuredRecyclerView(Context context) {
        this(context, null);
    }

    public MeasuredRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasuredRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        int maxHeight = 0;
        if(getChildCount() > 0) {
            View child = getChildAt(0);
            RecyclerView.LayoutParams params = (LayoutParams) child.getLayoutParams();
            child.measure(widthSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            maxHeight = child.getMeasuredHeight() + getPaddingTop() + getPaddingBottom()+ params.topMargin + params.bottomMargin;
        }
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY));
    }
}
