package com.grietenenknapen.sithandroid.util;

import java.util.Random;

public final class MathUtils {

    private MathUtils() {

    }

    public static int generateRandomInteger(final int min, final int max) {
        final Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
