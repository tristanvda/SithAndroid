package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MergeRecyclerAdapter extends RecyclerView.Adapter {

    protected ArrayList<LocalAdapter> mAdapters = new ArrayList<>();
    private int mViewTypeIndex = 0;

    /**
     * Append the given adapter to the list of merged adapters.
     */
    public void addAdapter(RecyclerView.Adapter adapter) {
        addAdapter(mAdapters.size(), adapter);
    }

    /**
     * Add the given adapter to the list of merged adapters at the given index.
     */
    public void addAdapter(int index, RecyclerView.Adapter adapter) {
        mAdapters.add(index, new LocalAdapter(adapter));
        notifyItemRangeInserted(getAdapterStartPosition(index), adapter.getItemCount());
    }

    /**
     * Remove the given adapter get the list of merged adapters.
     */
    public void removeAdapter(RecyclerView.Adapter adapter) {
        Object[] localAdapters = mAdapters.toArray();
        for (Object a : localAdapters)
            if (((LocalAdapter) a).mAdapter == adapter)
                removeAdapter(mAdapters.indexOf(a));
    }

    /**
     * Remove the adapter at the given index get the list of merged adapters.
     */
    public void removeAdapter(int index) {
        if (index < 0 || index >= mAdapters.size()) return;
        int startPos = getAdapterStartPosition(index);
        LocalAdapter adapter = mAdapters.remove(index);
        adapter.unregisterAdapterDataObserver();
        notifyItemRangeRemoved(startPos, adapter.mAdapter.getItemCount());
    }

    public int getAdapterCount() {
        return mAdapters.size();
    }

    public RecyclerView.Adapter getAdapter(int index) {
        return mAdapters.get(index).mAdapter;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (LocalAdapter adapter : mAdapters) {
            count += adapter.mAdapter.getItemCount();
        }
        return count;
    }

    private int getAdapterStartPosition(int index) {
        int start = 0;
        for (int i = 0; i < index; i++)
            start += getAdapter(i).getItemCount();
        return start;
    }

    /**
     * For a given merged position, find the corresponding Adapter and local position within that Adapter by iterating through Adapters and
     * summing their counts until the merged position is found.
     *
     * @param position a merged (global) position
     * @return the matching Adapter and local position, or null if not found
     */
    public LocalAdapter getAdapterOffsetForItem(final int position) {
        final int adapterCount = mAdapters.size();
        int i = 0;
        int count = 0;

        while (i < adapterCount) {
            LocalAdapter a = mAdapters.get(i);
            int newCount = count + a.mAdapter.getItemCount();
            if (position < newCount) {
                a.mLocalPosition = position - count;
                return a;
            }
            count = newCount;
            i++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        LocalAdapter result = getAdapterOffsetForItem(position);
        int localViewType = result.mAdapter.getItemViewType(result.mLocalPosition);
        if (result.mViewTypesMap.containsValue(localViewType)) {
            for (Map.Entry<Integer, Integer> entry : result.mViewTypesMap.entrySet()) {
                if (entry.getValue() == localViewType) {
                    return entry.getValue();
                }
            }
        }
        mViewTypeIndex += 1;
        result.mViewTypesMap.put(mViewTypeIndex, localViewType);
        return localViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        for (LocalAdapter adapter : mAdapters) {
            if (adapter.mViewTypesMap.containsValue(viewType))
                return adapter.mAdapter.onCreateViewHolder(viewGroup, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        LocalAdapter result = getAdapterOffsetForItem(position);
        result.mAdapter.onBindViewHolder(viewHolder, result.mLocalPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        for (LocalAdapter adapter: mAdapters){
            adapter.mAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        for (LocalAdapter adapter: mAdapters){
            adapter.mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    public void clear() {
        while (getAdapterCount() > 0)
            removeAdapter(0);
    }

    public void addView(View view) {
        addAdapter(new ViewRecyclerAdapter(view));
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * forwarding data set observer
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    private static class ViewsViewHolder extends RecyclerView.ViewHolder {
        public ViewsViewHolder(android.view.View itemView) {
            super(itemView);
        }
    }

    private static class ViewRecyclerAdapter extends RecyclerView.Adapter<ViewsViewHolder> {

        private final View view;

        ViewRecyclerAdapter(View view) {
            this.view = view;
        }

        @Override
        public ViewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewsViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    protected class LocalAdapter {
        public final RecyclerView.Adapter mAdapter;
        public int mLocalPosition = 0;
        public Map<Integer, Integer> mViewTypesMap = new HashMap<>();

        public LocalAdapter(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
            mAdapter.registerAdapterDataObserver(observer);
        }

        public void unregisterAdapterDataObserver() {
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        public final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                positionStart += getAdapterStartPosition(mAdapters.indexOf(LocalAdapter.this));
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                positionStart += getAdapterStartPosition(mAdapters.indexOf(LocalAdapter.this));
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                positionStart += getAdapterStartPosition(mAdapters.indexOf(LocalAdapter.this));
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                onChanged(); //FIXME
            }
        };
    }
}