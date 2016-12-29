package com.grietenenknapen.sithandroid.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.adapters.managers.TitleGridLayoutManager;

import java.util.List;

public class TitlePlayerCardAdapter extends PlayerCardAdapter implements TitleGridLayoutManager.TitleGridRecyclerViewAdapter {
    private static final int VIEW_TYPE_TITLE = 10;

    private String title;

    public TitlePlayerCardAdapter(Activity context, List<ActivePlayer> activePlayers, final String title, final int cardSize) {
        super(context, activePlayers, cardSize);
        this.title = title;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TITLE) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new TitleViewHolder(inflater.inflate(R.layout.list_item_title, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_TITLE) {
            ((TitleViewHolder) holder).onBind(title);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        if (getItemViewType(position) == VIEW_TYPE_TITLE) {
            return;
        }
        super.onItemClicked(recyclerView, position, v);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TITLE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position - 1);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public boolean isHeader(int position) {
        return position == 0;
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView listTitleTextView;

        TitleViewHolder(View itemView) {
            super(itemView);
            listTitleTextView = (TextView) itemView.findViewById(R.id.listTitle);
        }

        void onBind(String title) {
            listTitleTextView.setText(title);
        }
    }
}
