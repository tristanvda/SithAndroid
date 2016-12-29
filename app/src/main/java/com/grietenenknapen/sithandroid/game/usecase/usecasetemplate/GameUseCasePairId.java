package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;

import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public abstract class GameUseCasePairId<L extends UseCaseCallBack> extends GameUseCase<Pair<Long, Long>, L> {

    public GameUseCasePairId(L flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onExecuteStep(int step, Pair<Long, Long> stepData) {
        super.onExecuteStep(step, stepData);
    }
}
