package com.grietenenknapen.sithandroid.game.flowmanager;


public interface UseCaseCallBack {
    /**
     * Go to the next step
     */
    void nextStep(long delay);

    void skipStepDelay(long delay);

    void skipStep();
}
