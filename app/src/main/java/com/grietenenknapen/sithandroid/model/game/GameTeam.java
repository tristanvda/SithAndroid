package com.grietenenknapen.sithandroid.model.game;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class GameTeam {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SITH, JEDI, LOVERS})

    public @interface Team {
    }

    public static final int SITH = 1;
    public static final int JEDI = 2;
    public static final int LOVERS = 3;

    private GameTeam() {
    }

    @GameTeam.Team
    public static int getInitialTeamFromCardType(@GameCardType.CardType int cardType) {
        if (cardType == GameCardType.SITH || cardType == GameCardType.GENERAL_GRIEVOUS) {
            return SITH;
        } else {
            return JEDI;
        }
    }
}
