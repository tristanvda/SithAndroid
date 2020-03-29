package com.grietenenknapen.sithandroid.maingame;

import com.grietenenknapen.sithandroid.game.flowmanager.GameFlowCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public interface MainGameFlowCallBack extends GameFlowCallBack {

    void requestUserPairPlayerSelection(List<ActivePlayer> players);

    void showDelay(long delay);

    void requestYesNoAnswer(boolean disableYes, int titleResId);

    void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId);

    void requestUserPlayerSelection(List<ActivePlayer> activePlayers);

    void requestUsersPlayerSelection(List<ActivePlayer> activePlayers, int titleResId, int min, int max);

    void requestUserCardSelection(List<SithCard> availableSithCards);

    void requestUserCardPeek(List<ActivePlayer> players, long delay);

    void speak(int soundResId, int stringResId);

    void stackAndSpeak(int soundResSId);

    void stackAndSpeak(String soundResSId);

    void saveGame(MainGame game);

    void deleteSavedGame();

    void playMusic(int musicType);

    void stopPlayingMusic();

    void sendSMS(String number, int stringResId);

    void sendSMS(String number, int stringResId, Object... formatArgs);

}
