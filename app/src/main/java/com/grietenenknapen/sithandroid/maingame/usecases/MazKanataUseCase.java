package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseId;


public class MazKanataUseCase extends GameUseCase<MazKanataUseCase.CallBack> implements UseCaseId {

    public MazKanataUseCase(CallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(final int step) {
        switch (step) {
            case 1:
                flowManagerListener.speak(R.raw.basis7_mazkanatawordtwakker,
                        R.string.basis7_maz_kanata_wordt_wakker, this);
                break;
            case 2:
                flowManagerListener.playMazKanataMusic();
                if (!active) {
                    flowManagerListener.skipStepDelay(delayShort);
                } else {
                    flowManagerListener.requestUserCardPeek(this);
                }
                break;
            case 3:
                flowManagerListener.stopPlayingMusic();
                flowManagerListener.speak(R.raw.basis8_mazkanatamagteruggaanslapen,
                        R.string.basis8_maz_kanata_mag_terug_gaan_slapen, this);
                break;
        }
    }

    @Override
    public void onExecuteStep(final int step, final long stepData) {
        //Might want to use the id here later
        this.handleExecuteStep();
    }

    @Override
    public boolean finishUseCase(final int step) {
        return super.finishUseCase(step) || step > 3;
    }

    public interface CallBack extends UseCaseCallBack {

        void requestUserCardPeek(GameUseCase gameUseCase);

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

        void playMazKanataMusic();

        void stopPlayingMusic();

    }
}
