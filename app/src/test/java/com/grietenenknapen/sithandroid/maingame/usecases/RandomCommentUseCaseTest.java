package com.grietenenknapen.sithandroid.maingame.usecases;


import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class RandomCommentUseCaseTest {
    private static final int SOUND_RES = 1;
    private static final int STRING_RES = 2;

    private RandomCommentUseCase jediUseCase;

    @Mock
    private RandomCommentUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRandomComment_RoundOne_Skip() {
        jediUseCase = new RandomCommentUseCase(callBack, true, true, SOUND_RES, STRING_RES);

        jediUseCase.onSetupUseCase(1);
        Assert.assertEquals(jediUseCase.finishUseCase(1), true);
    }

    @Test
    public void testPeepingFinn_RoundOne_Active() {
        jediUseCase = new RandomCommentUseCase(callBack, true, false, SOUND_RES, STRING_RES);

        jediUseCase.onSetupUseCase(1);

        Assert.assertEquals(jediUseCase.finishUseCase(1), false);
        jediUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(SOUND_RES, STRING_RES, jediUseCase);
        jediUseCase.onExecuteStep(1);

        Mockito.verify(callBack).nextStep(0);

        Assert.assertEquals(jediUseCase.finishUseCase(2), true);
    }
}
