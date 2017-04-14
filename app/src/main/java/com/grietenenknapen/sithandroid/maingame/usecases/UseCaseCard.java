package com.grietenenknapen.sithandroid.maingame.usecases;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;

public interface UseCaseCard extends UseCase {

    /**
     * Same function as onExecuteStep, but provides an SithCard to the Use Case
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step     the current step of the Use Case
     * @param stepData the step data as SithCard
     */
    void onExecuteStep(int step, SithCard stepData);
}
