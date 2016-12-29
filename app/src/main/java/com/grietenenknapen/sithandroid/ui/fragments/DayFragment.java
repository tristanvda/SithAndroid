package com.grietenenknapen.sithandroid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.Game;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.PresenterFragment;
import com.grietenenknapen.sithandroid.ui.adapters.TitlePlayerCardAdapter;
import com.grietenenknapen.sithandroid.ui.adapters.managers.TitleGridLayoutManager;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.DayPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DayFragment extends CallbackPresenterFragment<DayPresenter, DayPresenter.View, DayFragment.CallBack> implements DayPresenter.View {
    private static final String PRESENTER_TAG = "day_presenter";
    private static final String KEY_GAME = "key:game";

    @BindView(R.id.dayButtonStart)
    ImageButton dayButtoStart;
    @BindView(R.id.dayButtonKill)
    ImageButton dayButtonKill;
    @BindView(R.id.ripSection)
    View ripSectionView;
    @BindView(R.id.ripRecyclerView)
    RecyclerView ripRecyclerView;

    public static Bundle createArguments(final MainGame game) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_GAME, game);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_day, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
    }

    private void initLayout() {
        ripRecyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.card_margin);
        ripRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<DayPresenter> getPresenterFactory() {
        return new PlayerSelectPresenterFactory((MainGame) getArguments().getParcelable(KEY_GAME));
    }

    @Override
    protected DayPresenter.View getPresenterView() {
        return this;
    }

    @OnClick(R.id.dayButtonStart)
    public void onDayButtonStartClicked() {
        getPresenter().onDayButtonStartClicked();
    }

    @OnClick(R.id.dayButtonKill)
    public void onDayButtonKillClicked() {
        getPresenter().onDayButtonKillClicked();
    }

    @OnClick(R.id.dayButtonPlayers)
    public void onDayButtonPlayersClicked() {
        getPresenter().onDayButtonPlayersClicked();
    }

    @Override
    public void goToNightStart() {
        callback.onStartNight();
    }

    @Override
    public void goToKillPlayer(List<ActivePlayer> alivePlayers) {
        callback.onKillPlayerSelected(alivePlayers);
    }

    @Override
    public void showRipPlayersList(List<ActivePlayer> killedPlayers) {
        ripSectionView.setVisibility(View.VISIBLE);
        TitlePlayerCardAdapter adapter = new TitlePlayerCardAdapter(getActivity(), killedPlayers, getString(R.string.killed_players_night),
                ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
        RecyclerView.LayoutManager layoutManager = new TitleGridLayoutManager(getContext().getApplicationContext(), 2, adapter);
        ripRecyclerView.setLayoutManager(layoutManager);
        ripRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void goToGamePlayers(List<ActivePlayer> alivePlayers,
                                List<ActivePlayer> killedPlayers) {
        callback.onGamePlayersSelected(alivePlayers, killedPlayers);
    }

    @Override
    public void hideRipPlayersList() {
        ripSectionView.setVisibility(View.GONE);
        ripRecyclerView.setAdapter(null);
    }

    public interface CallBack extends GameFragmentCallback {
        void onStartNight();

        void onKillPlayerSelected(List<ActivePlayer> activePlayers);

        void onGamePlayersSelected(List<ActivePlayer> alivePlayers, List<ActivePlayer> killedPlayers);

    }

    private static class PlayerSelectPresenterFactory implements PresenterFactory<DayPresenter> {
        private final MainGame game;

        private PlayerSelectPresenterFactory(MainGame game) {
            this.game = game;
        }

        @Override
        public DayPresenter createPresenter() {
            return new DayPresenter(game);
        }
    }

}
