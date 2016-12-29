package com.grietenenknapen.sithandroid.ui.adapters;

import com.grietenenknapen.sithandroid.ui.adapters.managers.TitleGridLayoutManager;

public class TitleGridMergeAdapter extends MergeRecyclerAdapter implements TitleGridLayoutManager.TitleGridRecyclerViewAdapter {

    @Override
    public boolean isHeader(int position) {
        final LocalAdapter localAdapter = getAdapterOffsetForItem(position);

        if (localAdapter.mAdapter instanceof TitleGridLayoutManager.TitleGridRecyclerViewAdapter) {
            return ((TitleGridLayoutManager.TitleGridRecyclerViewAdapter) localAdapter.mAdapter).isHeader(localAdapter.mLocalPosition);
        } else {
            return false;
        }
    }
}
