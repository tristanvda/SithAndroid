package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;

public class SithUseCase extends GameUseCaseId<SithUseCase.CallBack> {

    public SithUseCase(CallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(final int step) {
        switch (step) {
            case 1:
                flowManagerListener.speak(R.raw.basis9_sithwordenwakker,
                        R.string.basis9_sith_worden_wakker, this);
                break;
            case 2:
                flowManagerListener.playSithMusic();
                if (!active) {
                    flowManagerListener.skipStepDelay(delayShort);
                } else {
                    flowManagerListener.requestUserPlayerSelectionSith(this);
                }
                break;
            case 3:
                flowManagerListener.stopPlayingMusic();
                flowManagerListener.speak(R.raw.basis10_sithmogenteruggaanslapen,
                        R.string.basis10_sith_mogen_terug_gaan_slapen, this);
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
        return super.finishUseCase(step) || (step > 3);
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserPlayerSelectionSith(GameUseCaseId useCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void markUserAsDeath(long id);

        void playSithMusic();

        void stopPlayingMusic();

    }
}
