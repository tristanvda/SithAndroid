package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import android.util.Pair;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

public class YesNoGameFlowPresenter extends Presenter<YesNoGameFlowPresenter.View> {
    private GameUseCaseYesNoId gameUseCaseYesNo;
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

    public void setGameUseCaseYesNo(GameUseCaseYesNoId gameUseCaseYesNo){
        this.gameUseCaseYesNo = gameUseCaseYesNo;
    }

    public void onAnswerClicked(boolean yesClicked) {
        gameUseCaseYesNo.onExecuteStep(flowDetails.getStep(), new Pair<>(yesClicked, 0L));
    }

    public interface View extends PresenterView {
        void disableYesButton();
    }
}
