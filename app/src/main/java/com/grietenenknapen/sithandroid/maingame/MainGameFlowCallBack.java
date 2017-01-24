package com.grietenenknapen.sithandroid.maingame;

import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.maingame.usecases.GameUseCaseCard;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public interface MainGameFlowCallBack extends GameFlowCallBack {

    void requestUserPairPlayerSelection(List<ActivePlayer> players, GameUseCasePairId useCase);

    void showDelay(long delay, GameUseCase gameUseCase);

    void requestYesNoAnswer(boolean disableYes, GameUseCaseYesNoId useCase, int titleResId);

    void showKilledPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId, GameUseCaseYesNoId useCase);

    void requestUserPlayerSelection(List<ActivePlayer> activePlayers, GameUseCaseId useCase);

    void requestUserPlayerSelection(List<ActivePlayer> activePlayers, GameUseCaseYesNoId useCase);

    void requestUserCardSelection(List<SithCard> availableSithCards, GameUseCaseCard useCase);

    void requestUserCardPeek(List<ActivePlayer> players, long delay, GameUseCase useCase);

    void speak(int soundResId, int stringResId, GameUseCase useCase);

    void stackAndSpeak(String soundResStringId);

    void saveGame(MainGame game);

    void deleteSavedGame();

    void playMusic(int musicType);

    void stopPlayingMusic();

    void sendSMS(final String text, final String number);

}
