package com.grietenenknapen.sithandroid.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.ui.Animation.AnimationFactory;
import com.grietenenknapen.sithandroid.ui.Animation.FlipAnimation;
import com.grietenenknapen.sithandroid.util.ItemClickSupport;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayerCardAdapter extends BaseGridAdapter<RecyclerView.ViewHolder> implements ItemClickSupport.OnItemClickListener {
    private List<ActivePlayer> activePlayers;
    private Activity context;
    private List<Integer> flippedPositions = new ArrayList<>();
    private OnCardSelectListener onCardSelectListener;
    private boolean disableClick;

    public PlayerCardAdapter(Activity context, List<ActivePlayer> activePlayers, final int cardSize) {
        super(cardSize);
        this.activePlayers = activePlayers;
        this.context = context;
    }

    public void setOnCardSelectListener(PlayerCardAdapter.OnCardSelectListener onCardClickListener) {
        this.onCardSelectListener = onCardClickListener;
    }

    public void setDisableClick(boolean disableClick) {
        this.disableClick = disableClick;
    }

    public void flipActivePlayer(ActivePlayer activePlayer) {
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
    protected RecyclerView.ViewHolder onCustomCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_sith_card_flip, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemPosition = (int) getItemId(position);

        ((PlayerCardAdapter.ViewHolder) holder).onBind(context, activePlayers.get(itemPosition));
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
        public final ViewFlipper flipView;
        private final TextView playerNameText;
        private final ImageView cardImage;
        private final View cardColorLeft;
        private final View cardColorRight;

        ViewHolder(View itemView) {
            super(itemView);
            flipView = (ViewFlipper) itemView.findViewById(R.id.flipView);
            playerNameText = (TextView) itemView.findViewById(R.id.playerCardName);
            cardImage = (ImageView) itemView.findViewById(R.id.sithCardImage);
            cardColorLeft = itemView.findViewById(R.id.sithCardLeftColorView);
            cardColorRight = itemView.findViewById(R.id.sithCardRightColorView);
        }

        void onBind(Context context, ActivePlayer player) {
            playerNameText.setText(player.getName());

            final int drawableResId = ResourceUtils.getResIdFromResString(context,
                    player.getSithCard().getImageResId(),
                    ResourceUtils.RES_TYPE_DRAWABLE);

            final int colorResId = ResourceUtils.getResIdFromResString(context,
                    player.getSithCard().getColorResId(), ResourceUtils.RES_TYPE_COLOR);

            Glide.with(context)
                    .load(drawableResId)
                    .fitCenter()
                    .into(cardImage);


            cardColorLeft.setBackgroundColor(ContextCompat.getColor(context, colorResId));

            if (player.getSithCard().getCardType() == GameCardType.KYLO_REN) {
                cardColorRight.setBackgroundColor(ContextCompat.getColor(context, R.color.card_red));
            } else {
                cardColorRight.setBackgroundColor(ContextCompat.getColor(context, colorResId));
            }

        }
    }

    public interface OnCardSelectListener {
        void onCardSelected(ActivePlayer activePlayer);
    }
}
