package com.grietenenknapen.sithandroid.maingame;

import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.RandomCommentUseCase;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.util.MathUtils;

import java.util.List;

public class MainGameRandomFlowManager extends MainGameFlowManager implements RandomCommentUseCase.CallBack {

    private static final int RANDOM_COMMENT_CHANCE = 10;
    private List<Pair<Integer, Integer>> randomResourceList;

    public MainGameRandomFlowManager(final MainGame game,
                                     final List<SithCard> sithCards,
                                     final List<Pair<Integer, Integer>> randomResourceList) {
        super(game, sithCards);
        this.randomResourceList = randomResourceList;
    }

    @Override
    protected GameUseCase getCurrentUseCase(final int turn) {
        return super.getCurrentUseCase(turn);
    }

    @Override
    protected void proceedToNextTurn() {
        if (shouldPlayRandom()) {
            final Pair<Integer, Integer> randomResourcePair = randomResourceList.get(MathUtils.generateRandomInteger(0, randomResourceList.size() - 1));
            RandomCommentUseCase randomCommentUseCase = new RandomCommentUseCase(this, true, false, randomResourcePair.first, randomResourcePair.second);
            randomCommentUseCase.onPrepareStep(0);
        } else {
            super.proceedToNextTurn();
        }
    }

    private boolean shouldPlayRandom() {
        if (randomResourceList != null && randomResourceList.size() > 0) {
            final int randomInt = MathUtils.generateRandomInteger(0, RANDOM_COMMENT_CHANCE);
            return randomInt == 1;
        }

        return false;
    }
}
