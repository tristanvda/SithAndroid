package com.grietenenknapen.sithandroid.ui.presenters;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.application.Settings;
import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowManager;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowCallBack;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowManager;
import com.grietenenknapen.sithandroid.maingame.MainGameRandomFlowManager;
import com.grietenenknapen.sithandroid.maingame.usecases.GameUseCaseCard;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.lang.annotation.Retention;
import java.util.ArrayList;
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
    private
    @GameStatus
    int status;
    private List<Player> players;
    private List<SithCard> sithCards;
    private MainGame game;
    private GameFlowManager<MainGameFlowCallBack> gameFlowManager;
    private List<Pair<Integer, Integer>> randomResourceList;
    private Queue<Integer> speakStack;
    private boolean playRandomComments;

    public GameFlowPresenter(final PlayerService playerService,
                             final SithCardService sithCardService,
                             final MainGame mainGame) {

        this.playerService = playerService;
        init(sithCardService, mainGame, false);
    }

    public GameFlowPresenter(final PlayerService playerService,
                             final SithCardService sithCardService,
                             final MainGame mainGame,
                             final List<Pair<Integer, Integer>> randomResourceList) {

        this.playerService = playerService;
        this.randomResourceList = randomResourceList;
        init(sithCardService, mainGame, true);
    }

    private void init(final SithCardService sithCardService,
                      final MainGame mainGame,
                      final boolean playRandomComments) {

        this.playRandomComments = playRandomComments;

        if (mainGame != null) {
            status = STATUS_GAME;
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
        onSpeakDone();
    }

    private void startOrResumeGame() {
        status = STATUS_GAME;

        if (sithCards == null) {
            return;
        }
        if (gameFlowManager == null) {
            if (playRandomComments) {
                gameFlowManager = new MainGameRandomFlowManager(game, sithCards, randomResourceList);
            } else {
                gameFlowManager = new MainGameFlowManager(game, sithCards);
            }
        }
        if (!gameFlowManager.isAttached()) {
            gameFlowManager.attach(this);
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
            if (game.checkGameOver()) {
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

    public void onCardsShuffled(List<ActivePlayer> activePlayers) {
        status = STATUS_GAME;
        game = new MainGame(activePlayers);
        for (ActivePlayer activePlayer : activePlayers) {
            if (!TextUtils.isEmpty(activePlayer.getTelephoneNumber())) {
                getView().sendSMS(activePlayer.getSithCard().getName(),
                        activePlayer.getTelephoneNumber());
            }
        }
        startOrResumeGame();
    }

    public boolean onBackPressed() {
        if (status == STATUS_GAME || status == STATUS_GAME_OVER) {
            getView().showExitDialog();
            return true;
        }

        return false;
    }

    public FlowDetails getFlowDetails() {
        if (game != null) {
            game.getFlowDetails();
        }
        return null;
    }

    public void quitGame() {
        //TODO: remove the cached game data
        getView().closeScreen();
    }

    public void startNight() {
        gameFlowManager.startNewRound();
    }

    public void killPlayerSelected(final List<ActivePlayer> activePlayers) {
        List<Player> players = getPlayersList(activePlayers);
        getView().showKillPlayersScreen(players);
    }

    @NonNull
    private List<Player> getPlayersList(final List<ActivePlayer> activePlayers) {
        List<Player> players = new ArrayList<>();
        for (ActivePlayer activePlayer : activePlayers) {
            Player player = Player.newBuilder()
                    .name(activePlayer.getName())
                    ._id(activePlayer.getPlayerId())
                    .telephoneNumber(activePlayer.getTelephoneNumber())
                    .build();
            players.add(player);

        }
        return players;
    }

    @Override
    public void requestUserPairPlayerSelection(final List<ActivePlayer> activePlayers, final GameUseCasePairId useCase) {
        List<Player> players = getPlayersList(activePlayers);
        getView().goTotUserPairPlayerSelection(players, useCase, game.getFlowDetails());
    }

    @Override
    public void showDelay(final long delay, final GameUseCase gameUseCase) {
        getView().goToDelayScreen(delay, gameUseCase, game.getFlowDetails());
    }

    @Override
    public void requestYesNoAnswer(final boolean disableYes, final GameUseCaseYesNoId useCase, final int titleResId) {
        getView().goToYesNoScreen(disableYes, useCase, game.getFlowDetails(), titleResId);
    }

    @Override
    public void showKilledPlayerYesNo(final ActivePlayer activePlayer, final boolean disableYes, final int titleResId, final GameUseCaseYesNoId useCase) {
        getView().goToKilledPlayerYesNoScreen(activePlayer, disableYes, titleResId, useCase, game.getFlowDetails());
    }

    @Override
    public void requestUserPlayerSelection(final List<ActivePlayer> activePlayers, final GameUseCaseId useCase) {
        List<Player> players = getPlayersList(activePlayers);
        getView().goToUserPlayerSelectionScreen(players, useCase, game.getFlowDetails());
    }

    @Override
    public void requestUserPlayerSelection(final List<ActivePlayer> activePlayers, final GameUseCaseYesNoId useCase) {
        List<Player> players = getPlayersList(activePlayers);
        getView().goToUserPlayerSelectionScreen(players, useCase, game.getFlowDetails());
    }

    @Override
    public void requestUserCardSelection(final List<SithCard> availableSithCards, final GameUseCaseCard useCase) {
        getView().goToUserCardSelectionScreen(availableSithCards, useCase, game.getFlowDetails());
    }

    @Override
    public void requestUserCardPeek(final List<ActivePlayer> players, final long delay, final GameUseCase useCase) {
        getView().goToUserCardPeekScreen(players, delay, useCase, game.getFlowDetails());
    }

    @Override
    public void speak(final int soundResId, final int stringResId, final GameUseCase useCase) {
        getView().goToSpeakScreen(soundResId, stringResId, useCase, game.getFlowDetails());
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
    public void sendSMS(final int stringResId, String number) {
        getView().sendSMS(stringResId, number);
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
        getView().showGameOver(game.getAlivePlayers(), game.getWinningTeam());
    }

    public interface View extends PresenterView {

        void goToDelayScreen(long delay, GameUseCase gameUseCase, FlowDetails flowDetails);

        void goToYesNoScreen(boolean disableYes, GameUseCaseYesNoId useCase, FlowDetails flowDetails, int titleResId);

        void goToKilledPlayerYesNoScreen(ActivePlayer activePlayer, boolean disableYes, int titleResId, GameUseCaseYesNoId useCase, FlowDetails flowDetails);

        void goToUserPlayerSelectionScreen(List<Player> activePlayers, GameUseCaseId useCase, FlowDetails flowDetails);

        void goToUserPlayerSelectionScreen(List<Player> activePlayers, GameUseCaseYesNoId useCase, FlowDetails flowDetails);

        void goToUserCardSelectionScreen(List<SithCard> availableSithCards, GameUseCaseCard useCase, FlowDetails flowDetails);

        void goToUserCardPeekScreen(List<ActivePlayer> players, long delay, GameUseCase useCase, FlowDetails flowDetails);

        void goToSpeakScreen(int soundResId, int stringResId, GameUseCase useCase, FlowDetails flowDetails);

        void goTotUserPairPlayerSelection(List<Player> players, GameUseCasePairId useCase, FlowDetails flowDetails);

        void showSelectPlayersScreen(List<Player> players);

        void showKillPlayersScreen(List<Player> activePlayers);

        void goToShuffleScreen(List<Player> players);

        void goToDayScreen(MainGame game);

        void speak(int soundResId);

        void showExitDialog();

        void showGameOver(List<ActivePlayer> players, @GameTeam.Team int winningTeam);

        void showError(int stringResId);

        void closeScreen();

        void popBackStack();

        void saveGame(MainGame mainGame);

        void deleteSavedGame();

        void playMusic(int musicType);

        void stopPlayingMusic();

        void sendSMS(final int stringResId, String number);

        void sendSMS(String text, String number);

        int getRawResourceId(String resourceName);

    }
}
