package com.grietenenknapen.sithandroid.ui.views;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

public class AnimGridLayoutManager extends GridLayoutManager {

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public AnimGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AnimGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AnimGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
}