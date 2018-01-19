package com.grietenenknapen.sithandroid.util;

import java.util.Random;

public final class MathUtils {
    private static Random random;

    private MathUtils() {
    }

    private static Random getRandom() {
        if (random == null) {
            random = new Random();
        }

        return random;
    }

    public static int generateRandomInteger(final int min, final int max) {
        final Random r = getRandom();
        return r.nextInt((max - min) + 1) + min;
    }
}
