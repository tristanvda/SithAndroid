package com.grietenenknapen.sithandroid.maingame.usecases;

import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowCallBack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BB8UseCaseTest {
    private BB8UseCase bb8UseCase;

    @Mock
    private BB8UseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBB8_RoundOne_Active() {
        bb8UseCase = new BB8UseCase(callBack, true, false);

        bb8UseCase.onSetupUseCase(1);

        Assert.assertEquals(bb8UseCase.finishUseCase(1), false);
        bb8UseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis4_bb8wordtwakker, R.string.basis4_BB8wordtwakker, bb8UseCase);
        bb8UseCase.onExecuteStep(1);

        Assert.assertEquals(bb8UseCase.finishUseCase(2), false);
        bb8UseCase.onPrepareStep(2);
        Mockito.verify(callBack).requestUserPairPlayerSelection(bb8UseCase);
        bb8UseCase.onExecuteStep(2, new Pair<>(1L, 2L));
        Mockito.verify(callBack).linkTwoLovers(1L, 2L);

        Assert.assertEquals(bb8UseCase.finishUseCase(3), false);
        bb8UseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis5_1_bb8magteruggaanslapen, R.string.basis5_1_bb8_mag_terug_gaan_slapen, bb8UseCase);
        Mockito.verify(callBack).stopPlayingMusic();
        bb8UseCase.onExecuteStep(3);

        Assert.assertEquals(bb8UseCase.finishUseCase(4), false);
        bb8UseCase.onPrepareStep(4);
        Mockito.verify(callBack).speak(R.raw.basis5_2_bb8geliefdenkijkenelkaarliefdevolindeogen, R.string.basis5_2_bb8_geliefden_kijken_elkaar_liefdevol_in_de_ogen, bb8UseCase);
        bb8UseCase.onExecuteStep(4);

        Assert.assertEquals(bb8UseCase.finishUseCase(5), false);
        bb8UseCase.onPrepareStep(5);
        Mockito.verify(callBack).showDelay(Mockito.anyLong(), Mockito.eq(bb8UseCase));
        bb8UseCase.onExecuteStep(5);

        Assert.assertEquals(bb8UseCase.finishUseCase(6), false);
        bb8UseCase.onPrepareStep(6);
        Mockito.verify(callBack).speak(R.raw.basis6_degeliefdenmogenteruggaanslapen, R.string.basis6_de_geliefden_mogen_terug_gaan_slapen, bb8UseCase);
        bb8UseCase.onExecuteStep(6);

        Assert.assertEquals(bb8UseCase.finishUseCase(7), true);
        Mockito.verify(callBack, Mockito.times(2)).playBB8Music();
        Mockito.verify(callBack, Mockito.times(2)).stopPlayingMusic();

        Mockito.verify(callBack, Mockito.times(6)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testBB8_RoundOne_NotActive() {
        bb8UseCase = new BB8UseCase(callBack, false, false);

        bb8UseCase.onSetupUseCase(1);

        Assert.assertEquals(bb8UseCase.finishUseCase(1), false);
        bb8UseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis4_bb8wordtwakker, R.string.basis4_BB8wordtwakker, bb8UseCase);
        bb8UseCase.onExecuteStep(1);

        Assert.assertEquals(bb8UseCase.finishUseCase(2), false);
        bb8UseCase.onPrepareStep(2);
        Mockito.verify(callBack).skipStepDelay(Mockito.anyLong());
        bb8UseCase.onExecuteStep(2, new Pair<>(1L, 2L));
        Mockito.verify(callBack).linkTwoLovers(1L, 2L);

        Assert.assertEquals(bb8UseCase.finishUseCase(3), false);
        bb8UseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis5_1_bb8magteruggaanslapen, R.string.basis5_1_bb8_mag_terug_gaan_slapen, bb8UseCase);
        bb8UseCase.onExecuteStep(3);

        Assert.assertEquals(bb8UseCase.finishUseCase(4), false);
        bb8UseCase.onPrepareStep(4);
        Mockito.verify(callBack).speak(R.raw.basis5_2_bb8geliefdenkijkenelkaarliefdevolindeogen, R.string.basis5_2_bb8_geliefden_kijken_elkaar_liefdevol_in_de_ogen, bb8UseCase);
        bb8UseCase.onExecuteStep(4);

        Assert.assertEquals(bb8UseCase.finishUseCase(5), false);
        bb8UseCase.onPrepareStep(5);
        Mockito.verify(callBack).showDelay(Mockito.anyLong(), Mockito.eq(bb8UseCase));
        bb8UseCase.onExecuteStep(5);

        Assert.assertEquals(bb8UseCase.finishUseCase(6), false);
        bb8UseCase.onPrepareStep(6);
        Mockito.verify(callBack).speak(R.raw.basis6_degeliefdenmogenteruggaanslapen, R.string.basis6_de_geliefden_mogen_terug_gaan_slapen, bb8UseCase);
        bb8UseCase.onExecuteStep(6);

        Assert.assertEquals(bb8UseCase.finishUseCase(7), true);
        Mockito.verify(callBack, Mockito.times(2)).playBB8Music();
        Mockito.verify(callBack, Mockito.times(2)).stopPlayingMusic();

        Mockito.verify(callBack, Mockito.times(6)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testBB8_RoundOne_Skip() {
        bb8UseCase = new BB8UseCase(callBack, true, true);

        bb8UseCase.onSetupUseCase(1);
        Assert.assertEquals(bb8UseCase.finishUseCase(1), true);
    }


    @Test
    public void testBB8_RoundTwo_Active() {
        bb8UseCase = new BB8UseCase(callBack, true, false);

        bb8UseCase.onSetupUseCase(2);
        Assert.assertEquals(bb8UseCase.finishUseCase(1), true);
    }

}
