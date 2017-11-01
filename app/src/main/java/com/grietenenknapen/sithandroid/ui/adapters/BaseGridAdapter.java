package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class BaseGridAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    private final int itemSize;

    public BaseGridAdapter(final int itemSize) {
        this.itemSize = itemSize;
    }

    @Override
    public V onCreateViewHolder(final ViewGroup parent, final int viewType) {
        V viewHolder = onCustomCreateViewHolder(parent, viewType);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                itemSize, //width
                itemSize);//height
        viewHolder.itemView.setLayoutParams(lp);
        return viewHolder;
    }

    protected abstract V onCustomCreateViewHolder(ViewGroup parent, int viewType);
}
