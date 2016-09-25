package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseVoid;

public class KyloRenUseCase extends GameUseCaseVoid<KyloRenUseCase.CallBack> {

    public KyloRenUseCase(CallBack flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }

    @Override
    public void onPrepareStep(int step) {
        flowManagerListener.speak(1, 1, this);
    }

    @Override
    protected void onUseCaseExecuteStep(int step, Void stepData) {
        if (active) {
            flowManagerListener.turnKyloRenToDarkSide();
        }
    }

    @Override
    public boolean finishUseCase(int step) {
        return (round != 3) || step > 1;
    }

    public interface CallBack extends UseCaseCallBack {

        void turnKyloRenToDarkSide();

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);
    }
}
