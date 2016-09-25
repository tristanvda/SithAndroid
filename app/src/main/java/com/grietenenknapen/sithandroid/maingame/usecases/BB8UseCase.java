package com.grietenenknapen.sithandroid.maingame.usecases;

import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCasePairId;

public class BB8UseCase extends GameUseCasePairId<BB8UseCase.CallBack> {
    private static final long DELAY = 10 * 1000;
    private static final long DELAY_SHORT = 3 * 1000;

    public BB8UseCase(CallBack flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }

    @Override
    public void onPrepareStep(int step) {

        switch (step) {
            case 1:
                flowManagerListener.speak(1, 1, this);
                break;
            case 2:
                if (!active) {
                    flowManagerListener.skipStepDelay(DELAY_SHORT);
                } else {
                    flowManagerListener.requestUserPairPlayerSelection(this);
                }
                break;
            case 3:
                flowManagerListener.speak(1, 1, this);
                break;
            case 4:
                flowManagerListener.showDelay(DELAY, this);
                break;
            case 5:
                flowManagerListener.speak(1, 1, this);
                break;
        }
    }

    @Override
    protected void onUseCaseExecuteStep(int step, Pair<Long, Long> stepData) {
        if (step == 2 && stepData != null) {
            flowManagerListener.linkTwoLovers(stepData.first, stepData.second);
        }
    }

    @Override
    public boolean finishUseCase(int step) {
        return (round > 1 && step >= 1) || step > 5;
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserPairPlayerSelection(GameUseCasePairId useCase);

        void showDelay(long delay, GameUseCase gameUseCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void linkTwoLovers(long lover1Id, long lover2Id);

    }
}
