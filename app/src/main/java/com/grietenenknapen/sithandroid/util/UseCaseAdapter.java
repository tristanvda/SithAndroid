package com.grietenenknapen.sithandroid.util;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;

public class UseCaseAdapter implements UseCase {

    @Override
    public void onSetupUseCase(final int round) {

    }

    @Override
    public void onPrepareStep(final int step) {

    }

    @Override
    public void onExecuteStep(final int step) {

    }

    @Override
    public boolean finishUseCase(final int step) {
        return false;
    }
}
