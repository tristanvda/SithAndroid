package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.List;

public class SithCardSelectGameFlowPresenter extends Presenter<SithCardSelectGameFlowPresenter.View> {
    private UseCaseCard gameUseCase;
    private final FlowDetails flowDetails;
    private final List<SithCard> sithCards;
    private SithCard selectedSithCard;

    public SithCardSelectGameFlowPresenter(FlowDetails flowDetails, List<SithCard> sithCards) {
        this.flowDetails = flowDetails;
        this.sithCards = sithCards;
    }

    @Override
    protected void onViewBound() {
        getView().showSithCards(sithCards);
        if (selectedSithCard != null) {
            getView().showSelectedSithCard(selectedSithCard);
            getView().enableNext();
        } else {
            getView().disableNext();
        }
    }

    public void setGameUseCase(UseCaseCard gameUseCase) {
        this.gameUseCase = gameUseCase;
    }

    public void sithCardChosen(SithCard selectedSithCard) {
        this.selectedSithCard = selectedSithCard;
        getView().enableNext();
    }

    public void onNextClicked() {
        gameUseCase.onExecuteStep(flowDetails.getStep(), selectedSithCard);
    }

    public interface View extends PresenterView {
        void showSithCards(List<SithCard> sithCards);

        void showSelectedSithCard(SithCard selectedCard);

        void enableNext();

        void disableNext();
    }
}
