package com.grietenenknapen.sithandroid.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.TitlePlayerCardAdapter;
import com.grietenenknapen.sithandroid.ui.adapters.managers.TitleGridLayoutManager;
import com.grietenenknapen.sithandroid.ui.adapters.TitleGridMergeAdapter;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.GamePlayersPresenter;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GamePlayersFragment extends CallbackPresenterFragment<GamePlayersPresenter, GamePlayersPresenter.View, GameFragmentCallback>
        implements GamePlayersPresenter.View {
    private static final String PRESENTER_TAG = "game_players_presenter";

    protected static final String KEY_PLAYERS_DEATH = "key:death_players";
    protected static final String KEY_PLAYERS_ALIVE = "key:alive_players";

    @BindView(R.id.listTitle)
    TextView titleTextView;
    @BindView(R.id.listRecyclerView)
    RecyclerView playerRecyclerView;
    @BindView(R.id.listButton)
    ImageButton nextButton;

    public static Bundle createArguments(final ArrayList<ActivePlayer> alivePlayers, final ArrayList<ActivePlayer> deathPlayers) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAYERS_ALIVE, alivePlayers);
        bundle.putParcelableArrayList(KEY_PLAYERS_DEATH, deathPlayers);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME_PLAYERS);
    }

    protected void initLayout() {
        titleTextView.setText(getString(R.string.players));
        titleTextView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);

        playerRecyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.card_margin);
        playerRecyclerView.addItemDecoration(itemDecoration);
    }


    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<GamePlayersPresenter> getPresenterFactory() {
        return new GamePlayersPresenterFactory(getArguments().<ActivePlayer>getParcelableArrayList(KEY_PLAYERS_ALIVE),
                getArguments().<ActivePlayer>getParcelableArrayList(KEY_PLAYERS_DEATH));
    }

    @Override
    protected GamePlayersPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void displayGamePlayers(final List<ActivePlayer> alivePlayers, final List<ActivePlayer> deathPlayers) {
        final TitleGridMergeAdapter mergeAdapter = new TitleGridMergeAdapter();

        if (deathPlayers.size() > 0) {
            TitlePlayerCardAdapter killAdapter = new TitlePlayerCardAdapter(getActivity(), deathPlayers, getString(R.string.killed_players),
                    ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
            mergeAdapter.addAdapter(killAdapter);
        }

        TitlePlayerCardAdapter aliveAdapter = new TitlePlayerCardAdapter(getActivity(), alivePlayers, getString(R.string.alive_players),
                ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
        mergeAdapter.addAdapter(aliveAdapter);

        RecyclerView.LayoutManager layoutManager = new TitleGridLayoutManager(getContext().getApplicationContext(), 2, mergeAdapter);
        playerRecyclerView.setLayoutManager(layoutManager);
        playerRecyclerView.setAdapter(mergeAdapter);
    }

    private static class GamePlayersPresenterFactory implements PresenterFactory<GamePlayersPresenter> {
        private final List<ActivePlayer> alivePlayers;
        private final List<ActivePlayer> deathPlayers;

        private GamePlayersPresenterFactory(List<ActivePlayer> alivePlayers, List<ActivePlayer> deathPlayers) {
            this.alivePlayers = alivePlayers;
            this.deathPlayers = deathPlayers;
        }

        @Override
        public GamePlayersPresenter createPresenter() {
            return new GamePlayersPresenter(alivePlayers, deathPlayers);
        }
    }
}
