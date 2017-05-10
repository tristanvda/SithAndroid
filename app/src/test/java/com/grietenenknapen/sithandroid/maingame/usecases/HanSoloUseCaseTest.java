package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.SithCard;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HanSoloUseCaseTest {

    private static final SithCard SELECTED_SITH_CARD = new SithCard();

    private HanSoloUseCase hanSoloUseCase;

    @Mock
    private HanSoloUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHanSolo_RoundOne_Active() {
        hanSoloUseCase = new HanSoloUseCase(callBack, true, false);

        hanSoloUseCase.onSetupUseCase(1);

        Assert.assertEquals(hanSoloUseCase.finishUseCase(1), false);
        hanSoloUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis2_hansolowordtwakker, R.string.basis2_han_solo_wordt_wakker, hanSoloUseCase);
        hanSoloUseCase.onExecuteStep(1);

        Assert.assertEquals(hanSoloUseCase.finishUseCase(2), false);
        hanSoloUseCase.onPrepareStep(2);
        Mockito.verify(callBack).requestUserCardSelection(hanSoloUseCase);
        hanSoloUseCase.onExecuteStep(2, SELECTED_SITH_CARD);
        Mockito.verify(callBack).switchHanSoloUserCard(SELECTED_SITH_CARD);

        Assert.assertEquals(hanSoloUseCase.finishUseCase(3), false);
        hanSoloUseCase.onPrepareStep(3);
        Mockito.verify(callBack).speak(R.raw.basis3_hansolomagteruggaanlslapen, R.string.basis3_han_solo_mag_terug_gaan_slapen, hanSoloUseCase);
        hanSoloUseCase.onExecuteStep(3);

        Assert.assertEquals(hanSoloUseCase.finishUseCase(4), true);

        Mockito.verify(callBack, Mockito.times(1)).playHanSoloMusic();
        Mockito.verify(callBack, Mockito.times(1)).stopPlayingMusic();
        Mockito.verify(callBack, Mockito.times(3)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testHanSolo_RoundTwo_Active() {
        hanSoloUseCase = new HanSoloUseCase(callBack, true, false);

        hanSoloUseCase.onSetupUseCase(2);

        Assert.assertEquals(hanSoloUseCase.finishUseCase(1), true);
    }

    @Test
    public void testHanSolo_RoundOne_Skip() {
        hanSoloUseCase = new HanSoloUseCase(callBack, true, true);

        hanSoloUseCase.onSetupUseCase(1);
        Assert.assertEquals(hanSoloUseCase.finishUseCase(1), true);
    }

}
