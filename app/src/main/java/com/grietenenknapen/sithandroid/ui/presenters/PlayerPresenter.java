package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;
import com.grietenenknapen.sithandroid.util.InputValidations;

import java.util.List;

public class PlayerPresenter extends Presenter<PlayerPresenter.View> {
    private final PlayerService playerService;
    private List<Player> players;
    private String newName;
    private String newPhoneNumber;
    private Player shownPlayer;

    public PlayerPresenter(PlayerService playerService) {
        this.playerService = playerService;
        fetchPlayers(false);
    }

    @Override
    protected void onViewBound() {
        if (players != null) {
            getView().displayPlayers(players);
        }

        if (newName != null && newPhoneNumber != null) {
            getView().showNewPlayer(newName, newPhoneNumber, true);
        } else if (shownPlayer != null) {
            getView().showExistingPlayer(shownPlayer, true);
            getView().showDeleteButton();
        }

        validateNewValues();
    }

    private void fetchPlayers(final boolean scrollToBottom) {
        playerService.retrieveAllPlayers(new ServiceCallBack<List<Player>>() {
            @Override
            public void onSuccess(List<Player> returnData) {
                players = returnData;
                if (getView() != null) {
                    getView().displayPlayers(returnData);
                    if (scrollToBottom){
                        getView().scrollToBottom();
                    }
                }
            }

            @Override
            public void onError(int messageResId) {
                if (getView() != null) {
                    getView().displayError(messageResId);
                }
            }
        });
    }

    public void onListItemClicked(int position) {
        if (players == null || position >= players.size()) {
            newName = "";
            newPhoneNumber = "";
            getView().showNewPlayer(newName, newPhoneNumber, shownPlayer != null);
            shownPlayer = null;
            getView().hideDeleteButton();
        } else {
            final boolean doNotHide = shownPlayer != players.get(position);
            shownPlayer = players.get(position);
            getView().showExistingPlayer(shownPlayer, doNotHide);
            getView().showDeleteButton();
            newName = shownPlayer.getName();
            newPhoneNumber = shownPlayer.getTelephoneNumber();
        }
    }

    public void onDeleteClicked() {
        if (shownPlayer != null) {
            playerService.removePlayer(shownPlayer, new ServiceCallBack<Void>() {
                @Override
                public void onSuccess(Void returnData) {
                    if (getView() != null) {
                        fetchPlayers(false);
                    }

                }

                @Override
                public void onError(int messageResId) {
                    //TODO: show error
                }
            });
        }
        getView().hideBottomSheet();
    }

    public void onSaveClicked() {
        final ServiceCallBack<Long> serviceCallBack = new ServiceCallBack<Long>() {
            @Override
            public void onSuccess(Long returnData) {
                fetchPlayers(shownPlayer == null);
            }

            @Override
            public void onError(int messageResId) {
                //TODO: show error
            }
        };

        final Player player;
        if (shownPlayer != null) {
            shownPlayer.setName(newName);
            shownPlayer.setTelephoneNumber(newPhoneNumber);
            player = shownPlayer;
        } else {
            player = Player.newBuilder()
                    .name(newName)
                    .telephoneNumber(newPhoneNumber)
                    .build();
        }
        playerService.putPlayer(player, serviceCallBack);
        getView().hideBottomSheet();
    }

    public void cancelCurrentAction() {
        shownPlayer = null;
        newName = null;
        newPhoneNumber = null;
    }

    public void updateNewName(String name) {
        this.newName = name;
        validateNewValues();
    }

    public void updateNewPhoneNumber(String number) {
        this.newPhoneNumber = number;
        validateNewValues();
    }

    private void validateNewValues() {
        if (!InputValidations.validateNickName(newName)
                || !InputValidations.validatePhoneNumber(newPhoneNumber)) {

            getView().hideSaveButton();
            return;
        }

        if (shownPlayer != null
                && shownPlayer.getName().equals(newName)
                && shownPlayer.getTelephoneNumber().equals(newPhoneNumber)) {

            getView().hideSaveButton();
        } else {
            getView().showSaveButton();
        }

    }

    public interface View extends PresenterView {
        void displayPlayers(List<Player> players);

        void displayError(int messageResId);

        void showNewPlayer(String name, String number, boolean doNotHide);

        void showExistingPlayer(Player player, boolean doNotHide);

        void hideBottomSheet();

        void hideSaveButton();

        void hideDeleteButton();

        void showDeleteButton();

        void showSaveButton();

        void scrollToBottom();

    }
}
