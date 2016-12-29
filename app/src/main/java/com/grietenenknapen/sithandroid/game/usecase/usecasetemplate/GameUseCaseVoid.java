package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;


public abstract class GameUseCaseVoid<L extends UseCaseCallBack> extends GameUseCase<Void, L> {

    public GameUseCaseVoid(L flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onExecuteStep(int step, Void stepData) {
        super.onExecuteStep(step, stepData);
    }
}