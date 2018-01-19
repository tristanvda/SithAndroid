package com.grietenenknapen.sithandroid.ui.presenters;

import android.support.annotation.StringRes;
import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiDirectGameClientManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiP2pService;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiCommandRole;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiCommandSelectRole;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandMessage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandPeek;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandPlayerYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectPair;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectPlayer;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectSithCard;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseCard;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseId;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponsePairId;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiResponseRole;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;
import com.grietenenknapen.sithandroid.util.UseCaseAdapter;

import java.util.List;

public class GameClientFlowPresenter extends Presenter<GameClientFlowPresenter.View> {

    private static final int PEEK_DELAY = 3000;
    private static final int MESSAGE_DELAY = 5000;

    private final WifiDirectGameClientManager wifiDirectGameClientManager;
    private final WifiP2pService wifiP2pService;
    private FlowDetails currentFlowDetails = null;
    private ActivePlayer playerRole;

    public GameClientFlowPresenter(final WifiDirectGameClientManager wifiDirectGameClientManager,
                                   final WifiP2pService wifiP2pService) {

        this.wifiDirectGameClientManager = wifiDirectGameClientManager;
        this.wifiP2pService = wifiP2pService;
    }

    private void handleCardPeekCommand(WifiFlowCommandPeek peek) {
        getView().goToUserCardPeekScreen(peek.getActivePlayers(), PEEK_DELAY, peek.getFlowDetails());
    }

    private void handlePlayerYesNoCommand(WifiFlowCommandPlayerYesNo yesNo) {
        getView().goToPlayerYesNoScreen(yesNo.getActivePlayer(), yesNo.isHideYes(), yesNo.getMessage(), yesNo.getFlowDetails());
    }

    private void handleYesNoCommand(WifiFlowCommandYesNo yesNo) {
        getView().goToYesNoScreen(yesNo.isHideYes(), yesNo.getFlowDetails(), yesNo.getMessage());
    }

    private void handleSelectPairCommand(WifiFlowCommandSelectPair selectPair) {
        getView().goTotUserPairPlayerSelection(selectPair.getActivePlayers(), selectPair.getFlowDetails());
    }

    private void handleSelectPlayerCommand(WifiFlowCommandSelectPlayer selectPlayer) {
        getView().goToUserPlayerSelectionScreen(selectPlayer.getActivePlayers(), selectPlayer.getFlowDetails());
    }

    private void handleSelectSithCardCommand(WifiFlowCommandSelectSithCard selectSithCard) {
        getView().goToUserCardSelectionScreen(selectSithCard.getSithCards(), selectSithCard.getFlowDetails());
    }

    private void handleCommandMessage(WifiFlowCommandMessage message) {
        switch (message.getResponseCode()) {
            case WifiFlowCommandMessage.ResponseType.CODE_SUCCESS:
                if (getView() != null) {
                    getView().showMessage(message.getMessage(), MESSAGE_DELAY);
                }
                break;
            case WifiFlowCommandMessage.ResponseType.CODE_FAIL:
                if (getView() != null) {
                    getView().showError(message.getMessage(), 0);
                }
                break;
        }
    }

    @Override
    protected void onViewBound() {
        this.wifiDirectGameClientManager.setServerResponseListener(new WifiDirectGameClientManager.WifiServerClientListener() {

            @Override
            public void onServerResponse(final WifiPackage wifiPackage) {
                if (getView() != null) {
                    if (wifiPackage instanceof WifiFlowPackage) {
                        currentFlowDetails = ((WifiFlowPackage) wifiPackage).getFlowDetails();
                        switch (wifiPackage.getPackageType()) {
                            case WifiPackage.PackageType.COMMAND_TYPE_CARD_PEEK:
                                handleCardPeekCommand((WifiFlowCommandPeek) wifiPackage);
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_REQUEST_YES_NO:
                                handleYesNoCommand((WifiFlowCommandYesNo) wifiPackage);
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_SELECT_PAIR:
                                handleSelectPairCommand((WifiFlowCommandSelectPair) wifiPackage);
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_SELECT_PLAYER:
                                handleSelectPlayerCommand((WifiFlowCommandSelectPlayer) wifiPackage);
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_SELECT_SITH_CARD:
                                handleSelectSithCardCommand((WifiFlowCommandSelectSithCard) wifiPackage);
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_SHOW_PLAYER_YES_NO:
                                handlePlayerYesNoCommand((WifiFlowCommandPlayerYesNo) wifiPackage);
                                break;
                        }
                    } else {
                        switch (wifiPackage.getPackageType()) {
                            case WifiPackage.PackageType.COMMAND_TYPE_SELECT_ROLE:
                                getView().goToRequestRoleScreen(((WifiCommandSelectRole) wifiPackage).getActivePlayers());
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_ROLE:
                                playerRole = ((WifiCommandRole) wifiPackage).getActivePlayer();
                                showUserRoleScreen();
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_GAME_OVER:
                                getView().showMessage(R.string.game_over, 0);
                                break;
                            case WifiPackage.PackageType.COMMAND_TYPE_MESSAGE:
                                handleCommandMessage((WifiFlowCommandMessage) wifiPackage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onServerError(@StringRes final int messageRes) {
                if (getView() != null) {
                    getView().showError(messageRes, 0);
                }
            }
        });

        startConnectionIfNotRunning();
    }

    @Override
    protected void onViewUnBound() {
        this.wifiDirectGameClientManager.setServerResponseListener(null);
    }

    @Override
    protected void onPresenterDestroy() {
        wifiDirectGameClientManager.stopAndDestroyClientToServerConnection();
    }

    public void startConnectionIfNotRunning() {

        if (wifiDirectGameClientManager.isConnectedToServer()) {
            return;
        }

        if (getView() != null) {
            getView().showLoadingScreen();
        }

        this.wifiDirectGameClientManager.connectToWifiP2pServer(wifiP2pService, new WifiDirectGameClientManager.OnConnectToServerListener() {

            @Override
            public void onConnectedToServer() {
                //TODO: maybe do something when server is connected
            }

            @Override
            public void onConnectionError(@StringRes final int messageRes) {
                if (getView() != null) {
                    getView().showError(messageRes, 0);
                }
            }
        });
    }

    public void setWifiDirectBroadcastReceiver(WifiBroadcastReceiver wifiBroadcastReceiver) {
        wifiDirectGameClientManager.setBroadcastReceiver(wifiBroadcastReceiver);
    }

    public void onRoleSelected(Player player) {
        getView().showLoadingScreen();
        wifiDirectGameClientManager.sendWifiFlowPackageToPlayer(new WifiResponseRole(player.getId()));
    }

    public UseCase getResponseUseCase() {
        return responseUseCase;
    }

    private void showUserRoleScreen() {
        if (getView() != null && playerRole != null) {
            if (playerRole.isAlive()) {
                getView().showUserRoleScreen(playerRole);
            } else {
                getView().showMessage(R.string.user_killed, 0);
            }
        }
    }

    private ResponseUseCase responseUseCase = new ResponseUseCase();

    private final class ResponseUseCase extends UseCaseAdapter implements UseCaseId, UseCasePairId, UseCaseYesNo, UseCaseCard {

        @Override
        public void onExecuteStep(final int step) {
            //Nothing to do here yet, but might happen in the future
            showUserRoleScreen();
        }

        @Override
        public void onExecuteStep(final int step, final long stepData) {
            wifiDirectGameClientManager.sendWifiFlowPackageToPlayer(new WifiFlowResponseId(currentFlowDetails, stepData));
            showUserRoleScreen();
        }

        @Override
        public void onExecuteStep(final int step, final boolean stepData) {
            wifiDirectGameClientManager.sendWifiFlowPackageToPlayer(new WifiFlowResponseYesNo(currentFlowDetails, stepData));
            showUserRoleScreen();
        }

        @Override
        public void onExecuteStep(final int step, final Pair<Long, Long> stepData) {
            wifiDirectGameClientManager.sendWifiFlowPackageToPlayer(new WifiFlowResponsePairId(currentFlowDetails, stepData.first, stepData.second));
            showUserRoleScreen();
        }

        @Override
        public void onExecuteStep(final int step, final SithCard stepData) {
            wifiDirectGameClientManager.sendWifiFlowPackageToPlayer(new WifiFlowResponseCard(currentFlowDetails, stepData));
            showUserRoleScreen();
        }
    }

    public interface View extends PresenterView {

        void showError(@StringRes int errorResId, long delay);

        void showError(String error, long delay);

        void showMessage(String message, long delay);

        void showMessage(@StringRes int messageResId, long delay);

        void showLoadingScreen();

        void goToRequestRoleScreen(List<ActivePlayer> activePlayers);

        void goToYesNoScreen(boolean disableYes, FlowDetails flowDetails, String title);

        void goToPlayerYesNoScreen(ActivePlayer activePlayer, boolean disableYes, String title, FlowDetails flowDetails);

        void goToUserPlayerSelectionScreen(List<ActivePlayer> activePlayers, FlowDetails flowDetails);

        void goToUserCardSelectionScreen(List<SithCard> availableSithCards, FlowDetails flowDetails);

        void goToUserCardPeekScreen(List<ActivePlayer> players, long delay, FlowDetails flowDetails);

        void goTotUserPairPlayerSelection(List<ActivePlayer> players, FlowDetails flowDetails);

        void showUserRoleScreen(ActivePlayer activePlayer);

    }

}
