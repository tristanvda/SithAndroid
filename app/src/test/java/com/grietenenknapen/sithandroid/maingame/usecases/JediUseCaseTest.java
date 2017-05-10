package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class JediUseCaseTest {

    private JediUseCase jediUseCase;

    @Mock
    private UseCaseCallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJedi_RoundOne_Skip() {
        jediUseCase = new JediUseCase(callBack, true, true);

        jediUseCase.onSetupUseCase(1);
        Assert.assertEquals(jediUseCase.finishUseCase(1), true);
    }

    @Test
    public void testJedi_RoundOne_Active() {
        jediUseCase = new JediUseCase(callBack, true, false);

        jediUseCase.onSetupUseCase(1);
        Assert.assertEquals(jediUseCase.finishUseCase(1), true);
    }

}
