package com.grietenenknapen.sithandroid.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.clans.fab.FloatingActionMenu;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.TitlePlayerCardAdapter;
import com.grietenenknapen.sithandroid.ui.adapters.managers.TitleGridLayoutManager;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFlowActivity;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.DayPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.views.AnimateDrawable;
import com.grietenenknapen.sithandroid.util.BitmapUtil;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DayFragment extends CallbackPresenterFragment<DayPresenter, DayPresenter.View, DayFragment.CallBack>
        implements DayPresenter.View, BackPressFragment {

    private static final String PRESENTER_TAG = "day_presenter";
    private static final String KEY_GAME = "key:game";

    @BindView(R.id.ripSection)
    View ripSectionView;
    @BindView(R.id.ripRecyclerView)
    RecyclerView ripRecyclerView;
    @BindView(R.id.fab)
    FloatingActionMenu fab;
    @BindView(R.id.dayButtonServer)
    ImageButton buttonServer;

    private AnimateDrawable animateDrawable;

    public static Bundle createArguments(final MainGame game) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_GAME, game);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
        Pair<Boolean, Boolean> serverStatus = callback.getWifiServerState();
        getPresenter().updateWifiServerState(serverStatus.first, serverStatus.second);
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.close(false);
    }

    private void initLayout() {
        ripRecyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.card_margin);
        ripRecyclerView.addItemDecoration(itemDecoration);
        animateDrawable = new AnimateDrawable(getActivity(),
                new Integer[]{R.drawable.ic_wifi_1, R.drawable.ic_wifi_2, R.drawable.ic_wifi_3, R.drawable.ic_wifi_4}, 3,
                new BitmapUtil.PixelColorReplaceable[]{new BitmapUtil.PixelColorReplaceable(Color.WHITE,
                        ContextCompat.getColor(getContext(), R.color.yellow))});
        animateDrawable.setCallback(buttonServer);
        buttonServer.setImageDrawable(animateDrawable);
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

    @OnClick(R.id.dayButtonServer)
    public void onDayButtonServerClicked() {
        getPresenter().onDayButtonServerClicked();
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
        TitlePlayerCardAdapter adapter = new TitlePlayerCardAdapter(killedPlayers, getString(R.string.killed_players_night),
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

    @Override
    public void initiateStartServer() {
        callback.onServerButtonClicked();
    }

    @Override
    public boolean onBackPressed() {
        if (fab.isOpened()) {
            fab.close(true);
            return true;
        }
        return false;
    }

    public void startWifiDirectLoading() {
        getPresenter().setServerLoading(true);
    }

    public void stopWifiDirectLoading() {
        getPresenter().setServerLoading(false);
    }

    public void updateWifiServerState(final boolean p2pEnabled, final boolean isServerRunning) {
        getPresenter().updateWifiServerState(p2pEnabled, isServerRunning);
    }

    @Override
    public void setServerButtonEnabled(final boolean enabled) {
        buttonServer.setEnabled(enabled);
        if (!enabled) {
            animateDrawable.stop();
        }
    }

    @Override
    public void setServerButtonLoading() {
        animateDrawable.start();
    }

    @Override
    public void setServerButtonChecked(final boolean checked) {
        animateDrawable.stop();
        animateDrawable.setState(checked ? 3 : 0);
    }

    public interface CallBack extends GameFlowActivity {
        void onStartNight();

        void onKillPlayerSelected(List<ActivePlayer> activePlayers);

        void onGamePlayersSelected(List<ActivePlayer> alivePlayers, List<ActivePlayer> killedPlayers);

        void onServerButtonClicked();

        Pair<Boolean, Boolean> getWifiServerState();

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
