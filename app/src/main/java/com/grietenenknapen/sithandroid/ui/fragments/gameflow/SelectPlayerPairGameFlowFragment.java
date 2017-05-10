package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCasePairId;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.SelectPlayerAdapter;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.PlayerSelectPresenter;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayerPairGameFlowFragment extends PlayerSelectFragment
        implements GameFlowFragment<UseCasePairId> {
    private static final String PRESENTER_TAG = "select_player_pair_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";

    private UseCasePairId gameUseCase;
    private FlowDetails flowDetails;

    public static Bundle createStartBundle(final FlowDetails flowDetails,
                                           final ArrayList<Player> players) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelableArrayList(KEY_PLAYERS, players);
        bundle.putInt(KEY_SELECTION_MAX, 2);

        return bundle;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        setTitle(getString(R.string.pair_select_players));
        setNextButtonImage(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
        setNextButtonVisibility(false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
    }

    @Override
    public void showPlayers(final List<Player> players,
                            final int maxSelection) {

        super.showPlayers(players, maxSelection);
        getAdapter().setOnPlayerSelectListener(new SelectPlayerAdapter.OnPlayerSelectListener() {
            @Override
            public void onPlayerSelectionChanged(List<Player> selectedPlayers) {
                if (selectedPlayers.size() > 1) {
                    setNextButtonVisibility(true);
                } else {
                    setNextButtonVisibility(false);
                }
            }
        });
    }

    @Override
    public void playersSelected(final List<Player> players) {
        if (players.size() > 1) {
            final Pair<Long, Long> pair = new Pair<>(players.get(0).getId(),
                    players.get(1).getId());
            gameUseCase.onExecuteStep(flowDetails.getStep(), pair);
        }
    }

    @Override
    protected String getPresenterTag() {
        FlowDetails flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        if (flowDetails != null) {
            return PRESENTER_TAG + flowDetails.getRound() + flowDetails.getStep() + flowDetails.getTurn();
        }
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<PlayerSelectPresenter> getPresenterFactory() {
        return super.getPresenterFactory();
    }

    @Override
    public void setUseCase(final UseCasePairId useCase) {
        this.gameUseCase = useCase;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }
}
