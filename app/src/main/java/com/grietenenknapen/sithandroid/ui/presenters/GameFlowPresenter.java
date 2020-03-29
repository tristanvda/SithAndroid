package com.grietenenknapen.sithandroid.ui.presenters;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowManager;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowCallBack;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowManager;
import com.grietenenknapen.sithandroid.maingame.MainGameRandomFlowManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectReceiverListenerAdapter;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.helper.WifiPackageHelper;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseCard;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseId;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponsePairId;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.MainGameServerCallbackWrapper;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.WifiDirectGameServerManager;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;
import com.grietenenknapen.sithandroid.util.ResourceProvider;

import java.lang.annotation.Retention;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class GameFlowPresenter extends Presenter<GameFlowPresenter.View> implements MainGameFlowCallBack {
    @Retention(SOURCE)
    @IntDef({STATUS_SELECT_PLAYERS, STATUS_SHUFFLE, STATUS_GAME, STATUS_GAME_OVER, STATUS_KILL_PLAYERS, STATUS_GAME_PLAYERS})
    public @interface GameStatus {
    }

    public static final int STATUS_SELECT_PLAYERS = 1;
    public static final int STATUS_SHUFFLE = 2;
    public static final int STATUS_GAME = 3;
    public static final int STATUS_GAME_OVER = 4;
    public static final int STATUS_KILL_PLAYERS = 5;
    public static final int STATUS_GAME_PLAYERS = 6;

    private static final int MIN_PLAYERS = 5;

    private final PlayerService playerService;
    private final ResourceProvider resourceProvider;
    @GameStatus
    private int status;
    private List<Player> players;
    private List<SithCard> sithCards;
    private MainGame game;
    private GameFlowManager<MainGameFlowCallBack> gameFlowManager;
    private List<Pair<Integer, Integer>> randomResourceList;
    private Queue<Integer> speakStack;

    @Nullable
    private WifiDirectGameServerManager wifiDirectGameServerManager;
    private boolean playRandomComments;
    private boolean wifiP2PEnabled = false;

    public GameFlowPresenter(final PlayerService playerService,
                             final SithCardService sithCardService,
                             final MainGame mainGame,
                             final WifiDirectGameServerManager wifiDirectGameServerManager,
                             final ResourceProvider resourceProvider) {

        this.playerService = playerService;
        this.resourceProvider = resourceProvider;
        this.wifiDirectGameServerManager = wifiDirectGameServerManager;
        init(sithCardService, mainGame, false);
    }

    public GameFlowPresenter(final PlayerService playerService,
                             final SithCardService sithCardService,
                             final MainGame mainGame,
                             final WifiDirectGameServerManager wifiDirectGameServerManager,
                             final ResourceProvider resourceProvider,
                             final List<Pair<Integer, Integer>> randomResourceList) {

        this.playerService = playerService;
        this.resourceProvider = resourceProvider;
        this.wifiDirectGameServerManager = wifiDirectGameServerManager;
        this.randomResourceList = randomResourceList;
        init(sithCardService, mainGame, true);
    }

    private void init(final SithCardService sithCardService,
                      final MainGame mainGame,
                      final boolean playRandomComments) {

        this.playRandomComments = playRandomComments;

        if (wifiDirectGameServerManager != null) {
            wifiDirectGameServerManager.setClientResponseListener(new WifiDirectGameServerManager.WifiGameServerListener() {
                @Override
                public void onClientResponse(final WifiPackage wifiPackage) {
                    handleNewWifiPackage(wifiPackage);
                }

                @Override
                public void onServerError(final int messageRes) {
                    if (getView() != null) {
                        getView().showError(messageRes);
                    }
                }
            });
        }

        if (mainGame != null) {
            game = mainGame;
            startOrResumeGame();
        } else {
            showSelectPlayersScreen();
        }

        sithCardService.retrieveAllSithCards(new ServiceCallBack<List<SithCard>>() {
            @Override
            public void onSuccess(List<SithCard> returnData) {
                sithCards = returnData;
                if (status == STATUS_GAME) {
                    startOrResumeGame();
                }
            }

            @Override
            public void onError(int messageResId) {

            }
        });
    }

    public void setStatus(@GameStatus int status) {
        this.status = status;
    }

    @Override
    protected void onViewBound() {
        if (wifiDirectGameServerManager != null) {
            getView().updateWifiServerState(wifiP2PEnabled, wifiDirectGameServerManager.isServerRunning());
        } else {
            getView().hideWifiServerState();
        }

        onSpeakDone();
    }

    @Override
    protected void onPresenterDestroy() {
        super.onPresenterDestroy();
        if (wifiDirectGameServerManager != null) {
            wifiDirectGameServerManager.stopAndDestroyHostingServer();
        }
    }

    private void startOrResumeGame() {
        status = STATUS_GAME;

        if (sithCards == null) {
            return;
        }
        if (wifiDirectGameServerManager != null) {
            wifiDirectGameServerManager.setGame(game);
        }
        if (gameFlowManager == null) {
            if (playRandomComments) {
                gameFlowManager = new MainGameRandomFlowManager(game, sithCards, randomResourceList);
            } else {
                gameFlowManager = new MainGameFlowManager(game, sithCards);
            }
        }
        if (!gameFlowManager.isAttached()) {
            if (wifiDirectGameServerManager == null) {
                gameFlowManager.attach(this);
            } else {
                gameFlowManager.attach(new MainGameServerCallbackWrapper(this, game, wifiDirectGameServerManager, resourceProvider, gameFlowManager));
            }
        }
    }

    private void handleNewWifiPackage(WifiPackage wifiPackage) {
        if (gameFlowManager != null && gameFlowManager.isAttached() && wifiPackage != null) {
            if (!(wifiPackage instanceof WifiFlowPackage)) {
                return;
            }

            UseCase activeUseCase = gameFlowManager.getActiveGameUseCase();
            FlowDetails currentFlowDetails = game.getFlowDetails();
            Class<? extends UseCase> useCaseClass = WifiPackageHelper.getUseCaseFromPackage(wifiPackage);

            if (gameFlowManager.isStarted() && useCaseClass != null
                    && currentFlowDetails.equals(((WifiFlowPackage) wifiPackage).getFlowDetails())) {

                if (useCaseClass.equals(UseCaseId.class) && activeUseCase instanceof UseCaseId) {
                    ((UseCaseId) activeUseCase).onExecuteStep(currentFlowDetails.getStep(), ((WifiFlowResponseId) wifiPackage).getId());
                } else if (useCaseClass.equals(UseCasePairId.class) && activeUseCase instanceof UseCasePairId) {
                    ((UseCasePairId) activeUseCase).onExecuteStep(currentFlowDetails.getStep(), ((WifiFlowResponsePairId) wifiPackage).getPairId());
                } else if (useCaseClass.equals(UseCaseCard.class) && activeUseCase instanceof UseCaseCard) {
                    ((UseCaseCard) activeUseCase).onExecuteStep(currentFlowDetails.getStep(), ((WifiFlowResponseCard) wifiPackage).getCard());
                } else if (useCaseClass.equals(UseCaseYesNo.class) && activeUseCase instanceof UseCaseYesNo) {
                    ((UseCaseYesNo) activeUseCase).onExecuteStep(currentFlowDetails.getStep(), ((WifiFlowResponseYesNo) wifiPackage).isYes());
                }
            }
        }
    }

    private void showSelectPlayersScreen() {
        if (players == null) {
            playerService.retrieveAllPlayers(new ServiceCallBack<List<Player>>() {
                @Override
                public void onSuccess(List<Player> returnData) {
                    players = returnData;
                    if (getView() != null) {
                        getView().showSelectPlayersScreen(players);
                    }
                }

                @Override
                public void onError(int messageResId) {
                }
            });
        } else {
            getView().showSelectPlayersScreen(players);
        }
    }

    public void onPlayerSelected(final List<Player> players) {
        if (status == STATUS_KILL_PLAYERS) {
            game.killPlayers(players);
            for (Player player : players) {
                game.addToDeathList(player.getId());
            }
            if (game.isGameOver()) {
                getView().deleteSavedGame();
                goToGameOverFragment();
            } else {
                getView().popBackStack();
            }
        } else {
            if (players.size() >= MIN_PLAYERS) {
                status = STATUS_SHUFFLE;
                getView().goToShuffleScreen(players);
            } else {
                getView().showError(R.string.player_limit_error);
            }

        }
    }

    public void onCardsShuffled(final List<ActivePlayer> activePlayers) {
        status = STATUS_GAME;
        game = new MainGame(activePlayers);
        for (ActivePlayer activePlayer : activePlayers) {
            if (!TextUtils.isEmpty(activePlayer.getTelephoneNumber())) {
                getView().sendSMS(activePlayer.getTelephoneNumber(), activePlayer.getSithCard().getName());
            }
        }
        startOrResumeGame();
    }

    public void onServerButtonClicked() {
        if (wifiDirectGameServerManager != null && wifiDirectGameServerManager.isServerRunning()) {
            wifiDirectGameServerManager.stopAndDestroyHostingServer();
            getView().updateWifiServerState(wifiP2PEnabled, false);
            return;
        }
        getView().startWifiDirectLoading();
        wifiDirectGameServerManager.createAndStartHostingServer(new WifiDirectGameServerManager.WifiServerStartListener() {
            @Override
            public void serverStarted() {
                if (getView() != null) {
                    getView().showMessage(R.string.server_started);
                    getView().stopWifiDirectLoading();
                    getView().updateWifiServerState(wifiP2PEnabled, true);
                }
            }

            @Override
            public void serverStartFailed(final String message) {
                if (getView() != null) {
                    getView().showError(message);
                    getView().stopWifiDirectLoading();
                    getView().updateWifiServerState(wifiP2PEnabled, false);
                }
            }

            @Override
            public void serverStartFailed(final int messageRes) {
                if (getView() != null) {
                    getView().showError(messageRes);
                    getView().stopWifiDirectLoading();
                    getView().updateWifiServerState(wifiP2PEnabled, false);
                }
            }
        });
    }

    private WifiDirectReceiverListenerAdapter wifiDirectReceiverListener = new WifiDirectReceiverListenerAdapter() {
        @Override
        public void p2pStateEnabled() {
            wifiP2PEnabled = true;
            if (getView() != null) {
                getView().updateWifiServerState(true, wifiDirectGameServerManager.isServerRunning());
            }
        }

        @Override
        public void p2pStateDisabled() {
            wifiP2PEnabled = false;
            if (getView() != null) {
                getView().updateWifiServerState(false, wifiDirectGameServerManager.isServerRunning());
            }
        }
    };

    public boolean isWifiP2PEnabled() {
        return wifiP2PEnabled;
    }

    public boolean isServerRunning() {
        return wifiDirectGameServerManager != null && wifiDirectGameServerManager.isServerRunning();
    }

    public void setWifiDirectBroadcastReceiver(WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver) {
        if (wifiDirectGameServerManager != null) {
            wifiDirectBroadcastReceiver.addWifiDirectReceiver(wifiDirectReceiverListener);
            wifiDirectGameServerManager.setBroadcastReceiver(wifiDirectBroadcastReceiver);
        }
    }

    public boolean onBackPressed() {
        if (status == STATUS_GAME || status == STATUS_GAME_OVER) {
            getView().showExitDialog();
            return true;
        }

        return false;
    }

    public void quitGame() {
        if (gameFlowManager != null) {
            gameFlowManager.detach();
        }
        getView().closeScreen();
    }

    public void startNight() {
        gameFlowManager.startNewRound();
    }

    public void killPlayerSelected(final List<ActivePlayer> activePlayers) {
        getView().showKillPlayersScreen(activePlayers);
    }

    public GameUseCase getCurrentUseCase() {
        return gameFlowManager.getActiveGameUseCase();
    }

    @Override
    public void requestUserPairPlayerSelection(final List<ActivePlayer> activePlayers) {
        getView().goTotUserPairPlayerSelection(activePlayers, game.getFlowDetails());
    }

    @Override
    public void showDelay(final long delay) {
        getView().goToDelayScreen(delay, game.getFlowDetails());
    }

    @Override
    public void requestYesNoAnswer(final boolean disableYes, final int titleResId) {
        getView().goToYesNoScreen(disableYes, game.getFlowDetails(), titleResId);
    }

    @Override
    public void showPlayerYesNo(final ActivePlayer activePlayer, final boolean disableYes, final int titleResId) {
        getView().goToPlayerYesNoScreen(activePlayer, disableYes, titleResId, game.getFlowDetails());
    }

    @Override
    public void requestUserPlayerSelection(final List<ActivePlayer> activePlayers) {
        getView().goToUserPlayerSelectionScreen(activePlayers, game.getFlowDetails());
    }

    @Override
    public void requestUsersPlayerSelection(final List<ActivePlayer> activePlayers, final int titleResId, final int min, final int max) {
        getView().goToUsersPlayerSelectionScreen(activePlayers, titleResId, min, max, game.getFlowDetails());
    }

    @Override
    public void requestUserCardSelection(final List<SithCard> availableSithCards) {
        getView().goToUserCardSelectionScreen(availableSithCards, game.getFlowDetails());
    }

    @Override
    public void requestUserCardPeek(final List<ActivePlayer> players, final long delay) {
        getView().goToUserCardPeekScreen(players, delay, game.getFlowDetails());
    }

    @Override
    public void speak(final int soundResId, final int stringResId) {
        getView().goToSpeakScreen(soundResId, stringResId, game.getFlowDetails());
    }

    @Override
    public void stackAndSpeak(final int soundResId) {
        if (speakStack == null) {
            speakStack = new LinkedList<>();
            speakStack.add(soundResId);
            handleStackSpeak();
        } else {
            speakStack.add(soundResId);
        }
    }

    @Override
    public void stackAndSpeak(final String soundResSId) {
        if (getView() != null) {
            stackAndSpeak(getView().getRawResourceId(soundResSId));
        }
    }

    @Override
    public void saveGame(final MainGame game) {
        if (getView() != null) {
            getView().saveGame(game);
        }
    }

    @Override
    public void deleteSavedGame() {
        if (getView() != null) {
            getView().deleteSavedGame();
        }
    }

    @Override
    public void playMusic(int musicType) {
        if (getView() != null) {
            getView().playMusic(musicType);
        }
    }

    @Override
    public void stopPlayingMusic() {
        if (getView() != null) {
            getView().stopPlayingMusic();
        }
    }

    @Override
    public void sendSMS(String number, final int stringResId) {
        getView().sendSMS(number, stringResId);
    }

    @Override
    public void sendSMS(final String number, final int stringResId, final Object... formatArgs) {
        getView().sendSMS(number, stringResId, formatArgs);
    }

    public void onSpeakDone() {
        if (speakStack == null || speakStack.isEmpty()) {
            speakStack = null;
        } else {
            handleStackSpeak();
        }
    }

    private void handleStackSpeak() {
        if (speakStack != null) {
            final int resId = speakStack.poll();
            if (getView() != null) {
                getView().speak(resId);
            }
        }
    }

    @Override
    public void roundStatusChanged(final boolean started, final boolean gameOver) {
        if (gameOver) {
            goToGameOverFragment();
            return;
        }
        if (!started) {
            getView().goToDayScreen(game);
        }
    }

    private void goToGameOverFragment() {
        if (game.getWinningTeam() == GameTeam.GALEN_ERSO) {
            getView().showGameOver(game.findPlayersByTeam(GameTeam.GALEN_ERSO), game.getWinningTeam());
        } else {
            getView().showGameOver(game.getAlivePlayers(), game.getWinningTeam());
        }
    }

    public interface View extends PresenterView {

        void goToDelayScreen(long delay, FlowDetails flowDetails);

        void goToYesNoScreen(boolean disableYes, FlowDetails flowDetails, int titleResId);

        void goToPlayerYesNoScreen(ActivePlayer activePlayer, boolean disableYes, int titleResId, FlowDetails flowDetails);

        void goToUserPlayerSelectionScreen(List<ActivePlayer> activePlayers, FlowDetails flowDetails);

        void goToUsersPlayerSelectionScreen(List<ActivePlayer> activePlayers, int titleResId, int min, int max, FlowDetails flowDetails);

        void goToUserCardSelectionScreen(List<SithCard> availableSithCards, FlowDetails flowDetails);

        void goToUserCardPeekScreen(List<ActivePlayer> players, long delay, FlowDetails flowDetails);

        void goToSpeakScreen(int soundResId, int stringResId, FlowDetails flowDetails);

        void goTotUserPairPlayerSelection(List<ActivePlayer> players, FlowDetails flowDetails);

        void showSelectPlayersScreen(List<Player> players);

        void showKillPlayersScreen(List<ActivePlayer> activePlayers);

        void goToShuffleScreen(List<Player> players);

        void goToDayScreen(MainGame game);

        void speak(int soundResId);

        void showExitDialog();

        void showGameOver(List<ActivePlayer> players, @GameTeam.Team int winningTeam);

        void showError(int stringResId);

        void showMessage(int stringResId);

        void showError(String error);

        void startWifiDirectLoading();

        void stopWifiDirectLoading();

        void closeScreen();

        void popBackStack();

        void saveGame(MainGame mainGame);

        void deleteSavedGame();

        void playMusic(int musicType);

        void stopPlayingMusic();

        void sendSMS(String number, final int stringResId);

        void sendSMS(String number, String text);

        void sendSMS(final String number, final int stringResId, final Object... formatArgs);

        int getRawResourceId(String resourceName);

        void updateWifiServerState(boolean wifiP2PEnabled, boolean serverRunning);

        void hideWifiServerState();
    }
}
