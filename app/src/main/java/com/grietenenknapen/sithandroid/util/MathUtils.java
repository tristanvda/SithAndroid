package com.grietenenknapen.sithandroid.util;


import java.util.Random;

public class MathUtils {

    private MathUtils() {

    }

    public static int generateRandomInteger(final int min, final int max) {
        final Random r = new Random();
        return r.nextInt(max - min) + min;
    }
}
