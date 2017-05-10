package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

public class PeepingFinnUseCase extends SkipUseCase {

    public PeepingFinnUseCase(UseCaseCallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }
}
