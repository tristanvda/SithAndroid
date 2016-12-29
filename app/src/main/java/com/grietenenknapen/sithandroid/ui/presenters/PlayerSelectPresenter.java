package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectPresenter extends Presenter<PlayerSelectPresenter.View> {

    private final List<Player> players;
    private ArrayList<Player> selectedPlayers;
    private final int maxSelection;

    public PlayerSelectPresenter(final List<Player> players, final int maxSelection) {
        this.maxSelection = maxSelection;
        this.players = players;
    }

    @Override
    protected void onViewBound() {
        getView().showPlayers(players, maxSelection);
        if (selectedPlayers != null) {
            getView().showSelectedPlayers(players);
        }
    }

    public void onNextClicked(List<Integer> selectedPositions) {

        List<Player> selectedPlayers = new ArrayList<>();

        for (Integer integer : selectedPositions) {
            selectedPlayers.add(players.get(integer));
        }

        getView().playersSelected(selectedPlayers);
    }

    public interface View extends PresenterView {
        void showPlayers(List<Player> players, int maxSelection);

        void showSelectedPlayers(List<Player> selectedPlayers);

        void playersSelected(List<Player> players);
    }
}
