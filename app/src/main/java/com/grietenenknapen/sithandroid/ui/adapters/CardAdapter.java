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
import com.grietenenknapen.sithandroid.util.ItemClickSupport;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends BaseGridAdapter<CardAdapter.ViewHolder> implements ItemClickSupport.OnItemClickListener {
    public static final int SELECTION_LIMIT_NONE = -1;

    private Context context;
    private int maxItemSelection;
    private final List<SithCard> sithCards;
    private final List<Integer> selectedPositions;
    private OnCardSelectListener onCardSelectListener;

    public CardAdapter(final Context context, final List<SithCard> sithCards, final int cardSize) {
        super(cardSize);
        this.sithCards = sithCards;
        this.context = context;
        selectedPositions = new ArrayList<>();
    }

    public void selectSithCard(SithCard sithCard) {
        final int position = sithCards.indexOf(sithCard);
        if (position > -1) {
            if (selectedPositions.contains(position)) {
                return;
            }
            selectedPositions.add(position);
            notifyDataSetChanged();
        }
    }

    public void setOnCardSelectListener(CardAdapter.OnCardSelectListener onCardSelectListener) {
        this.onCardSelectListener = onCardSelectListener;
    }

    public List<SithCard> getSelectedSithCards() {
        List<SithCard> selectedCards = new ArrayList<>();
        for (Integer position : selectedPositions) {
            selectedCards.add(this.sithCards.get(position));
        }
        return selectedCards;
    }

    public void setMaxItemSelection(int maxSelectItems) {
        this.maxItemSelection = maxSelectItems;
    }

    @Override
    protected CardAdapter.ViewHolder onCustomCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CardAdapter.ViewHolder(layoutInflater.inflate(R.layout.list_item_sith_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        holder.onBind(sithCards.get(position));
        holder.itemView.setSelected(selectedPositions.contains(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        ItemClickSupport.removeFrom(recyclerView);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
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
        private final ImageView cardImage;
        private final View cardColorLeft;
        private final View cardColorRight;

        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.sithCardImage);
            cardColorLeft = itemView.findViewById(R.id.sithCardLeftColorView);
            cardColorRight = itemView.findViewById(R.id.sithCardRightColorView);
        }

        void onBind(SithCard sithCard) {

            final int drawableResId = ResourceUtils.getResIdFromResString(context,
                    sithCard.getImageResId(),
                    ResourceUtils.RES_TYPE_DRAWABLE);

            final int colorResId = ResourceUtils.getResIdFromResString(context,
                    sithCard.getColorResId(), ResourceUtils.RES_TYPE_COLOR);

            Glide.with(context)
                    .load(drawableResId)
                    .fitCenter()
                    .into(cardImage);
            cardColorLeft.setBackgroundColor(ContextCompat.getColor(context, colorResId));
            if (sithCard.getCardType() == GameCardType.KYLO_REN) {
                cardColorRight.setBackgroundColor(ContextCompat.getColor(context, R.color.card_red));
            } else {
                cardColorRight.setBackgroundColor(ContextCompat.getColor(context, colorResId));
            }

        }
    }

    public interface OnCardSelectListener {
        void onCardSelectionChanged(List<SithCard> selectedCards);
    }
}
