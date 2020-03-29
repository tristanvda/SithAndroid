package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseIds;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCasePairId;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.SelectPlayerAdapter;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.PlayerSelectPresenter;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayersGameFlowFragment extends PlayerSelectFragment implements GameFlowFragment {
    private static final String PRESENTER_TAG = "select_player_pair_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";
    private static final String KEY_MIN = "key:min";
    private static final String KEY_TITLE = "key:title";

    private UseCaseIds gameUseCase;
    private FlowDetails flowDetails;

    public static Bundle createStartBundleActive(final FlowDetails flowDetails,
                                                 final List<ActivePlayer> players,
                                                 final String title,
                                                 final int min,
                                                 final int max) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelableArrayList(KEY_PLAYERS, createPlayersList(players));
        bundle.putInt(KEY_SELECTION_MAX, max);
        bundle.putInt(KEY_MIN, min);
        bundle.putString(KEY_TITLE, title);

        return bundle;
    }

    public static Bundle createStartBundle(final FlowDetails flowDetails,
                                           final List<Player> players,
                                           final String title,
                                           final int min,
                                           final int max) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelableArrayList(KEY_PLAYERS, new ArrayList<>(players));
        bundle.putInt(KEY_SELECTION_MAX, max);
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_MIN, min);

        return bundle;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        setTitle(getArguments().getString(KEY_TITLE));
        setNextButtonImage(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
        setNextButtonVisibility(false);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
        gameUseCase = (UseCaseIds) callback.getCurrentGameUseCase();
    }

    @Override
    public void showPlayers(final List<Player> players,
                            final int maxSelection) {

        super.showPlayers(players, maxSelection);
        getAdapter().setOnPlayerSelectListener(new SelectPlayerAdapter.OnPlayerSelectListener() {
            @Override
            public void onPlayerSelectionChanged(List<Player> selectedPlayers) {
                if (selectedPlayers.size() >= getArguments().getInt(KEY_MIN)) {
                    setNextButtonVisibility(true);
                } else {
                    setNextButtonVisibility(false);
                }
            }
        });
    }

    @Override
    public void playersSelected(final List<Player> players) {
        if (players.size() >= getArguments().getInt(KEY_MIN)) {
            final List<Long> ids = new ArrayList<>();
            for (Player player: players){
                ids.add(player.getId());
            }
            gameUseCase.onExecuteStep(flowDetails.getStep(), ids);
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
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }

}