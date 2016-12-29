package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import android.util.Pair;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;


public class ShowPlayerYesNoGameFlowPresenter extends Presenter<ShowPlayerYesNoGameFlowPresenter.View> {
    private final FlowDetails flowDetails;
    private final ActivePlayer activePlayer;
    private GameUseCaseYesNoId gameUseCase;
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

    public void setGameUseCaseYesNo(GameUseCaseYesNoId gameUseCaseYesNo) {
        this.gameUseCase = gameUseCaseYesNo;
    }

    public void onAnswerClicked(boolean yesClicked) {
        gameUseCase.onExecuteStep(flowDetails.getStep(), new Pair<>(yesClicked, 0L));
    }

    public interface View extends PresenterView {
        void displayPlayer(ActivePlayer activePlayer);

        void disableYesButton();

    }
}
