package com.grietenenknapen.sithandroid.ui.flows.gameflow;

import com.grietenenknapen.sithandroid.ui.presenter.PresenterActivity;
import com.grietenenknapen.sithandroid.ui.presenter.PresenterFactory;

public class GameFlowActivity extends PresenterActivity<GameFlowPresenter, GameFlowPresenter.View> implements GameFlowPresenter.View {
    private static final String TAG = "game_flow_activity";

    @Override
    protected String getPresenterTag() {
        return TAG;
    }

    @Override
    protected PresenterFactory<GameFlowPresenter> getPresenterFactory() {
        return new GameFlowPresenterFactory();
    }

    @Override
    protected GameFlowPresenter.View getPresenterView() {
        return this;
    }

    private static class GameFlowPresenterFactory implements PresenterFactory<GameFlowPresenter>{

        @Override
        public GameFlowPresenter createPresenter() {
            return new GameFlowPresenter();
        }
    }

}
