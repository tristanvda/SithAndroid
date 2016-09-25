package com.grietenenknapen.sithandroid.maingame;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DayCycle {
    @Retention(RetentionPolicy.SOURCE)

    @IntDef({CYCLE_DAY, CYCLE_NIGHT})
    public @interface Cycle {
    }

    public static final int CYCLE_DAY = 0;
    public static final int CYCLE_NIGHT = 1;
}
