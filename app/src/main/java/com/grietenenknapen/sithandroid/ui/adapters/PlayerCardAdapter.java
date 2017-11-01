package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Animation.AnimationFactory;
import com.grietenenknapen.sithandroid.ui.views.SithCardView;
import com.grietenenknapen.sithandroid.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class PlayerCardAdapter extends BaseGridAdapter<RecyclerView.ViewHolder> implements ItemClickSupport.OnItemClickListener {
    private List<ActivePlayer> activePlayers;
    private List<Integer> flippedPositions = new ArrayList<>();
    private OnCardSelectListener onCardSelectListener;
    private boolean disableClick;

    public PlayerCardAdapter(final List<ActivePlayer> activePlayers, final int cardSize) {
        super(cardSize);
        this.activePlayers = activePlayers;
    }

    public void setOnCardSelectListener(PlayerCardAdapter.OnCardSelectListener onCardClickListener) {
        this.onCardSelectListener = onCardClickListener;
    }

    public void setDisableClick(final boolean disableClick) {
        this.disableClick = disableClick;
    }

    public void flipActivePlayer(final ActivePlayer activePlayer) {
        final int position = activePlayers.indexOf(activePlayer);
        if (position > -1) {
            if (flippedPositions.contains(position)) {
                return;
            }
            flippedPositions.add(position);
            notifyDataSetChanged();
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCustomCreateViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_sith_card_flip, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int itemPosition = (int) getItemId(position);

        ((PlayerCardAdapter.ViewHolder) holder).onBind(activePlayers.get(itemPosition));
        if (flippedPositions.contains(itemPosition)) {
            ((PlayerCardAdapter.ViewHolder) holder).flipView.setDisplayedChild(1);
        } else {
            ((PlayerCardAdapter.ViewHolder) holder).flipView.setDisplayedChild(0);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return activePlayers.size();
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
        if (disableClick) {
            return;
        }

        final int itemPosition = (int) getItemId(position);

        ViewFlipper flipView = (ViewFlipper) v.findViewById(R.id.flipView);
        if (flippedPositions.contains(itemPosition)) {
            AnimationFactory.flipTransition(flipView, AnimationFactory.FlipDirection.LEFT_RIGHT);
            flippedPositions.remove(Integer.valueOf(itemPosition));
        } else {
            AnimationFactory.flipTransition(flipView, AnimationFactory.FlipDirection.LEFT_RIGHT);
            flippedPositions.add(itemPosition);
            if (onCardSelectListener != null) {
                onCardSelectListener.onCardSelected(activePlayers.get(itemPosition));
            }
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewFlipper flipView;
        private final TextView playerNameText;
        private final SithCardView sithCardView;

        ViewHolder(final View itemView) {
            super(itemView);
            flipView = (ViewFlipper) itemView.findViewById(R.id.flipView);
            playerNameText = (TextView) itemView.findViewById(R.id.playerCardName);
            sithCardView = (SithCardView) itemView.findViewById(R.id.sithCard);

        }

        void onBind(final ActivePlayer player) {
            playerNameText.setText(player.getName());
            sithCardView.setSithCard(player.getSithCard());
        }
    }

    public interface OnCardSelectListener {
        void onCardSelected(ActivePlayer activePlayer);
    }
}
