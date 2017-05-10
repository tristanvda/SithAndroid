package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SithUseCaseTest {
    private SithUseCase sithUseCase;

    @Mock
    private SithUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSith_RoundOne_Active() {
        sithUseCase = new SithUseCase(callBack, true, false);

        sithUseCase.onSetupUseCase(1);

        Assert.assertEquals(sithUseCase.finishUseCase(1), false);
        sithUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis9_sithwordenwakker, R.string.basis9_sith_worden_wakker, sithUseCase);
        sithUseCase.onExecuteStep(1);

        Assert.assertEquals(sithUseCase.finishUseCase(2), false);
        sithUseCase.onPrepareStep(2);
        Mockito.verify(callBack).requestUserPlayerSelectionSith(sithUseCase);
        sithUseCase.onExecuteStep(2, 1L);
        Mockito.verify(callBack).markUserAsDeath(1L);

        Assert.assertEquals(sithUseCase.finishUseCase(3), false);
        sithUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis10_sithmogenteruggaanslapen, R.string.basis10_sith_mogen_terug_gaan_slapen, sithUseCase);
        sithUseCase.onExecuteStep(3);

        Assert.assertEquals(sithUseCase.finishUseCase(4), true);
        Mockito.verify(callBack, Mockito.times(1)).playSithMusic();
        Mockito.verify(callBack, Mockito.times(1)).stopPlayingMusic();

        Mockito.verify(callBack, Mockito.times(3)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testSith_RoundOne_NotActive() {
        sithUseCase = new SithUseCase(callBack, false, false);

        sithUseCase.onSetupUseCase(1);

        Assert.assertEquals(sithUseCase.finishUseCase(1), false);
        sithUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis9_sithwordenwakker, R.string.basis9_sith_worden_wakker, sithUseCase);
        sithUseCase.onExecuteStep(1);

        Assert.assertEquals(sithUseCase.finishUseCase(2), false);
        sithUseCase.onPrepareStep(2);
        Mockito.verify(callBack).skipStepDelay(Mockito.anyLong());
        sithUseCase.onExecuteStep(2, 1L);
        Mockito.verify(callBack).markUserAsDeath(1L);

        Assert.assertEquals(sithUseCase.finishUseCase(3), false);
        sithUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis10_sithmogenteruggaanslapen, R.string.basis10_sith_mogen_terug_gaan_slapen, sithUseCase);
        sithUseCase.onExecuteStep(3);

        Assert.assertEquals(sithUseCase.finishUseCase(4), true);
        Mockito.verify(callBack, Mockito.times(1)).playSithMusic();
        Mockito.verify(callBack, Mockito.times(1)).stopPlayingMusic();

        Mockito.verify(callBack, Mockito.times(3)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testSith_RoundOne_Skip() {
        sithUseCase = new SithUseCase(callBack, true, true);

        sithUseCase.onSetupUseCase(1);
        Assert.assertEquals(sithUseCase.finishUseCase(1), true);
    }
}
