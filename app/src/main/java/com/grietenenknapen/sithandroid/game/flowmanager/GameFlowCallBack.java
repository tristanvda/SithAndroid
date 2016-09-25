package com.grietenenknapen.sithandroid.game.flowmanager;

/**
 * Extend this interface to provide communication from the Game Flow Manger to the UI
 * This interface will be necessary for asking User input and passing 'FlowDetails' objects to the fragments
 */
public interface GameFlowCallBack {

    void roundStatusChanged(boolean started, boolean gameOver);

}