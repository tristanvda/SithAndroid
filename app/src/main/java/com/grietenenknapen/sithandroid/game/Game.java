package com.grietenenknapen.sithandroid.game;



public interface Game {

    /**
     * Each Use Case has a few steps that it needs to pass trough
     * This method returns the current step of the current Use Case
     * @return the current step
     */
    int getCurrentStep();

    /**
     * Each round has a few turns that it needs to pass trough
     * This method returns the current turn of the current Round
     * @return the current step
     */
    int getCurrentTurn();

    /**
     * Each Game has a few rounds that it needs to pass trough
     * This method returns the current round of the current Game
     * @return the current step
     */
    int getCurrentRound();

    /**
     * @return if the current round is active
     */
    boolean isRoundActive();

    /**
     * Before each round the game should to a setup to prepare for the next round
     * This means that variables, that are round-related will be reset or set
     */
    void setupNewRound();

    /***
     * Finish the current round
     */
    void finishRound();


    /**
     * Increase the current turn
     */
    void nextTurn();


    /**
     * Increase the current step
     */
    void nextStep();


    /**
     * Increase next sround
     */
    void nextRound();


    /**
     * check if "game over" or not
     * @return boolean indicating if "game over"
     */
    boolean isGameOver();

}
