package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public final class RandomCommentUseCase extends GameUseCase<Void, RandomCommentUseCase.CallBack> {
    private final int resSoundId;
    private final int resStringId;

    public RandomCommentUseCase(final RandomCommentUseCase.CallBack flowManagerListener,
                                final boolean active,
                                final boolean skip,
                                final int resSoundId,
                                final int resStringId) {

        super(flowManagerListener, active, skip);
        this.resSoundId = resSoundId;
        this.resStringId = resStringId;
    }

    @Override
    public void onPrepareStep(final int step) {
        flowManagerListener.speak(resSoundId, resStringId, this);
    }

    @Override
    public boolean finishUseCase(final int step) {
        return step > 1;
    }

    @Override
    protected void onUseCaseExecuteStep(final int step, final Void stepData) {

    }

    public interface CallBack extends UseCaseCallBack {

        void speak(int soundResId, int stringResId, GameUseCase gameUseCase);

    }
}