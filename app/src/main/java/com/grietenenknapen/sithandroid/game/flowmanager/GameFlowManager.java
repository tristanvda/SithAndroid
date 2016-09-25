package com.grietenenknapen.sithandroid.game.flowmanager;

import android.os.Handler;

import com.grietenenknapen.sithandroid.game.Game;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;

public abstract class GameFlowManager<U extends GameFlowCallBack> implements UseCaseCallBack {
    private static final long TURN_DELAY = 500;

    private final Game game;
    protected GameUseCase currentUseCase;
    private Handler handler = new Handler();
    private boolean started;
    protected U uiListener;

    public GameFlowManager(Game game) {
        this.game = game;
    }

    public void startNewRound() {
        game.setupNewRound();
        started = true;
    }

    /**
     * Call this method only once to attach the flowManager to the UI
     * Preferably this method can be called during the attach call of the presenter
     */
    public void attach(U uiListener) {
        this.uiListener = uiListener;
        checkGameStatus();

        //Every time the UI gets attached the current step will be processed
        //The UI should handle whether this data is old or new
        processStep();
    }

    /**
     * Call this method only once to attach the flowManager to the UI
     * Preferably this method can be called during the attach call of the presenter
     */
    public void onDetach() {
        this.uiListener = null;
    }

    /**
     * Check if the flow manager is started and processing a round
     *
     * @return if the flow has started
     */
    public boolean isStarted() {
        return started;
    }

    @Override
    public void nextStep(long delay) {
        handler.postDelayed(runnableNextStep, delay);
    }

    private Runnable runnableNextStep = new Runnable() {
        @Override
        public void run() {
            proceedToNextStep();
        }
    };

    private void proceedToNextStep() {
        game.nextStep();
        processStep();
    }

    private void processStep() {
        if (currentUseCase == null) {
            return;
        }

        if (currentUseCase.finishUseCase(game.getCurrentStep())) {
            //Finish round or else finish turn
            if (game.getCurrentTurn() >= getTurnCount()) {
                game.finishRound();
                started = false;
                onRoundEnd();
                uiListener.roundStatusChanged(started, isGameOver());
            } else {
                handler.postDelayed(runnableNextTurn, TURN_DELAY);
            }
        } else {
            currentUseCase.onPrepareStep(game.getCurrentStep());
        }

    }

    private Runnable runnableNextTurn = new Runnable() {
        @Override
        public void run() {
            proceedToNextTurn();
        }
    };

    private void proceedToNextTurn() {
        game.nextTurn();
        processStep();
    }

    private void checkGameStatus() {
        if (game.isRoundActive()) {
            currentUseCase = getCurrentUseCase(game.getCurrentRound());
            started = true;
            uiListener.roundStatusChanged(started, false);
        } else {
            started = false;
            uiListener.roundStatusChanged(started, false);
        }
    }

    //TODO: ENDING THE USE CASEEEEEEEEEEEEEEEEEEEE!!!!!

    protected abstract void onRoundEnd();

    protected abstract boolean isGameOver();

    protected abstract GameUseCase getCurrentUseCase(int turn);

    protected abstract int getTurnCount();

}
