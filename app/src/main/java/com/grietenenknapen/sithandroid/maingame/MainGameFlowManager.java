package com.grietenenknapen.sithandroid.maingame;

import android.content.Context;
import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowManager;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.usecases.BB8UseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.BobaFettUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.ChewBaccaUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.GameUseCaseCard;
import com.grietenenknapen.sithandroid.maingame.usecases.HanSoloUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.JediUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.KyloRenUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.MazKanataUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.PeepingFinnUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.SithUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.SkipUseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.model.game.GameSide;

import java.util.List;

public class MainGameFlowManager extends GameFlowManager<MainGameFlowCallBack> implements
        BB8UseCase.CallBack,
        BobaFettUseCase.CallBack,
        HanSoloUseCase.CallBack,
        MazKanataUseCase.CallBack,
        SithUseCase.CallBack,
        KyloRenUseCase.CallBack,
        SkipUseCase.CallBack {

    private static final int TURN_COUNT = 9;
    private static final int DEFAULT_DELAY = 10 * 1000;
    private static final int DELAY_PEAK = 3 * 1000;

    private static final String RAW_SOUND_BEHALVE = "behalve";

    private final MainGame mainGame;

    public MainGameFlowManager(MainGame game) {
        super(game);
        mainGame = game;
    }

    @Override
    protected GameUseCase getCurrentUseCase(final int turn) {
        switch (turn) {
            case 1:
                return new HanSoloUseCase(this, cardIsActive(GameCardType.HAN_SOLO));
            case 2:
                return new BB8UseCase(this, cardIsActive(GameCardType.BB8));
            case 3:
                return new MazKanataUseCase(this, cardIsActive(GameCardType.MAZ_KANATA));
            case 4:
                return new KyloRenUseCase(this, cardIsActive(GameCardType.KYLO_REN));
            case 5:
                return new SithUseCase(this, cardIsActive(GameCardType.SITH));
            case 6:
                return new JediUseCase(this, cardIsActive(GameCardType.JEDI));
            case 7:
                return new PeepingFinnUseCase(this, cardIsActive(GameCardType.PEEPING_FINN));
            case 8:
                return new ChewBaccaUseCase(this, cardIsActive(GameCardType.CHEWBACCA));
            case 9:
                return new BobaFettUseCase(this, cardIsActive(GameCardType.BOBA_FETT),
                        mainGame.isRocketAlreadySelected(), mainGame.isMedPackAlreadySelected());
            default:
                return new SkipUseCase(this, false);
        }
    }

    @Override
    protected void onRoundEnd() {
        uiListener.stackAndSpeak(RAW_SOUND_BEHALVE);
        for (ActivePlayer activePlayer : mainGame.getDeathList()) {
            uiListener.stackAndSpeak(activePlayer.getSithCard().getSoundResId());
        }
    }

    @Override
    protected boolean isGameOver() {
        return mainGame.isGameOver();
    }

    private boolean cardIsActive(@GameCardType.CardType int type) {
        List<ActivePlayer> activePlayers = mainGame.findPlayersrByType(type);

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.isAlive()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected int getTurnCount() {
        return TURN_COUNT;
    }


    @Override
    public void turnKyloRenToDarkSide() {
        List<ActivePlayer> players = mainGame.findPlayersrByType(GameCardType.KYLO_REN);

        if (players.size() > 0) {
            ActivePlayer kyloRen = players.get(0);
            kyloRen.setSide(GameSide.SITH);
        }
    }

    @Override
    public void markUserAsDeath(final long playerId) {
        mainGame.addToDeathList(playerId);
    }

    @Override
    public void switchHanSoloUserCard(final SithCard sithCard) {
        List<ActivePlayer> players = mainGame.findPlayersrByType(GameCardType.HAN_SOLO);

        if (players.size() > 0) {
            ActivePlayer hanSolo = players.get(0);
            hanSolo.setSithCard(sithCard);
        }
    }

    @Override
    public void useRockedLauncher(final long playerId) {
        mainGame.addToDeathList(playerId);
    }

    @Override
    public void linkTwoLovers(final long lover1Id, long lover2Id) {
        mainGame.setLovers(new Pair<>(mainGame.getActivePlayer(lover1Id),
                mainGame.getActivePlayer(lover2Id)));
    }

    @Override
    public void useMedPack() {
        ActivePlayer activePlayer = mainGame.getCurrentKilledPlayer();
        mainGame.deleteFromDeathList(activePlayer.getPlayerId());
    }

    @Override
    public void skipStepDelay(long delay) {
        uiListener.showDelay(delay, currentUseCase);
    }

    @Override
    public void skipStep() {
        currentUseCase.onExecuteStep(mainGame.getCurrentStep());
    }

    @Override
    public void requestUserPairPlayerSelection(final GameUseCasePairId useCase) {
        uiListener.requestUserPairPlayerSelection(mainGame.getAlivePlayers(), useCase);
    }

    @Override
    public void showDelay(final long delay, final GameUseCase gameUseCase) {
        uiListener.showDelay(delay, gameUseCase);
    }

    @Override
    public void speak(final int soundResId, final int stringResId, final GameUseCase gameUseCase) {
        uiListener.speak(soundResId, stringResId, gameUseCase);
    }

    @Override
    public void requestYesNoAnswerMedPack(final GameUseCaseYesNo useCase) {
        uiListener.requestYesNoAnswer(mainGame.isBobaMedPackUsed(), useCase);
    }

    @Override
    public void requestYesNoAnswerRocket(final GameUseCaseYesNo useCase) {
        uiListener.requestYesNoAnswer(mainGame.isBobaRocketUsed(), useCase);
    }

    @Override
    public void showKilledPlayerDelay(final GameUseCase useCase) {
        uiListener.showKilledPlayerDelay(mainGame.getCurrentKilledPlayer(), DEFAULT_DELAY / 2, useCase);
    }

    @Override
    public void requestUserPlayerSelection(final GameUseCaseYesNo useCase) {
        uiListener.requestUserPlayerSelection(mainGame.getAlivePlayers(), useCase);
    }

    @Override
    public void requestUserCardSelection(final GameUseCaseCard useCase) {
        uiListener.requestUserCardSelection(useCase);
    }

    @Override
    public void requestUserCardPeek(final GameUseCase gameUseCase) {
        uiListener.requestUserCardPeek(mainGame.getAlivePlayers(), DELAY_PEAK / 2, gameUseCase);
    }

    @Override
    public void requestUserPlayerSelection(final GameUseCaseId useCase) {
        uiListener.requestUserPlayerSelection(mainGame.getAlivePlayers(), useCase);
    }
}
