package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimateAdapter<U, V extends RecyclerView.ViewHolder> extends BaseGridAdapter<V> {
    private List<Long> itemIds = new ArrayList<>();
    private boolean hasUniqueItemIds = false;

    public AnimateAdapter(int itemSize) {
        super(itemSize);
    }

    public void setData(List<U> items, final boolean animate) {

        final List<Long> newItemIds = createItemIdsList();

        if (hasUniqueItemIds && animate) {
            applyAndAnimateRemovals(newItemIds);
            applyAndAnimateAdditions(newItemIds);
            applyAndAnimateMovedItems(newItemIds);
        } else {
            notifyDataSetChanged();
        }

        itemIds = newItemIds;
    }

    public void setHasUniqueItemIds(boolean hasUniqueItemIds) {
        this.hasUniqueItemIds = hasUniqueItemIds;
    }

    private List<Long> createItemIdsList() {
        final List<Long> itemIds = new ArrayList<>();

        for (int i = 0; i < getItemCount(); i++) {
            itemIds.add(getItemId(i));
        }

        return itemIds;
    }

    private void applyAndAnimateRemovals(List<Long> newItemIds) {
        for (int i = itemIds.size() - 1; i >= 0; i--) {
            final Long item = itemIds.get(i);
            if (!newItemIds.contains(item))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<Long> newItemIds) {
        for (int i = 0, count = newItemIds.size(); i < count; i++) {
            final Long item = newItemIds.get(i);
            if (!itemIds.contains(item))
                addItem(i);
        }
    }

    private void applyAndAnimateMovedItems(List<Long> newItemIds) {
        for (int toPosition = newItemIds.size() - 1; toPosition >= 0; toPosition--) {
            final Long item = newItemIds.get(toPosition);
            final int fromPosition = itemIds.indexOf(item);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    private void removeItem(int position) {
        notifyItemRemoved(position);
    }

    private void addItem(int position) {
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition > toPosition ? toPosition : fromPosition,
                fromPosition > toPosition ? fromPosition : toPosition);
    }

}
