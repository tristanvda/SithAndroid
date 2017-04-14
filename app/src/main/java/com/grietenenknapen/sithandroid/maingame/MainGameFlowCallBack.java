package com.grietenenknapen.sithandroid.maingame;

import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public interface MainGameFlowCallBack extends GameFlowCallBack {

    void requestUserPairPlayerSelection(List<ActivePlayer> players, UseCasePairId useCase);

    void showDelay(long delay, GameUseCase gameUseCase);

    void requestYesNoAnswer(boolean disableYes, UseCaseYesNo useCase, int titleResId);

    void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId, UseCaseYesNo useCase);

    void requestUserPlayerSelection(List<ActivePlayer> activePlayers, UseCaseId useCase);

    void requestUserCardSelection(List<SithCard> availableSithCards, UseCaseCard useCase);

    void requestUserCardPeek(List<ActivePlayer> players, long delay, GameUseCase useCase);

    void speak(int soundResId, int stringResId, GameUseCase useCase);

    void stackAndSpeak(int soundResSId);

    void stackAndSpeak(String soundResSId);

    void saveGame(MainGame game);

    void deleteSavedGame();

    void playMusic(int musicType);

    void stopPlayingMusic();

    void sendSMS(int stringResId, String number);

}
