package com.grietenenknapen.sithandroid.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.ui.views.SithCardView;
import com.grietenenknapen.sithandroid.util.ItemClickSupport;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends BaseGridAdapter<CardAdapter.ViewHolder> implements ItemClickSupport.OnItemClickListener {
    public static final int SELECTION_LIMIT_NONE = -1;

    private int maxItemSelection;
    private final List<SithCard> sithCards;
    private final List<Integer> selectedPositions;
    private OnCardSelectListener onCardSelectListener;

    public CardAdapter(final List<SithCard> sithCards, final int cardSize) {
        super(cardSize);
        this.sithCards = sithCards;
        selectedPositions = new ArrayList<>();
    }

    public void selectSithCard(final SithCard sithCard) {
        final int position = sithCards.indexOf(sithCard);
        if (position > -1) {
            if (selectedPositions.contains(position)) {
                return;
            }
            selectedPositions.add(position);
            notifyDataSetChanged();
        }
    }

    public void setOnCardSelectListener(final CardAdapter.OnCardSelectListener onCardSelectListener) {
        this.onCardSelectListener = onCardSelectListener;
    }

    public List<SithCard> getSelectedSithCards() {
        List<SithCard> selectedCards = new ArrayList<>();
        for (Integer position : selectedPositions) {
            selectedCards.add(this.sithCards.get(position));
        }
        return selectedCards;
    }

    public void setMaxItemSelection(final int maxSelectItems) {
        this.maxItemSelection = maxSelectItems;
    }

    @Override
    protected CardAdapter.ViewHolder onCustomCreateViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CardAdapter.ViewHolder(layoutInflater.inflate(R.layout.list_item_sith_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final CardAdapter.ViewHolder holder, final int position) {
        holder.onBind(sithCards.get(position));
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
            if (onCardSelectListener != null) {
                onCardSelectListener.onCardSelectionChanged(getSelectedSithCards());
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

            if (onCardSelectListener != null) {
                onCardSelectListener.onCardSelectionChanged(getSelectedSithCards());
            }
        }

        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return sithCards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final SithCardView sithCardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            sithCardView = (SithCardView) itemView.findViewById(R.id.sithCard);
        }

        void onBind(final SithCard sithCard) {
            sithCardView.setSithCard(sithCard);
        }
    }

    public interface OnCardSelectListener {
        void onCardSelectionChanged(List<SithCard> selectedCards);
    }
}
