package com.grietenenknapen.sithandroid.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.Player;

import java.util.List;

public class EditablePlayerAdapter extends PlayerAdapter {
    private static final int VIEW_TYPE_ADD = 2;

    public EditablePlayerAdapter(final int cardSize) {
        super(cardSize);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADD) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(inflater.inflate(R.layout.list_item_player_add, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_ADD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == VIEW_TYPE_ADD) {
            return -110000 + VIEW_TYPE_ADD;
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ADD) {
            return;
        }
        super.onBindViewHolder(holder, position);
    }
}
