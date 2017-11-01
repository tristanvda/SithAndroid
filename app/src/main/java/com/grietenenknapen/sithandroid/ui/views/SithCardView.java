package com.grietenenknapen.sithandroid.ui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SithCardView extends RoundedFrameLayout {

    private SithCard sithCard = null;
    private boolean inflated = false;

    @BindView(R.id.sithCardImage)
    ImageView cardImage;
    @BindView(R.id.sithCardLeftColorView)
    View cardColorLeft;
    @BindView(R.id.sithCardRightColorView)
    View cardColorRight;

    public SithCardView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public SithCardView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SithCardView(final Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.sith_card, this);
        ButterKnife.bind(this);
        inflated = true;
        updateLayout();
    }

    public void setSithCard(final SithCard sithCard) {
        this.sithCard = sithCard;
        updateLayout();
    }

    private void updateLayout() {
        if (sithCard != null && inflated) {
            final int drawableResId = ResourceUtils.getResIdFromResString(getContext(), sithCard.getImageResId(),
                    ResourceUtils.RES_TYPE_DRAWABLE);

            final int colorResId = ResourceUtils.getResIdFromResString(getContext(),
                    sithCard.getColorResId(), ResourceUtils.RES_TYPE_COLOR);

            Glide.with(getContext())
                    .load(drawableResId)
                    .fitCenter()
                    .into(cardImage);

            cardColorLeft.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));

            if (sithCard.getCardType() == GameCardType.KYLO_REN) {
                cardColorRight.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.card_red));
            } else {
                cardColorRight.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
            }
        }
    }
}
