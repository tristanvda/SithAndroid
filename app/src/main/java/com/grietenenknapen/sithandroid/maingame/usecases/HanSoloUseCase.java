package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;

public class HanSoloUseCase extends GameUseCaseCard<HanSoloUseCase.CallBack> {
    private static final long DELAY_SHORT = 3 * 1000;

    public HanSoloUseCase(CallBack flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }

    @Override
    public void onPrepareStep(final int step) {

        switch (step) {
            case 1:
                flowManagerListener.speak(1, 1, this);
                break;
            case 2:
                if (!active){
                    flowManagerListener.skipStepDelay(DELAY_SHORT);
                } else {
                    flowManagerListener.requestUserCardSelection(this);
                }
                break;
            case 3:
                flowManagerListener.speak(1, 1, this);
                break;
        }
    }

    @Override
    protected void onUseCaseExecuteStep(final int step, final SithCard stepData) {
       if (step == 2 && stepData != null){
            flowManagerListener.switchHanSoloUserCard(stepData);
       }
    }

    @Override
    public boolean finishUseCase(final int step) {
        return (round > 1 && step >= 1) || step > 3;
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserCardSelection(GameUseCaseCard useCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void switchHanSoloUserCard(SithCard sithCard);
    }
}
