package com.grietenenknapen.sithandroid.ui.fragments.gameflow;


import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;

public interface GameFlowFragment<T extends UseCase> {

    void setUseCase(T useCase);

    boolean isNewTask(FlowDetails flowDetails);
}
