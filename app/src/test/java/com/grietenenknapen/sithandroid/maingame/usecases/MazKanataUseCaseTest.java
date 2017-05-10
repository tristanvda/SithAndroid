package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MazKanataUseCaseTest {

    private MazKanataUseCase mazKanataUseCase;

    @Mock
    private MazKanataUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMazKanata_RoundOne_Active() {
        mazKanataUseCase = new MazKanataUseCase(callBack, true, false);

        mazKanataUseCase.onSetupUseCase(1);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(1), false);
        mazKanataUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis7_mazkanatawordtwakker, R.string.basis7_maz_kanata_wordt_wakker, mazKanataUseCase);
        mazKanataUseCase.onExecuteStep(1);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(2), false);
        mazKanataUseCase.onPrepareStep(2);
        Mockito.verify(callBack).requestUserCardPeek(mazKanataUseCase);
        mazKanataUseCase.onExecuteStep(2, 0);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(3), false);
        mazKanataUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis8_mazkanatamagteruggaanslapen, R.string.basis8_maz_kanata_mag_terug_gaan_slapen, mazKanataUseCase);
        mazKanataUseCase.onExecuteStep(3);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(4), true);

        Mockito.verify(callBack).playMazKanataMusic();
        Mockito.verify(callBack).stopPlayingMusic();
        Mockito.verify(callBack, Mockito.times(3)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testMazKanata_RoundOne_NotActive() {
        mazKanataUseCase = new MazKanataUseCase(callBack, false, false);

        mazKanataUseCase.onSetupUseCase(1);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(1), false);
        mazKanataUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis7_mazkanatawordtwakker, R.string.basis7_maz_kanata_wordt_wakker, mazKanataUseCase);
        mazKanataUseCase.onExecuteStep(1);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(2), false);
        mazKanataUseCase.onPrepareStep(2);
        Mockito.verify(callBack).skipStepDelay(Mockito.anyLong());
        mazKanataUseCase.onExecuteStep(2, 0);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(3), false);
        mazKanataUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis8_mazkanatamagteruggaanslapen, R.string.basis8_maz_kanata_mag_terug_gaan_slapen, mazKanataUseCase);
        mazKanataUseCase.onExecuteStep(3);

        Assert.assertEquals(mazKanataUseCase.finishUseCase(4), true);

        Mockito.verify(callBack).playMazKanataMusic();
        Mockito.verify(callBack).stopPlayingMusic();
        Mockito.verify(callBack, Mockito.times(3)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testMazKanata_RoundOne_Skip() {
        mazKanataUseCase = new MazKanataUseCase(callBack, true, true);

        mazKanataUseCase.onSetupUseCase(1);
        Assert.assertEquals(mazKanataUseCase.finishUseCase(1), true);
    }

}
