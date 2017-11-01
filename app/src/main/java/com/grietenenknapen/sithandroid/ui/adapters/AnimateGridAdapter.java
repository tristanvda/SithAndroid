package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

abstract class AnimateGridAdapter<U, V extends RecyclerView.ViewHolder> extends BaseGridAdapter<V> {
    private List<Long> itemIds = new ArrayList<>();
    private boolean hasUniqueItemIds = false;

    public AnimateGridAdapter(int itemSize) {
        super(itemSize);
    }

    public void setData(List<U> items, final boolean animate) {

        final List<Long> newItemIds = AnimateAdapterHelper.createItemIdsList(this);

        if (hasUniqueItemIds && animate) {
            AnimateAdapterHelper.applyAndAnimateRemovals(this, newItemIds, itemIds);
            AnimateAdapterHelper.applyAndAnimateAdditions(this, newItemIds, itemIds);
            AnimateAdapterHelper.applyAndAnimateMovedItems(this, newItemIds, itemIds);
        } else {
            notifyDataSetChanged();
        }

        itemIds = newItemIds;
    }

    public void setHasUniqueItemIds(boolean hasUniqueItemIds) {
        this.hasUniqueItemIds = hasUniqueItemIds;
    }

}
