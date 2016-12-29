package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.View;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.SelectPlayerAdapter;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.PlayerSelectPresenter;

import java.util.ArrayList;
import java.util.List;

public class SelectPLayerBobaGameFlowFragment extends PlayerSelectFragment implements GameFlowFragment<GameUseCaseYesNoId> {
    private static final String PRESENTER_TAG = "select_player_boba_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";

    private GameUseCaseYesNoId gameUseCase;
    private FlowDetails flowDetails;

    public static SelectPLayerBobaGameFlowFragment createInstance(final FlowDetails flowDetails,
                                                                   final ArrayList<Player> players) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelableArrayList(KEY_PLAYERS, players);
        bundle.putInt(KEY_SELECTION_MAX, 1);

        SelectPLayerBobaGameFlowFragment fragment = new SelectPLayerBobaGameFlowFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        setTitle(getString(R.string.single_select_player));
        setNextButtonImage(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
        setNextButtonVisibility(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
    }

    @Override
    public void playersSelected(List<Player> players) {
        if (players.size() > 0) {
            gameUseCase.onExecuteStep(flowDetails.getStep(),
                    new Pair<>(true, players.get(0).getId()));
        }
    }

    @Override
    public void showPlayers(List<Player> players, int maxSelection) {
        super.showPlayers(players, maxSelection);
        getAdapter().setOnPlayerSelectListener(new SelectPlayerAdapter.OnPlayerSelectListener() {
            @Override
            public void onPlayerSelectionChanged(List<Player> selectedPlayers) {
                if (selectedPlayers.size() > 0) {
                    setNextButtonVisibility(true);
                } else {
                    setNextButtonVisibility(false);
                }
            }
        });
    }

    @Override
    protected String getPresenterTag() {
        FlowDetails flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        if (flowDetails != null) {
            return PRESENTER_TAG + flowDetails.getRound() + flowDetails.getStep() + flowDetails.getTurn();
        }
        return PRESENTER_TAG;    }

    @Override
    protected PresenterFactory<PlayerSelectPresenter> getPresenterFactory() {
        return super.getPresenterFactory();
    }

    @Override
    public void setUseCase(GameUseCaseYesNoId useCase) {
        this.gameUseCase = useCase;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }
}
