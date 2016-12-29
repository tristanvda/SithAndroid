package com.grietenenknapen.sithandroid.ui.adapters.managers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class TitleGridLayoutManager extends GridLayoutManager {
    private final TitleGridRecyclerViewAdapter adapter;

    public TitleGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, TitleGridRecyclerViewAdapter adapter) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.adapter = adapter;
    }

    public TitleGridLayoutManager(Context context, int spanCount, TitleGridRecyclerViewAdapter adapter) {
        super(context, spanCount);
        this.adapter = adapter;
    }

    public TitleGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, TitleGridRecyclerViewAdapter adapter) {
        super(context, spanCount, orientation, reverseLayout);
        this.adapter = adapter;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter != null && adapter.isHeader(position) ? getSpanCount() : 1;
            }
        });
    }

    public interface TitleGridRecyclerViewAdapter {

        boolean isHeader(int position);
    }
}
