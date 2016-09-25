package com.grietenenknapen.sithandroid.maingame.usecases;

import android.util.Pair;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNo;

public class BobaFettUseCase extends GameUseCaseYesNo<BobaFettUseCase.CallBack> {
    private static final long DELAY_SHORT = 3 * 1000;

    private boolean rocketAlreadySelected = false;
    private boolean medPackAlreadySelected = false;

    public BobaFettUseCase(CallBack flowManagerListener,
                           boolean active,
                           boolean rockedAlreadySelected,
                           boolean medPackAlreadySelected) {
        super(flowManagerListener, active);
        this.rocketAlreadySelected = rockedAlreadySelected;
        this.medPackAlreadySelected = medPackAlreadySelected;
    }

    @Override
    public void onPrepareStep(final int step) {
        switch (step) {
            case 1:
                flowManagerListener.speak(1, 1, this);
                break;
            case 2:
                flowManagerListener.showKilledPlayerDelay(this);
                break;
            case 3:
                flowManagerListener.speak(1, 1, this);
                break;
            case 4:
                if (!active) {
                    flowManagerListener.skipStepDelay(DELAY_SHORT);
                } else {
                    flowManagerListener.requestYesNoAnswerMedPack(this);
                }
                break;
            case 5:
                flowManagerListener.speak(1, 1, this);
                break;
            case 6:
                if (!active) {
                    flowManagerListener.skipStepDelay(DELAY_SHORT);
                } else {
                    flowManagerListener.requestYesNoAnswerRocket(this);
                }
                break;
            case 7:
                if (!active) {
                    flowManagerListener.skipStep();
                    break;
                }
                if (rocketAlreadySelected) {
                    flowManagerListener.requestUserPlayerSelection(this);
                } else {
                    flowManagerListener.speak(1, 1, this);
                }
                break;
            case 8:
                flowManagerListener.speak(1, 1, this);
                break;
        }
    }

    @Override
    public boolean finishUseCase(final int step) {
        return (step == 8 && !rocketAlreadySelected) || step > 8;
    }

    @Override
    protected void onUseCaseExecuteStep(int step, Pair<Boolean, Long> stepData) {
        if (step == 4 && stepData != null && stepData.first) {
            flowManagerListener.useMedPack();
            medPackAlreadySelected = true;
        } else if (step == 6 && stepData != null && stepData.first) {
            rocketAlreadySelected = true;
        } else if (step == 7 && stepData != null) {
            flowManagerListener.useRockedLauncher(stepData.second);
        }
    }

    public interface CallBack extends UseCaseCallBack {

        void requestYesNoAnswerMedPack(GameUseCaseYesNo useCase);

        void requestYesNoAnswerRocket(GameUseCaseYesNo useCase);

        void showKilledPlayerDelay(GameUseCase useCase);

        void requestUserPlayerSelection(GameUseCaseYesNo useCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void useMedPack();

        void useRockedLauncher(long playerId);
    }
}
