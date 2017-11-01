package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;

public interface GameFlowActivity {

    void setGameStatus(@GameFlowPresenter.GameStatus int gameStatus);

    UseCase getCurrentGameUseCase();
}
