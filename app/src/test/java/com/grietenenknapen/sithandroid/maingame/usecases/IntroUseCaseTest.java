package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class IntroUseCaseTest {

    private IntroUseCase introUseCase;

    @Mock
    private IntroUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIntro_RoundOne_Skip() {
        introUseCase = new IntroUseCase(callBack, true, true);

        introUseCase.onSetupUseCase(1);
        Assert.assertEquals(introUseCase.finishUseCase(1), true);
    }

    @Test
    public void testIntro_RoundOne_Active() {
        introUseCase = new IntroUseCase(callBack, true, false);

        introUseCase.onSetupUseCase(1);

        Assert.assertEquals(introUseCase.finishUseCase(1), false);
        introUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis1_intro, R.string.basis1_intro, introUseCase);
        introUseCase.onExecuteStep(1);

        Mockito.verify(callBack).nextStep(0);

        Assert.assertEquals(introUseCase.finishUseCase(2), true);
    }

}
