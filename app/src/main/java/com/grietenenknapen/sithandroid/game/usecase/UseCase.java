package com.grietenenknapen.sithandroid.game.usecase;


public interface UseCase<T> {

    /**
     * this is the setup method of the UseCase
     * parameters that are necessary for the Use Case to know during it's entire life cycle can
     * be passed trough this method
     *
     * @param round the current round of the game
     */
    void onSetupUseCase(int round);

    /**
     * Every pre-user-action for the step should be done here (like showing a list of users)
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step the current step of the Use Case
     */
    void onPrepareStep(int step);


    /**
     * Handle the action of the post user interaction. This method should be called to execute the step and continue the flow
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step the current step of the Use Case
     */
    void onExecuteStep(final int step);


    /**
     * Same function as onExecuteStep, but provides data to the Use Case
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step the current step of the Use Case
     */
    void onExecuteStep(int step, T stepData);

    /**
     * Return true if the Use Case has finished it's job and the flowmanger should continue to the next turn
     * Mind that the step parameter should not be cached in the UseCase, as it will be provided by
     * the flow manager every time
     *
     * @param step the current step of the Use Case
     */
    boolean finishUseCase(int step);

}
