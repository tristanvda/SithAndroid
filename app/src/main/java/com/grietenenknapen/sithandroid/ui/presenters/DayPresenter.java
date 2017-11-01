package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.ArrayList;
import java.util.List;

public class DayPresenter extends Presenter<DayPresenter.View> {
    private final MainGame game;
    private boolean p2pEnabled;
    private boolean isServerRunning;
    private boolean isServerLoading;

    public DayPresenter(MainGame game) {
        this.game = game;
    }

    @Override
    protected void onViewBound() {
        if (game.getDeathList() != null && game.getDeathList().size() > 0) {
            getView().showRipPlayersList(game.getDeathList());
        } else {
            getView().hideRipPlayersList();
        }
        handleServerButton();
    }

    public void setServerLoading(final boolean serverLoading) {
        this.isServerLoading = serverLoading;
        handleServerButton();
    }

    public void updateWifiServerState(final boolean p2pEnabled, final boolean isServerRunning) {
        this.p2pEnabled = p2pEnabled;
        this.isServerRunning = isServerRunning;
        handleServerButton();
    }

    public void onDayButtonStartClicked() {
        getView().goToNightStart();
    }

    public void onDayButtonKillClicked() {
        getView().goToKillPlayer(game.getAlivePlayers());
    }

    public void onDayButtonPlayersClicked() {
        getView().goToGamePlayers(game.getAlivePlayers(), game.getKilledPlayers());
    }

    public void onDayButtonServerClicked() {
        getView().initiateStartServer();
    }

    private void handleServerButton() {
        if (getView() == null) {
            return;
        }

        if (!p2pEnabled) {
            getView().setServerButtonEnabled(false);
            return;
        }

        getView().setServerButtonEnabled(true);
        if (isServerLoading) {
            getView().setServerButtonLoading();
        } else {
            getView().setServerButtonChecked(isServerRunning);
        }
    }

    public interface View extends PresenterView {

        void goToNightStart();

        void goToKillPlayer(List<ActivePlayer> alivePlayers);

        void showRipPlayersList(List<ActivePlayer> killedPlayers);

        void goToGamePlayers(List<ActivePlayer> alivePlayers, List<ActivePlayer> killedPlayers);

        void hideRipPlayersList();

        void initiateStartServer();

        void setServerButtonEnabled(boolean enabled);

        void setServerButtonLoading();

        void setServerButtonChecked(boolean checked);

    }
}
