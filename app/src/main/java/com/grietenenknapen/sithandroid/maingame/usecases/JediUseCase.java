package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

public class JediUseCase extends SkipUseCase {

    public JediUseCase(UseCaseCallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }
}
