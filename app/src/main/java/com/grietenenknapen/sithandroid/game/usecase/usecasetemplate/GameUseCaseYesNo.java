package com.grietenenknapen.sithandroid.game.usecase.usecasetemplate;

import android.util.Pair;

import com.grietenenknapen.sithandroid.game.flowmanager.UseCaseCallBack;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;

public abstract class GameUseCaseYesNo<L extends UseCaseCallBack> extends GameUseCase<Pair<Boolean, Long>, L> {

    public GameUseCaseYesNo(L flowManagerListener, boolean active) {
        super(flowManagerListener, active);
    }
}