package com.grietenenknapen.sithandroid.util;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void generateRandomInteger_Within_Bounds() {
        for (int i = 0; i < 500; i++) {
            final int randomNumber = MathUtils.generateRandomInteger(0, 4);
            System.out.printf(String.valueOf(randomNumber));
            Assert.assertTrue(randomNumber >= 0 && randomNumber <= 4);
        }
    }

    @Test
    public void generateRandomIntegerWithInclusion_Same_Numbers() {
        for (int i = 0; i < 500; i++) {
            final int randomNumber = MathUtils.getRandomWithExclusion(0, 4, 0, 1, 2, 4);
            System.out.printf(String.valueOf(randomNumber));
            Assert.assertTrue(randomNumber == 3);
        }
    }

}
