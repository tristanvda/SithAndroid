package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

public class DelayGameFlowPresenter extends Presenter<DelayGameFlowPresenter.View> {
    private long delay;
    private final FlowDetails flowDetails;
    private UseCase gameUseCase;

    public DelayGameFlowPresenter(final FlowDetails flowDetails, final long delay) {
        this.delay = delay;
        this.flowDetails = flowDetails;
    }

    @Override
    protected void onViewBound() {
        getView().updateTimerText(String.valueOf(delay));
        getView().startTimer(delay);
    }

    public void setGameUseCase(UseCase gameUseCase) {
        this.gameUseCase = gameUseCase;
    }

    public void updateTimer(int delay) {
        this.delay = delay;
        getView().updateTimerText(String.valueOf(delay));
    }

    public void onTimerStop() {
        getView().updateTimerText("0");
        gameUseCase.onExecuteStep(flowDetails.getStep());
    }

    public interface View extends PresenterView {
        void updateTimerText(String delay);

        void startTimer(long delay);
    }
}
