package com.grietenenknapen.sithandroid.game.usecase.type;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;

public interface UseCaseId extends UseCase {

    /**
     * Same function as onExecuteStep, but provides an Id to the Use Case
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step     the current step of the Use Case
     * @param stepData the step data as Id
     */
    void onExecuteStep(int step, long stepData);
}
