package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;

public class HanSoloUseCase extends GameUseCaseCard<HanSoloUseCase.CallBack> {
    private static final long DELAY_SHORT = 3 * 1000;

    public HanSoloUseCase(CallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(final int step) {

        switch (step) {
            case 1:
                flowManagerListener.speak(R.raw.basis2_hansolowordtwakker,
                        R.string.basis2_han_solo_wordt_wakker, this);
                break;
            case 2:
                flowManagerListener.playHanSoloMusic();
                if (!active) {
                    flowManagerListener.skipStepDelay(DELAY_SHORT);
                } else {
                    flowManagerListener.requestUserCardSelection(this);
                }
                break;
            case 3:
                flowManagerListener.stopPlayingMusic();
                flowManagerListener.speak(R.raw.basis3_hansolomagteruggaanlslapen,
                        R.string.basis3_han_solo_mag_terug_gaan_slapen, this);
                break;
        }
    }

    @Override
    protected void onUseCaseExecuteStep(final int step, final SithCard stepData) {
        if (step == 2 && stepData != null) {
            flowManagerListener.switchHanSoloUserCard(stepData);
        }
    }

    @Override
    public boolean finishUseCase(final int step) {
        return super.finishUseCase(step) || (round > 1 && step >= 1) || step > 3;
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserCardSelection(GameUseCaseCard useCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void switchHanSoloUserCard(SithCard sithCard);

        void playHanSoloMusic();

        void stopPlayingMusic();
    }
}
