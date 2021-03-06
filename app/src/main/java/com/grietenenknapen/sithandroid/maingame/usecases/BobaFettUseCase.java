package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseYesNo;

public class BobaFettUseCase extends GameUseCase<BobaFettUseCase.CallBack> implements UseCaseYesNo, UseCaseId {

    private boolean rocketAlreadySelected = false;

    public BobaFettUseCase(CallBack flowManagerListener,
                           boolean active,
                           boolean skip,
                           boolean rockedAlreadySelected) {
        super(flowManagerListener, active, skip);
        this.rocketAlreadySelected = rockedAlreadySelected;
    }

    @Override
    public void onPrepareStep(final int step) {
        switch (step) {
            case 1:
                flowManagerListener.speak(R.raw.basis11_bobafettwordtwakker, R.string.basis11_boba_fett_wordt_wakker, this);
                break;
            case 2:
                flowManagerListener.playBobaFettMusic();
                if (!active) {
                    flowManagerListener.skipStepDelay(delayShort);
                } else {
                    flowManagerListener.showKilledPlayerMedPackYesNo(this);
                }
                break;
            case 3:
                flowManagerListener.stopPlayingMusic();
                flowManagerListener.speak(R.raw.basis12_bobafettrocketlauncher, R.string.basis12_boba_fett_rocketlauncher, this);
                break;
            case 4:
                flowManagerListener.playBobaFettMusic();
                if (!active) {
                    flowManagerListener.skipStepDelay(delayLong);
                } else {
                    flowManagerListener.requestYesNoAnswerRocket(this);
                }
                break;
            case 5:
                if (!active) {
                    flowManagerListener.skipStep();
                    break;
                }
                if (rocketAlreadySelected) {
                    flowManagerListener.requestUserPlayerRocketSelection(this);
                } else {
                    flowManagerListener.stopPlayingMusic();
                    flowManagerListener.speak(R.raw.basis13_bobafettgaatterugslapen, R.string.basis13_boba_fett_gaat_terug_slapen, this);
                }
                break;
            case 6:
                flowManagerListener.stopPlayingMusic();
                flowManagerListener.speak(R.raw.basis13_bobafettgaatterugslapen, R.string.basis13_boba_fett_gaat_terug_slapen, this);
                break;
        }
    }

    @Override
    public boolean finishUseCase(final int step) {
        return super.finishUseCase(step) || (step == 6 && !rocketAlreadySelected && active) || step > 6;
    }

    @Override
    public void onExecuteStep(int step, boolean stepData) {
        if (step == 2 && stepData) {
            flowManagerListener.useMedPack();
        } else if (step == 4 && stepData) {
            rocketAlreadySelected = true;
            flowManagerListener.setRockedAlreadySelected(true);
        }
        this.handleExecuteStep();
    }

    @Override
    public void onExecuteStep(final int step, final long stepData) {
        flowManagerListener.useRockedLauncher(stepData);
        this.handleExecuteStep();
    }

    public interface CallBack extends UseCaseCallBack {

        void requestYesNoAnswerRocket(UseCaseYesNo useCase);

        void showKilledPlayerMedPackYesNo(UseCaseYesNo useCase);

        void requestUserPlayerRocketSelection(UseCaseId useCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void useMedPack();

        void useRockedLauncher(long playerId);

        void setRockedAlreadySelected(boolean rockedAlreadySelected);

        void playBobaFettMusic();

        void stopPlayingMusic();
    }
}
