package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

public class ChewBaccaUseCase extends SkipUseCase {

    public ChewBaccaUseCase(UseCaseCallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }
}
