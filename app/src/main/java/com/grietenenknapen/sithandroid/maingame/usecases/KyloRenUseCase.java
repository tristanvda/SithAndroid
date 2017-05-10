package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public class KyloRenUseCase extends GameUseCase<KyloRenUseCase.CallBack> {

    public KyloRenUseCase(CallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(int step) {
        flowManagerListener.speak(R.raw.basis14_kylorenontwaakt,
                R.string.basis14_kylo_ren_ontwaakt, this);
    }

    @Override
    public void onExecuteStep(final int step) {
        if (active) {
            flowManagerListener.turnKyloRenToDarkSide();
        }
        this.handleExecuteStep();
    }

    @Override
    public boolean finishUseCase(int step) {
        return super.finishUseCase(step) || (round != 3) || step > 1;
    }

    public interface CallBack extends UseCaseCallBack {

        void turnKyloRenToDarkSide();

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);
    }
}
