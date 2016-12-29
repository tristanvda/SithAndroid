package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;


import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public abstract class GameUseCaseId<L extends UseCaseCallBack> extends GameUseCase<Long, L> {

    public GameUseCaseId(L flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onExecuteStep(int step, Long stepData) {
        super.onExecuteStep(step, stepData);
    }
}