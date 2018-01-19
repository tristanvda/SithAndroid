package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.List;

public class UserCardPeekGameFlowPresenter extends Presenter<UserCardPeekGameFlowPresenter.View> {
    private UseCaseId gameUseCase;
    private final FlowDetails flowDetails;
    private final List<ActivePlayer> activePlayers;
    private ActivePlayer selectedPlayer;

    public UserCardPeekGameFlowPresenter(FlowDetails flowDetails, List<ActivePlayer> activePlayers) {
        this.flowDetails = flowDetails;
        this.activePlayers = activePlayers;
    }

    @Override
    protected void onViewBound() {
        getView().showActivePlayers(activePlayers);
        if (selectedPlayer != null) {
            getView().showSelectedPlayer(selectedPlayer);
            getView().enableNext();
            getView().disableList();
        } else {
            getView().disableNext();
        }
    }

    public void setGameUseCase(UseCaseId gameUseCase){
        this.gameUseCase = gameUseCase;
    }

    public void activePlayerChosen(ActivePlayer selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
        getView().disableList();
        getView().enableNext();
    }

    public void onNextClicked() {
        gameUseCase.onExecuteStep(flowDetails.getStep(), selectedPlayer.getPlayerId());
    }


    public interface View extends PresenterView {
        void showActivePlayers(List<ActivePlayer> activePlayers);

        void showSelectedPlayer(ActivePlayer activePlayer);

        void disableList();

        void enableNext();

        void disableNext();
    }
}
