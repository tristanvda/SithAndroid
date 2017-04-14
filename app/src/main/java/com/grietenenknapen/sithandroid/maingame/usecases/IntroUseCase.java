package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public class IntroUseCase extends GameUseCase<IntroUseCase.CallBack> {

    public IntroUseCase(CallBack flowManagerListener, boolean active, boolean skip) {
        super(flowManagerListener, active, skip);
    }

    @Override
    public void onPrepareStep(final int step) {
        flowManagerListener.speak(R.raw.basis1_intro,
                R.string.basis1_intro, this);
    }

    @Override
    public boolean finishUseCase(final int step) {
        return super.finishUseCase(step) || step > 1;
    }

    public interface CallBack extends UseCaseCallBack {

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

    }
}