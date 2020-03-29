package com.grietenenknapen.sithandroid.model.game;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class GameCardType {

    private GameCardType() {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BB8, SITH, JEDI, HAN_SOLO, BOBA_FETT, KYLO_REN, PEEPING_FINN,
            MAZ_KANATA, CHEWBACCA, GENERAL_GRIEVOUS, GALEN_ERSO, R2D2, JAR_JAR_BINKS})
    public @interface CardType {
    }

    public static final int BB8 = 1;
    public static final int SITH = 2;
    public static final int JEDI = 3;
    public static final int HAN_SOLO = 4;
    public static final int BOBA_FETT = 5;
    public static final int KYLO_REN = 6;
    public static final int PEEPING_FINN = 7;
    public static final int MAZ_KANATA = 8;
    public static final int CHEWBACCA = 9;
    public static final int GENERAL_GRIEVOUS = 10;
    public static final int JAR_JAR_BINKS = 11;
    public static final int GALEN_ERSO = 12;
    public static final int R2D2 = 13;
}
