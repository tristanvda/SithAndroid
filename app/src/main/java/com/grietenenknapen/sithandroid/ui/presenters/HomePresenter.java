package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.application.Settings;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

public class HomePresenter extends Presenter<HomePresenter.View> {

    private boolean menuResumeSelected;
    private boolean menuStartSelected;

    @Override
    protected void onViewBound() {
        getView().updateStartResumeScreen();
    }

    public void onMenuStartClicked() {
        getView().deleteSavedGame();
        getView().goToGameFlowScreen();
    }

    public void onMenuResumeClicked() {
        getView().goToGameFlowScreen();
    }

    public void handleStartOrResumeGameClicked() {
        if (menuStartSelected) {
            onMenuStartClicked();
            menuStartSelected = false;
        } else if (menuResumeSelected) {
            onMenuResumeClicked();
            menuResumeSelected = false;
        }
    }

    public void setMenuResumeSelected(boolean menuResumeSelected) {
        this.menuResumeSelected = menuResumeSelected;
    }

    public void setMenuStartSelected(boolean menuStartSelected) {
        this.menuStartSelected = menuStartSelected;
    }

    public void onPlayersClicked() {
        getView().goToPlayersScreen();
    }

    public interface View extends PresenterView {
        void goToPlayersScreen();

        void goToGameFlowScreen();

        void updateStartResumeScreen();

        void deleteSavedGame();
    }
}
