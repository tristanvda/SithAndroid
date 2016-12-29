package com.grietenenknapen.sithandroid.maingame.usecases;


import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;

public abstract class GameUseCaseCard<L extends UseCaseCallBack> extends GameUseCase<SithCard, L> {

    public GameUseCaseCard(L flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onExecuteStep(int step, SithCard stepData) {
        super.onExecuteStep(step, stepData);
    }
}


