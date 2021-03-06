package com.grietenenknapen.sithandroid.ui.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.Player;

import java.util.List;

public class PlayerAdapter extends AnimateAdapter<Player, PlayerAdapter.ViewHolder> {
    protected List<Player> players;

    public PlayerAdapter(final int cardSize) {
        super(cardSize);
        setHasUniqueItemIds(true);
    }

    @Override
    public void setData(List<Player> items, boolean animate) {
        players = items;
        super.setData(items, animate);
    }

    @Override
    protected PlayerAdapter.ViewHolder onCustomCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.list_item_player_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(players.get(position));
    }

    @Override
    public long getItemId(int position) {
        return players.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerCardName;

        ViewHolder(View itemView) {
            super(itemView);
            playerCardName = (TextView) itemView.findViewById(R.id.playerCardName);
        }

        void onBind(Player player) {
            playerCardName.setText(player.getName());
        }
    }
}
