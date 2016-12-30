package com.grietenenknapen.sithandroid.game.usecase;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.util.MathUtils;

public abstract class GameUseCase<T, L extends UseCaseCallBack> implements UseCase<T> {
    private static final int NEXT_STEP_DELAY = 0;
    private static final int DELAY_LONG_MIN = 8;
    private static final int DELAY_LONG_MAX = 12;
    private static final int DELAY_SHORT_MIN = 6;
    private static final int DELAY_SHORT_MAX = 10;
    private static final int SECOND = 1000;

    protected L flowManagerListener;
    protected int round;
    protected boolean active;
    private boolean skip;
    protected long delayLong;
    protected long delayShort;

    public GameUseCase(L flowManagerListener,
                       boolean active,
                       boolean skip) {
        this.active = active;
        this.flowManagerListener = flowManagerListener;
        this.skip = skip;
        this.delayLong = MathUtils.generateRandomInteger(DELAY_LONG_MIN, DELAY_LONG_MAX) * SECOND;
        this.delayShort = MathUtils.generateRandomInteger(DELAY_SHORT_MIN, DELAY_SHORT_MAX) * SECOND;
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
