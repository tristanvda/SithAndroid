package com.grietenenknapen.sithandroid.game.usecase.type;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;

import java.util.List;

public interface UseCaseIds extends UseCase {

    /**
     * Same function as onExecuteStep, but provides a list of Id's to the Use Case
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step     the current step of the Use Case
     * @param stepData the step data as a pair of Id's
     */
    void onExecuteStep(int step, List<Long> stepData);
}
