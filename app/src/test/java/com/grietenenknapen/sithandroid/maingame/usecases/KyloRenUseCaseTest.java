package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.SithCard;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class KyloRenUseCaseTest {

    private KyloRenUseCase kyloRenUseCase;

    @Mock
    private KyloRenUseCase.CallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testKyloRen_RoundThree_Active() {
        kyloRenUseCase = new KyloRenUseCase(callBack, true, false);

        kyloRenUseCase.onSetupUseCase(3);

        Assert.assertEquals(kyloRenUseCase.finishUseCase(1), false);
        kyloRenUseCase.onPrepareStep(1);
        Mockito.verify(callBack).speak(R.raw.basis14_kylorenontwaakt, R.string.basis14_kylo_ren_ontwaakt, kyloRenUseCase);
        kyloRenUseCase.onExecuteStep(1);

        Assert.assertEquals(kyloRenUseCase.finishUseCase(2), true);

        Mockito.verify(callBack, Mockito.times(1)).nextStep(Mockito.anyInt());
    }

    @Test
    public void testKyloRen_RoundOne_Active() {
        kyloRenUseCase = new KyloRenUseCase(callBack, true, false);

        kyloRenUseCase.onSetupUseCase(1);

        Assert.assertEquals(kyloRenUseCase.finishUseCase(1), true);
    }

    @Test
    public void testKyloRen_RoundThree_Skip() {
        kyloRenUseCase = new KyloRenUseCase(callBack, true, true);

        kyloRenUseCase.onSetupUseCase(3);
        Assert.assertEquals(kyloRenUseCase.finishUseCase(1), true);
    }

}
