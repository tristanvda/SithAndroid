package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

public class ShowPlayerYesNoGameFlowPresenter extends Presenter<ShowPlayerYesNoGameFlowPresenter.View> {
    private final FlowDetails flowDetails;
    private final ActivePlayer activePlayer;
    private UseCaseYesNo gameUseCase;
    private final boolean hideYes;

    public ShowPlayerYesNoGameFlowPresenter(FlowDetails flowDetails, ActivePlayer activePlayer, final boolean hideYes) {
        this.flowDetails = flowDetails;
        this.activePlayer = activePlayer;
        this.hideYes = hideYes;
    }

    @Override
    protected void onViewBound() {
        getView().displayPlayer(activePlayer);

        if (hideYes) {
            getView().disableYesButton();
        }
    }

    public void setGameUseCaseYesNo(UseCaseYesNo gameUseCaseYesNo) {
        this.gameUseCase = gameUseCaseYesNo;
    }

    public void onAnswerClicked(boolean yesClicked) {
        gameUseCase.onExecuteStep(flowDetails.getStep(), yesClicked);
    }

    public interface View extends PresenterView {
        void displayPlayer(ActivePlayer activePlayer);

        void disableYesButton();
    }
}
