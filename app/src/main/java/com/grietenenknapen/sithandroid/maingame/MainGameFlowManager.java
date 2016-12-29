package com.grietenenknapen.sithandroid.maingame;

import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowManager;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.maingame.usecases.BB8UseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.BobaFettUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.ChewBaccaUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.GameUseCaseCard;
import com.grietenenknapen.sithandroid.maingame.usecases.HanSoloUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.IntroUseCase;
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
import com.grietenenknapen.sithandroid.util.SMSUtils;
import com.grietenenknapen.sithandroid.util.SithMusicPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainGameFlowManager extends GameFlowManager<MainGameFlowCallBack> implements
        BB8UseCase.CallBack,
        BobaFettUseCase.CallBack,
        HanSoloUseCase.CallBack,
        MazKanataUseCase.CallBack,
        SithUseCase.CallBack,
        KyloRenUseCase.CallBack,
        SkipUseCase.CallBack,
        IntroUseCase.CallBack {

    private static final int TURN_COUNT = 10;
    private static final int DELAY_PEAK = 3 * 1000;

    private static final String RAW_SOUND_BEHALVE = "behalve";
    private static final String RAW_SOUND_COUNCIL_AWAKES = "basis16_dejedicouncilontwaakt";

    private final MainGame mainGame;
    private final List<SithCard> sithCards;

    public MainGameFlowManager(MainGame game, List<SithCard> sithCards) {
        super(game);
        mainGame = game;
        this.sithCards = sithCards;
    }

    @Override
    protected GameUseCase getCurrentUseCase(final int turn) {
        switch (turn) {
            case 1:
                return new IntroUseCase(this, true, false);
            case 2:
                return new HanSoloUseCase(this, cardIsActive(GameCardType.HAN_SOLO), cardsInGameAndKilled(GameCardType.HAN_SOLO));
            case 3:
                return new BB8UseCase(this, cardIsActive(GameCardType.BB8), cardsInGameAndKilled(GameCardType.BB8));
            case 4:
                return new MazKanataUseCase(this, cardIsActive(GameCardType.MAZ_KANATA), cardsInGameAndKilled(GameCardType.MAZ_KANATA));
            case 5:
                return new KyloRenUseCase(this, cardIsActive(GameCardType.KYLO_REN), cardsInGameAndKilled(GameCardType.KYLO_REN));
            case 6:
                return new SithUseCase(this, cardIsActive(GameCardType.SITH), cardsInGameAndKilled(GameCardType.SITH));
            case 7:
                return new JediUseCase(this, cardIsActive(GameCardType.JEDI), cardsInGameAndKilled(GameCardType.JEDI));
            case 8:
                return new PeepingFinnUseCase(this, cardIsActive(GameCardType.PEEPING_FINN), cardsInGameAndKilled(GameCardType.PEEPING_FINN));
            case 9:
                return new ChewBaccaUseCase(this, cardIsActive(GameCardType.CHEWBACCA), cardsInGameAndKilled(GameCardType.CHEWBACCA));
            case 10:
                return new BobaFettUseCase(this, cardIsActive(GameCardType.BOBA_FETT),
                        mainGame.isRocketAlreadySelected(), cardsInGameAndKilled(GameCardType.BOBA_FETT));
            default:
                return new SkipUseCase(this);
        }
    }

    @Override
    protected void onRoundEnd() {
        boolean bobaFettKilledThisRound = false;

        for (ActivePlayer activePlayer : mainGame.getDeathList()) {
            if (activePlayer.getSithCard().getCardType() == GameCardType.BOBA_FETT) {
                bobaFettKilledThisRound = true;
            }
        }

        if (cardsInGameAndKilled(GameCardType.BOBA_FETT) && !bobaFettKilledThisRound) {
            uiListener.stackAndSpeak(RAW_SOUND_COUNCIL_AWAKES);
        }
        if (mainGame.getDeathList().size() > 0) {
            uiListener.stackAndSpeak(RAW_SOUND_BEHALVE);
            for (ActivePlayer activePlayer : mainGame.getDeathList()) {
                uiListener.stackAndSpeak(activePlayer.getSithCard().getSoundResId());
            }
        }
    }

    @Override
    protected boolean isGameOver() {
        return mainGame.isGameOver();
    }

    private boolean cardIsActive(@GameCardType.CardType int type) {
        List<ActivePlayer> activePlayers = mainGame.findPlayersByType(type);

        return activePlayers.size() > 0;
    }

    private boolean cardsInGameAndKilled(@GameCardType.CardType int type) {

        List<ActivePlayer> activePlayers = mainGame.findPlayersByType(type);

        if (activePlayers.size() == 0) {
            return false;
        }

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.isAlive()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected int getTurnCount() {
        return TURN_COUNT;
    }

    @Override
    protected void saveGameStatus() {
        uiListener.saveGame(mainGame);
    }

    @Override
    protected void deleteGameStatus() {
        uiListener.deleteSavedGame();
    }

    @Override
    public void turnKyloRenToDarkSide() {
        List<ActivePlayer> players = mainGame.findPlayersByType(GameCardType.KYLO_REN);

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
        List<ActivePlayer> players = mainGame.findPlayersByType(GameCardType.HAN_SOLO);

        if (players.size() > 0) {
            ActivePlayer hanSolo = players.get(0);
            hanSolo.setSithCard(sithCard);
        }
    }

    @Override
    public void useRockedLauncher(final long playerId) {
        mainGame.addToDeathList(playerId);
        mainGame.setBobaRocketUsed(true);
    }

    @Override
    public void setRockedAlreadySelected(boolean rockedAlreadySelected) {
        mainGame.setRocketAlreadySelected(rockedAlreadySelected);
    }

    @Override
    public void linkTwoLovers(final long lover1Id, long lover2Id) {
        final ActivePlayer lover1 = mainGame.getActivePlayer(lover1Id);
        final ActivePlayer lover2 = mainGame.getActivePlayer(lover2Id);
        mainGame.setLovers(new Pair<>(lover1, lover2));

        if (!TextUtils.isEmpty(lover1.getTelephoneNumber())) {
            //TODO: resources
            SMSUtils.sendSMS("Verbonden door BB8!", lover1.getTelephoneNumber());
        }

        if (!TextUtils.isEmpty(lover2.getTelephoneNumber())) {
            //TODO: resources
            SMSUtils.sendSMS("Verbonden door BB8!", lover2.getTelephoneNumber());
        }
    }

    @Override
    public void playBobaFettMusic() {
        uiListener.playMusic(SithMusicPlayer.MUSIC_TYPE_BOBA_FETT);
    }

    @Override
    public void playHanSoloMusic() {
        uiListener.playMusic(SithMusicPlayer.MUSIC_TYPE_HAN_SOLO);
    }


    @Override
    public void playBB8Music() {
        uiListener.playMusic(SithMusicPlayer.MUSIC_TYPE_BB8);
    }

    @Override
    public void playSithMusic() {
        uiListener.playMusic(SithMusicPlayer.MUSIC_TYPE_BOBA_FETT);
    }

    @Override
    public void playMazKanataMusic() {
        uiListener.playMusic(SithMusicPlayer.MUSIC_TYPE_MAZ_KANATA);
    }

    @Override
    public void useMedPack() {
        ActivePlayer activePlayer = mainGame.getCurrentKilledPlayer();
        mainGame.deleteFromDeathList(activePlayer.getPlayerId());
        mainGame.setBobaMedPackUsed(true);
    }

    @Override
    public void stopPlayingMusic() {
        uiListener.stopPlayingMusic();
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
    public void requestYesNoAnswerRocket(final GameUseCaseYesNoId useCase, final int titleResId) {
        uiListener.requestYesNoAnswer(mainGame.isBobaRocketUsed(), useCase, titleResId);
    }

    @Override
    public void showKilledPlayerMedPackYesNo(GameUseCaseYesNoId useCase) {
        uiListener.showKilledPlayerYesNo(mainGame.getCurrentKilledPlayer(),
                mainGame.isBobaMedPackUsed(), R.string.boba_fett_use_med_pack, useCase);
    }

    @Override
    public void requestUserPlayerRocketSelection(final GameUseCaseYesNoId useCase) {

        final List<ActivePlayer> killAblePlayers = new ArrayList<>();
        final ActivePlayer bobaFettPlayer = mainGame.findPlayersByType(GameCardType.BOBA_FETT).get(0);
        final ActivePlayer currentKilledPlayer = mainGame.getCurrentKilledPlayer();

        for (ActivePlayer activePlayer : mainGame.getAlivePlayers()) {
            if (activePlayer.getPlayerId() != bobaFettPlayer.getPlayerId()
                    && (currentKilledPlayer == null || activePlayer.getPlayerId() != currentKilledPlayer.getPlayerId())) {
                killAblePlayers.add(activePlayer);
            }
        }
        uiListener.requestUserPlayerSelection(killAblePlayers, useCase);
    }

    @Override
    public void requestUserCardSelection(final GameUseCaseCard useCase) {
        List<ActivePlayer> activePlayers = mainGame.getAlivePlayers();

        List<SithCard> availableSithCards = new ArrayList<>();

        for (SithCard sithCard : sithCards) {
            boolean available = true;
            for (ActivePlayer activePlayer : activePlayers) {
                if (activePlayer.getSithCard().getId() == sithCard.getId()) {
                    available = false;
                }
            }
            if (available) {
                availableSithCards.add(sithCard);
            }
        }
        uiListener.requestUserCardSelection(availableSithCards, useCase);
    }

    @Override
    public void requestUserCardPeek(final GameUseCase gameUseCase) {
        uiListener.requestUserCardPeek(mainGame.getAlivePlayers(), DELAY_PEAK / 2, gameUseCase);
    }

    @Override
    public void requestUserPlayerSelectionSith(final GameUseCaseId useCase) {
        uiListener.requestUserPlayerSelection(mainGame.getAlivePlayersLightSide(), useCase);
    }
}
