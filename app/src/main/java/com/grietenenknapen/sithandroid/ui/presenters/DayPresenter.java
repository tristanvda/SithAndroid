package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.ArrayList;
import java.util.List;

public class DayPresenter extends Presenter<DayPresenter.View> {
    private final MainGame game;

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

    public interface View extends PresenterView {
        void goToNightStart();

        void goToKillPlayer(List<ActivePlayer> alivePlayers);

        void showRipPlayersList(List<ActivePlayer> killedPlayers);

        void goToGamePlayers(List<ActivePlayer> alivePlayers, List<ActivePlayer> killedPlayers);

        void hideRipPlayersList();
    }
}
