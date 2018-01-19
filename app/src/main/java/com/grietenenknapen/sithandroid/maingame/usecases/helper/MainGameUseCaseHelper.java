package com.grietenenknapen.sithandroid.maingame.usecases.helper;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.BB8UseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.BobaFettUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.ChewBaccaUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.HanSoloUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.JediUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.KyloRenUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.MazKanataUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.PeepingFinnUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.SithUseCase;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.model.game.GameSide;

public final class MainGameUseCaseHelper {

    private MainGameUseCaseHelper() {
    }

    public static boolean activePlayerCanExecuteUseCase(final UseCase useCase,
                                                        final ActivePlayer activePlayer) {
        return activePlayerCanExecuteUseCase(useCase.getClass(), activePlayer);
    }

    public static <T extends UseCase> boolean activePlayerCanExecuteUseCase(final Class<T> useCaseClass,
                                                                            final ActivePlayer activePlayer) {

        final @GameCardType.CardType int cardType = activePlayer.getSithCard().getCardType();
        final @GameSide.Side int side = activePlayer.getSide();

        if (BB8UseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.BB8;
        } else if (BobaFettUseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.BOBA_FETT;
        } else if (ChewBaccaUseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.CHEWBACCA;
        } else if (HanSoloUseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.HAN_SOLO;
        } else if (JediUseCase.class.equals(useCaseClass)) {
            return side == GameSide.JEDI;
        } else if (KyloRenUseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.KYLO_REN;
        } else if (MazKanataUseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.MAZ_KANATA;
        } else if (PeepingFinnUseCase.class.equals(useCaseClass)) {
            return cardType == GameCardType.PEEPING_FINN;
        } else if (SithUseCase.class.equals(useCaseClass)) {
            return side == GameSide.SITH;
        }

        return false;
    }
}
