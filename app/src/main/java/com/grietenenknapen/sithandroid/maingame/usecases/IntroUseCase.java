package com.grietenenknapen.sithandroid.maingame.usecases;


import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;

public class IntroUseCase extends GameUseCaseCard<IntroUseCase.CallBack> {

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

    @Override
    protected void onUseCaseExecuteStep(int step, SithCard stepData) {

    }

    public interface CallBack extends UseCaseCallBack {


        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

    }
}
