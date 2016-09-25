package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;

public class SithUseCase extends GameUseCaseId<SithUseCase.CallBack> {
    private static final long DELAY_SHORT = 3 * 1000;

    public SithUseCase(CallBack flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }

    @Override
    public void onPrepareStep(final int step) {
        switch (step) {
            case 1:
                flowManagerListener.speak(1, 1, this);
                break;
            case 2:
                if (!active) {
                    flowManagerListener.skipStepDelay(DELAY_SHORT);
                } else {
                    flowManagerListener.requestUserPlayerSelection(this);
                }
                break;
            case 3:
                flowManagerListener.speak(1, 1, this);
                break;
        }
    }

    @Override
    protected void onUseCaseExecuteStep(final int step, final Long stepData) {
        if (step == 2 && stepData != null) {
            flowManagerListener.markUserAsDeath(stepData);
        }
    }

    @Override
    public boolean finishUseCase(final int step) {
        return (step > 3);
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserPlayerSelection(GameUseCaseId useCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void markUserAsDeath(long id);

    }
}
