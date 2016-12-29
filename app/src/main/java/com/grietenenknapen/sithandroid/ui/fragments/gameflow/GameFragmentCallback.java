package com.grietenenknapen.sithandroid.ui.fragments.gameflow;


import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;

public interface GameFragmentCallback {

    void setGameStatus(@GameFlowPresenter.GameStatus int gameStatus);
}
