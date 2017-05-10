package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BobaFettUseCaseTest {

    private static final long KILLED_USER_ID = 1L;

    private BobaFettUseCase bobaFettUseCase;

    @Mock
    private BobaFettUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBobaFett_NoHistoryUseMedPackUseRocketLauncher_Active() {
        bobaFettUseCase = new BobaFettUseCase(callBack, true, false, false);

        bobaFettUseCase.onSetupUseCase(1);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(1), false);
        bobaFettUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis11_bobafettwordtwakker, R.string.basis11_boba_fett_wordt_wakker, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(1);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(2), false);
        bobaFettUseCase.onPrepareStep(2);
        Mockito.verify(callBack).showKilledPlayerMedPackYesNo(bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(2, true);
        Mockito.verify(callBack).useMedPack();

        Assert.assertEquals(bobaFettUseCase.finishUseCase(3), false);
        bobaFettUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis12_bobafettrocketlauncher, R.string.basis12_boba_fett_rocketlauncher, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(3);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(4), false);
        bobaFettUseCase.onPrepareStep(4);
        Mockito.verify(callBack).requestYesNoAnswerRocket(bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(4, true);
        Mockito.verify(callBack).setRockedAlreadySelected(true);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(5), false);
        bobaFettUseCase.onPrepareStep(5);
        Mockito.verify(callBack).requestUserPlayerRocketSelection(bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(5, KILLED_USER_ID);
        Mockito.verify(callBack).useRockedLauncher(KILLED_USER_ID);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(6), false);
        bobaFettUseCase.onPrepareStep(6);
        Mockito.verify(callBack).speak(R.raw.basis13_bobafettgaatterugslapen, R.string.basis13_boba_fett_gaat_terug_slapen, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(6);

        Mockito.verify(callBack, Mockito.times(2)).playBobaFettMusic();
        Mockito.verify(callBack, Mockito.times(2)).stopPlayingMusic();

        Assert.assertEquals(bobaFettUseCase.finishUseCase(7), true);

        Mockito.verify(callBack, Mockito.times(6)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testBobaFett_NoHistoryUseNothing_Active() {
        bobaFettUseCase = new BobaFettUseCase(callBack, true, false, false);

        bobaFettUseCase.onSetupUseCase(1);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(1), false);
        bobaFettUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis11_bobafettwordtwakker, R.string.basis11_boba_fett_wordt_wakker, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(1);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(2), false);
        bobaFettUseCase.onPrepareStep(2);
        Mockito.verify(callBack).showKilledPlayerMedPackYesNo(bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(2, false);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(3), false);
        bobaFettUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis12_bobafettrocketlauncher, R.string.basis12_boba_fett_rocketlauncher, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(3);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(4), false);
        bobaFettUseCase.onPrepareStep(4);
        Mockito.verify(callBack).requestYesNoAnswerRocket(bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(4, false);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(5), false);
        bobaFettUseCase.onPrepareStep(5);
        Mockito.verify(callBack).speak(R.raw.basis13_bobafettgaatterugslapen, R.string.basis13_boba_fett_gaat_terug_slapen, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(5);

        Mockito.verify(callBack, Mockito.times(2)).playBobaFettMusic();
        Mockito.verify(callBack, Mockito.times(2)).stopPlayingMusic();

        Assert.assertEquals(bobaFettUseCase.finishUseCase(6), true);

        Mockito.verify(callBack, Mockito.times(5)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testBobaFett_RocketAlreadyUsedUseNothing_Active() {
        bobaFettUseCase = new BobaFettUseCase(callBack, true, false, true);

        bobaFettUseCase.onSetupUseCase(2);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(5), false);
        bobaFettUseCase.onPrepareStep(5);
        Mockito.verify(callBack).requestUserPlayerRocketSelection(bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(5, KILLED_USER_ID);
        Mockito.verify(callBack).useRockedLauncher(KILLED_USER_ID);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(6), false);
        bobaFettUseCase.onPrepareStep(6);
        Mockito.verify(callBack).speak(R.raw.basis13_bobafettgaatterugslapen, R.string.basis13_boba_fett_gaat_terug_slapen, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(6);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(7), true);

        Mockito.verify(callBack, Mockito.times(1)).stopPlayingMusic();
        Mockito.verify(callBack, Mockito.times(2)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testBobaFett_NotActive() {
        bobaFettUseCase = new BobaFettUseCase(callBack, false, false, false);

        bobaFettUseCase.onSetupUseCase(1);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(1), false);
        bobaFettUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis11_bobafettwordtwakker, R.string.basis11_boba_fett_wordt_wakker, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(1);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(2), false);
        bobaFettUseCase.onPrepareStep(2);
        bobaFettUseCase.onExecuteStep(2);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(3), false);
        bobaFettUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis12_bobafettrocketlauncher, R.string.basis12_boba_fett_rocketlauncher, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(3);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(4), false);
        bobaFettUseCase.onPrepareStep(4);
        bobaFettUseCase.onExecuteStep(4);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(5), false);
        bobaFettUseCase.onPrepareStep(5);
        bobaFettUseCase.onExecuteStep(5);

        Assert.assertEquals(bobaFettUseCase.finishUseCase(6), false);
        bobaFettUseCase.onPrepareStep(6);
        Mockito.verify(callBack).speak(R.raw.basis13_bobafettgaatterugslapen, R.string.basis13_boba_fett_gaat_terug_slapen, bobaFettUseCase);
        bobaFettUseCase.onExecuteStep(6);

        Mockito.verify(callBack, Mockito.times(2)).playBobaFettMusic();
        Mockito.verify(callBack, Mockito.times(2)).stopPlayingMusic();

        Assert.assertEquals(bobaFettUseCase.finishUseCase(7), true);

        Mockito.verify(callBack, Mockito.times(2)).skipStepDelay(Mockito.anyLong());
        Mockito.verify(callBack, Mockito.times(1)).skipStep();

        Mockito.verify(callBack, Mockito.times(6)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testBobaFett_RoundOne_Akip() {
        bobaFettUseCase = new BobaFettUseCase(callBack, true, true, false);

        bobaFettUseCase.onSetupUseCase(1);
        Assert.assertEquals(bobaFettUseCase.finishUseCase(1), true);
    }

}
