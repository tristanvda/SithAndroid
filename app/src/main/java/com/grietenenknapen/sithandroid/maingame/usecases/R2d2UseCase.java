package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseIds;

import java.util.List;

public class R2d2UseCase extends GameUseCase<R2d2UseCase.CallBack> implements UseCaseIds {
    private static final int SHUFFLE_PLAYERS = 3;

    public R2d2UseCase(CallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(int step) {
        switch (step) {
            case 1:
                flowManagerListener.speak(R.raw.basis17_r2d2wordtnuwakker,
                        R.string.basis15_r2d2_ontwaakt, this);
                break;
            case 2:
                flowManagerListener.playR2D2Music();
                if (!active) {
                    flowManagerListener.skipStepDelay(delayShort);
                } else {
                    flowManagerListener.requestUserPlayersSelection(this, R.string.r2d2_select_players, SHUFFLE_PLAYERS, SHUFFLE_PLAYERS);
                }
                break;
            case 3:
                flowManagerListener.stopPlayingMusic();
                flowManagerListener.speak(R.raw.basis18_r2d2magteruggaanslapen,
                        R.string.basis15_r2d2_mag_terug_gaan_slapen, this);
                break;
        }
    }

    @Override
    public void onExecuteStep(final int step, final List<Long> stepData) {
        if (step == 2 && stepData != null) {
            flowManagerListener.switchRoles(stepData);
        }
        this.handleExecuteStep();
    }

    @Override
    public boolean finishUseCase(int step) {
        return super.finishUseCase(step) || (round > 1 && step >= 1) || step > 3;
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserPlayersSelection(UseCaseIds useCase, int titleRes, int minNumber, int maxNumber);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void switchRoles(List<Long> players);

        void playR2D2Music();

        void stopPlayingMusic();
    }
}
