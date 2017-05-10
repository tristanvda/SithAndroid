package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;

public interface UseCaseYesNo extends UseCase {

    /**
     * Same function as onExecuteStep, but provides a flag to the Use Case
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step     the current step of the Use Case
     * @param stepData the step data as a boolean flag
     */
    void onExecuteStep(int step, boolean stepData);
}