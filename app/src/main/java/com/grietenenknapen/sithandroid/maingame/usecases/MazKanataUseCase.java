package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;


public class MazKanataUseCase extends GameUseCaseId<MazKanataUseCase.CallBack> {
    private static final long DELAY_SHORT = 3 * 1000;

    public MazKanataUseCase(CallBack flowManagerListener, boolean active) {
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
                    flowManagerListener.requestUserCardPeek(this);
                }
                break;
        }
    }

    @Override
    protected void onUseCaseExecuteStep(final int step, final Long stepData) {

    }

    @Override
    public boolean finishUseCase(final int step) {
        return step > 2;
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserCardPeek(GameUseCase gameUseCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

    }
}
