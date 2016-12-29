package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;

import android.util.Pair;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public abstract class GameUseCaseYesNoId<L extends UseCaseCallBack> extends GameUseCase<Pair<Boolean, Long>, L> {

    public GameUseCaseYesNoId(L flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onExecuteStep(int step, Pair<Boolean, Long> stepData) {
        super.onExecuteStep(step, stepData);
    }
}