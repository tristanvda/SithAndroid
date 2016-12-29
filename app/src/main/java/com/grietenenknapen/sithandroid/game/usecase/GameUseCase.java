package com.grietenenknapen.sithandroid.game.usecase;


import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;

public abstract class GameUseCase<T, L extends UseCaseCallBack> implements UseCase<T> {
    private static final long NEXT_STEP_DELAY = 0;

    protected L flowManagerListener;
    protected int round;
    protected boolean active;
    private boolean skip;

    public GameUseCase(L flowManagerListener,
                       boolean active,
                       boolean skip) {
        this.active = active;
        this.flowManagerListener = flowManagerListener;
        this.skip = skip;
    }

    @Override
    public void onSetupUseCase(final int round) {
        this.round = round;
    }

    @Override
    public void onExecuteStep(final int step, final T stepData) {
        this.onUseCaseExecuteStep(step, stepData);
        flowManagerListener.nextStep(NEXT_STEP_DELAY);
    }

    @Override
    public void onExecuteStep(final int step) {
        this.onUseCaseExecuteStep(step, null);
        flowManagerListener.nextStep(NEXT_STEP_DELAY);
    }

    @Override
    public boolean finishUseCase(int step) {
        return skip;
    }

    protected abstract void onUseCaseExecuteStep(int step, T stepData);

}
