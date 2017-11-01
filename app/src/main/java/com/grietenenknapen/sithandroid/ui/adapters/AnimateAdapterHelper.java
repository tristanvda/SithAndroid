package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

final class AnimateAdapterHelper {

    private AnimateAdapterHelper() {
    }

    static List<Long> createItemIdsList(final RecyclerView.Adapter adapter) {
        final List<Long> itemIds = new ArrayList<>();

        for (int i = 0; i < adapter.getItemCount(); i++) {
            itemIds.add(adapter.getItemId(i));
        }

        return itemIds;
    }

    static void applyAndAnimateRemovals(final RecyclerView.Adapter adapter,
                                        final List<Long> newItemIds,
                                        final List<Long> itemIds) {

        for (int i = itemIds.size() - 1; i >= 0; i--) {
            final Long item = itemIds.get(i);
            if (!newItemIds.contains(item))
                removeItem(adapter, i);
        }
    }

    static void applyAndAnimateAdditions(final RecyclerView.Adapter adapter,
                                         final List<Long> newItemIds,
                                         final List<Long> itemIds) {

        for (int i = 0, count = newItemIds.size(); i < count; i++) {
            final Long item = newItemIds.get(i);
            if (!itemIds.contains(item))
                addItem(adapter, i);
        }
    }

    static void applyAndAnimateMovedItems(final RecyclerView.Adapter adapter,
                                          final List<Long> newItemIds,
                                          final List<Long> itemIds) {

        for (int toPosition = newItemIds.size() - 1; toPosition >= 0; toPosition--) {
            final Long item = newItemIds.get(toPosition);
            final int fromPosition = itemIds.indexOf(item);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(adapter, fromPosition, toPosition);
        }
    }

    private static void removeItem(final RecyclerView.Adapter adapter, final int position) {
        adapter.notifyItemRemoved(position);
    }

    private static void addItem(final RecyclerView.Adapter adapter, final int position) {
        adapter.notifyItemInserted(position);
    }

    private static void moveItem(final RecyclerView.Adapter adapter, final int fromPosition, final int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
        adapter.notifyItemRangeChanged(fromPosition > toPosition ? toPosition : fromPosition,
                fromPosition > toPosition ? fromPosition : toPosition);
    }
}
