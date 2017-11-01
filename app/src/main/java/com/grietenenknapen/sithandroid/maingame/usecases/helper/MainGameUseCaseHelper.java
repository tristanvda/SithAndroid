package com.grietenenknapen.sithandroid.maingame.usecases.helper;


import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.BB8UseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.BobaFettUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.ChewBaccaUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.HanSoloUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.JediUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.KyloRenUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.MazKanataUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.SithUseCase;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;

public final class MainGameUseCaseHelper {

    private MainGameUseCaseHelper() {
    }

    public static boolean isUseCaseActivePlayer(final UseCase useCase,
                                                final ActivePlayer activePlayer) {

        final @GameCardType.CardType int cardType = activePlayer.getSithCard().getCardType();

        if (useCase instanceof BB8UseCase) {
            return cardType == GameCardType.BB8;
        } else if (useCase instanceof BobaFettUseCase) {
            return cardType == GameCardType.BOBA_FETT;
        } else if (useCase instanceof ChewBaccaUseCase) {
            return cardType == GameCardType.CHEWBACCA;
        } else if (useCase instanceof HanSoloUseCase) {
            return cardType == GameCardType.HAN_SOLO;
        } else if (useCase instanceof JediUseCase) {
            return cardType == GameCardType.JEDI;
        } else if (useCase instanceof KyloRenUseCase) {
            return cardType == GameCardType.KYLO_REN;
        } else if (useCase instanceof MazKanataUseCase) {
            return cardType == GameCardType.PEEPING_FINN;
        } else if (useCase instanceof SithUseCase) {
            return cardType == GameCardType.SITH;
        }

        return false;
    }
}
