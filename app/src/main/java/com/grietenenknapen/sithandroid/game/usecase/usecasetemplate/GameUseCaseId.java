package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;


import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public abstract class GameUseCaseId<L extends UseCaseCallBack> extends GameUseCase<Long, L> {

    public GameUseCaseId(L flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }
}