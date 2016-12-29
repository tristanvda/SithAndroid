package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.List;

public class GamePlayersPresenter extends Presenter<GamePlayersPresenter.View> {
    private final List<ActivePlayer> alivePlayers;
    private final List<ActivePlayer> deathPlayers;


    public GamePlayersPresenter(final List<ActivePlayer> alivePlayers, List<ActivePlayer> deathPlayers) {
        this.alivePlayers = alivePlayers;
        this.deathPlayers = deathPlayers;
    }

    @Override
    protected void onViewBound() {
        getView().displayGamePlayers(alivePlayers, deathPlayers);
    }

    public interface View extends PresenterView {
        void displayGamePlayers(List<ActivePlayer> alivePlayers, List<ActivePlayer> deathPlayers);
    }
}



