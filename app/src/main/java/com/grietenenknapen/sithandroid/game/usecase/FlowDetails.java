package com.grietenenknapen.sithandroid.game.usecase;


public class FlowDetails {
    private final int round;
    private final int turn;
    private final int step;

    public FlowDetails(int round, int step, int turn) {
        this.round = round;
        this.step = step;
        this.turn = turn;
    }

    public int getRound() {
        return round;
    }

    public int getStep() {
        return step;
    }

    public int getTurn() {
        return turn;
    }
}
