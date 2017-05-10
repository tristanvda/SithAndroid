package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public class SkipUseCase extends GameUseCase<UseCaseCallBack> {

    public SkipUseCase(UseCaseCallBack flowManagerListener) {
        super(flowManagerListener, false, false);
    }

    public SkipUseCase(UseCaseCallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(int step) {
        //Do nothing
    }

    @Override
    public boolean finishUseCase(int step) {
        return true;
    }

}
