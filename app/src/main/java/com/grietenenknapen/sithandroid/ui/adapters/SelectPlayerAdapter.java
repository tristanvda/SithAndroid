package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayerAdapter extends PlayerAdapter implements ItemClickSupport.OnItemClickListener {
    public static final int SELECTION_LIMIT_NONE = -1;

    private final List<Integer> selectedPositions;
    private int maxItemSelection;
    private OnPlayerSelectListener onPlayerSelectListener;

    public SelectPlayerAdapter(final int cardSize) {
        super(cardSize);
        selectedPositions = new ArrayList<>();
    }

    public List<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public List<Player> getSelectedPlayers() {
        List<Player> selectedPlayers = new ArrayList<>();
        for (Integer position : selectedPositions) {
            selectedPlayers.add(this.players.get(position));
        }
        return selectedPlayers;
    }

    public void selectPlayer(final Player player) {
        final int position = players.indexOf(player);
        if (position > -1) {
            if (selectedPositions.contains(position)) {
                return;
            }
            selectedPositions.add(position);
            notifyDataSetChanged();
        }
    }

    public void setOnPlayerSelectListener(OnPlayerSelectListener onPlayerSelectListener) {
        this.onPlayerSelectListener = onPlayerSelectListener;
    }

    public void setMaxItemSelection(final int maxItemSelection) {
        this.maxItemSelection = maxItemSelection;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setSelected(selectedPositions.contains(position));
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(this);
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        ItemClickSupport.removeFrom(recyclerView);
    }

    @Override
    public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
        if (selectedPositions.contains(position)) {
            v.setSelected(false);
            selectedPositions.remove(Integer.valueOf(position));
            if (onPlayerSelectListener != null) {
                onPlayerSelectListener.onPlayerSelectionChanged(getSelectedPlayers());
            }
        } else {
            if (maxItemSelection != SELECTION_LIMIT_NONE
                    && selectedPositions.size() > 0
                    && selectedPositions.size() >= maxItemSelection) {
                int deletedPos = selectedPositions.get(0);
                selectedPositions.remove(0);
                notifyItemChanged(deletedPos);
            }

            v.setSelected(true);
            selectedPositions.add(position);

            if (onPlayerSelectListener != null) {
                onPlayerSelectListener.onPlayerSelectionChanged(getSelectedPlayers());
            }
        }

        notifyItemChanged(position);
    }

    public interface OnPlayerSelectListener {
        void onPlayerSelectionChanged(List<Player> selectedPlayers);
    }
}
