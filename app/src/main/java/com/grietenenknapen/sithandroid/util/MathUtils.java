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

    public static int getRandomWithExclusion(int start, int end, int... exclude) {
        int random = start + getRandom().nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    public static int generateRandomInteger(final int min, final int max) {
        final Random r = getRandom();
        return r.nextInt((max - min) + 1) + min;
    }
}
