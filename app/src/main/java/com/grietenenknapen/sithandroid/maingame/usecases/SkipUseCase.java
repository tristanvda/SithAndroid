package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseVoid;

public class SkipUseCase extends GameUseCaseVoid<SkipUseCase.CallBack> {

    public SkipUseCase(CallBack flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }

    @Override
    public void onPrepareStep(int step) {

    }

    @Override
    protected void onUseCaseExecuteStep(int step, Void stepData) {

    }

    @Override
    public boolean finishUseCase(int step) {
        return true;
    }

    public interface CallBack extends UseCaseCallBack {

    }
}
