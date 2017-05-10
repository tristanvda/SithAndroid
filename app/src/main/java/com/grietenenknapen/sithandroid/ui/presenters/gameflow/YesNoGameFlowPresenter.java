package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseYesNo;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

public class YesNoGameFlowPresenter extends Presenter<YesNoGameFlowPresenter.View> {
    private UseCaseYesNo gameUseCaseYesNo;
    private final FlowDetails flowDetails;
    private final boolean hideYes;

    public YesNoGameFlowPresenter(final FlowDetails flowDetails, final boolean hideYes) {
        this.flowDetails = flowDetails;
        this.hideYes = hideYes;
    }

    @Override
    protected void onViewBound() {
        if (hideYes) {
            getView().disableYesButton();
        }
    }

    public void setGameUseCaseYesNo(UseCaseYesNo gameUseCaseYesNo) {
        this.gameUseCaseYesNo = gameUseCaseYesNo;
    }

    public void onAnswerClicked(boolean yesClicked) {
        gameUseCaseYesNo.onExecuteStep(flowDetails.getStep(), yesClicked);
    }

    public interface View extends PresenterView {
        void disableYesButton();
    }
}
