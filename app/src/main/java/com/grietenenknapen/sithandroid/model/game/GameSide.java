package com.grietenenknapen.sithandroid.model.game;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class GameSide {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SITH, JEDI})

    public @interface Side {
    }

    public static final int SITH = 1;
    public static final int JEDI = 2;

    private GameSide() {
    }

    @GameSide.Side
    public static int getSideFromCardType(@GameCardType.CardType int cardType) {
        if (cardType == GameCardType.SITH || cardType == GameCardType.GENERAL_GRIEVOUS) {
            return SITH;
        } else {
            return JEDI;
        }
    }
}
