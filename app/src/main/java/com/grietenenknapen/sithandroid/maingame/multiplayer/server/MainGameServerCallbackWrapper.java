package com.grietenenknapen.sithandroid.maingame.multiplayer.server;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowManager;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowCallBack;
import com.grietenenknapen.sithandroid.maingame.MainGameFlowManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandPeek;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandPlayerYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectPair;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectPlayer;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectSithCard;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandYesNo;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.maingame.usecases.helper.MainGameUseCaseHelper;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.util.ResourceProvider;

import java.util.List;

public class MainGameServerCallbackWrapper implements MainGameFlowCallBack {

    private final MainGameFlowCallBack callBack;
    private final MainGame game;
    private final WifiDirectGameServerManager serverManager;
    private final ResourceProvider resourceProvider;
    private final GameFlowManager<MainGameFlowCallBack> flowManager;

    public MainGameServerCallbackWrapper(final MainGameFlowCallBack callBack,
                                         final MainGame game,
                                         final WifiDirectGameServerManager serverManager,
                                         final ResourceProvider resourceProvider,
                                         final GameFlowManager<MainGameFlowCallBack> flowManager) {

        this.callBack = callBack;
        this.game = game;
        this.serverManager = serverManager;
        this.resourceProvider = resourceProvider;
        this.flowManager = flowManager;
    }

    @Override
    public void roundStatusChanged(final boolean started, final boolean gameOver) {
        this.callBack.roundStatusChanged(started, gameOver);
    }

    @Override
    public void requestUserPairPlayerSelection(final List<ActivePlayer> players) {
        this.callBack.requestUserPairPlayerSelection(players);
        final WifiPackage wifiPackage = new WifiFlowCommandSelectPair(game.getFlowDetails(), players);
        sendWifiPackageToClients(wifiPackage, flowManager.getActiveGameUseCase());
    }

    @Override
    public void showDelay(final long delay) {
        this.callBack.showDelay(delay);
        //Send nothing in this case
    }

    @Override
    public void requestYesNoAnswer(final boolean disableYes, final int titleResId) {
        this.callBack.requestYesNoAnswer(disableYes, titleResId);
        final WifiPackage wifiPackage = new WifiFlowCommandYesNo(game.getFlowDetails(), resourceProvider.getString(titleResId), disableYes);
        sendWifiPackageToClients(wifiPackage, flowManager.getActiveGameUseCase());
    }

    @Override
    public void showPlayerYesNo(final ActivePlayer activePlayer, final boolean disableYes, final int titleResId) {
        this.callBack.showPlayerYesNo(activePlayer, disableYes, titleResId);
        final WifiPackage wifiPackage = new WifiFlowCommandPlayerYesNo(game.getFlowDetails(), activePlayer, resourceProvider.getString(titleResId), disableYes);
        sendWifiPackageToClients(wifiPackage, flowManager.getActiveGameUseCase());
    }

    @Override
    public void requestUserPlayerSelection(final List<ActivePlayer> activePlayers) {
        this.callBack.requestUserPlayerSelection(activePlayers);
        final WifiPackage wifiPackage = new WifiFlowCommandSelectPlayer(game.getFlowDetails(), activePlayers, resourceProvider.getString(R.string.single_select_player));
        sendWifiPackageToClients(wifiPackage, flowManager.getActiveGameUseCase());
    }

    @Override
    public void requestUserCardSelection(final List<SithCard> availableSithCards) {
        this.callBack.requestUserCardSelection(availableSithCards);
        final WifiPackage wifiPackage = new WifiFlowCommandSelectSithCard(game.getFlowDetails(), availableSithCards);
        sendWifiPackageToClients(wifiPackage, flowManager.getActiveGameUseCase());
    }

    @Override
    public void requestUserCardPeek(final List<ActivePlayer> players, final long delay) {
        this.callBack.requestUserCardPeek(players, delay);
        final WifiPackage wifiPackage = new WifiFlowCommandPeek(game.getFlowDetails(), players);
        sendWifiPackageToClients(wifiPackage, flowManager.getActiveGameUseCase());
    }

    @Override
    public void speak(final int soundResId, final int stringResId) {
        this.callBack.speak(soundResId, stringResId);
    }

    @Override
    public void stackAndSpeak(final int soundResSId) {
        this.callBack.stackAndSpeak(soundResSId);
    }

    @Override
    public void stackAndSpeak(final String soundResSId) {
        this.callBack.stackAndSpeak(soundResSId);
    }

    @Override
    public void saveGame(final MainGame game) {
        this.callBack.saveGame(game);
    }

    @Override
    public void deleteSavedGame() {
        this.callBack.deleteSavedGame();
    }

    @Override
    public void playMusic(final int musicType) {
        this.callBack.playMusic(musicType);
    }

    @Override
    public void stopPlayingMusic() {
        this.callBack.stopPlayingMusic();
    }

    @Override
    public void sendSMS(final int stringResId, final String number) {
        this.callBack.sendSMS(stringResId, number);
    }

    private void sendWifiPackageToClients(final WifiPackage wifiPackage, final UseCase gameUseCase) {
        if (!serverManager.isServerRunning()) {
            return;
        }
        for (ActivePlayer activePlayer : game.getAlivePlayers()) {
            if (activePlayer.isAlive()
                    && MainGameUseCaseHelper.isUseCaseActivePlayer(gameUseCase, activePlayer)) {

                serverManager.sendWifiFlowPackageToPlayer(wifiPackage, activePlayer.getPlayerId());
            }
        }
    }
}
