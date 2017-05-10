package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ChewbaccaUseCaseTest {
    private ChewBaccaUseCase bb8UseCase;

    @Mock
    private UseCaseCallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testChewbacca_RoundOne_Skip() {
        bb8UseCase = new ChewBaccaUseCase(callBack, true, true);

        bb8UseCase.onSetupUseCase(1);
        Assert.assertEquals(bb8UseCase.finishUseCase(1), true);
    }

    @Test
    public void testChewbacca_RoundOne_Active() {
        bb8UseCase = new ChewBaccaUseCase(callBack, true, false);

        bb8UseCase.onSetupUseCase(1);
        Assert.assertEquals(bb8UseCase.finishUseCase(1), true);
    }

}
