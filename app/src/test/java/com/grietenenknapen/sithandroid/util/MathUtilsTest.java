package com.grietenenknapen.sithandroid.util;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void generateRandomInteger_Within_Bounds() {
        for (int i = 0; i < 100; i++) {
            final int randomNumber = MathUtils.generateRandomInteger(0, 4);
            System.out.printf(String.valueOf(randomNumber));
            Assert.assertTrue(randomNumber >= 0 && randomNumber <= 4);
        }
    }
}