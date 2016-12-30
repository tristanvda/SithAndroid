package com.grietenenknapen.sithandroid.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.SelectPlayerAdapter;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.PlayerSelectPresenter;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerSelectFragment extends CallbackPresenterFragment<PlayerSelectPresenter, PlayerSelectPresenter.View, PlayerSelectFragment.Callback> implements PlayerSelectPresenter.View {

    private static final String PRESENTER_TAG = "player_select_presenter";

    protected static final String KEY_SELECTION_MAX = "key:selection_max";
    protected static final String KEY_PLAYERS = "key:players";

    @BindView(R.id.listTitle)
    TextView titleTextView;
    @BindView(R.id.listRecyclerView)
    RecyclerView playerRecyclerView;
    @BindView(R.id.listButton)
    ImageButton nextButton;

    private SelectPlayerAdapter adapter;

    public static Bundle createArguments(final ArrayList<Player> players, final int maxSelection) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAYERS, players);
        bundle.putInt(KEY_SELECTION_MAX, maxSelection);
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
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME_OVER);
    }

    protected void initLayout() {
        titleTextView.setText(getString(R.string.select_players));
        nextButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_shuffle));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        playerRecyclerView.setLayoutManager(layoutManager);
        playerRecyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.card_margin);
        playerRecyclerView.addItemDecoration(itemDecoration);
    }

    protected void setTitle(String title) {
        titleTextView.setText(title);
    }

    protected void setNextButtonImage(Drawable drawable) {
        nextButton.setImageDrawable(drawable);
    }

    protected void setNextButtonVisibility(boolean visible) {
        nextButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected SelectPlayerAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<PlayerSelectPresenter> getPresenterFactory() {
        return new PlayerSelectPresenterFactory(getArguments().<Player>getParcelableArrayList(KEY_PLAYERS),
                getArguments().getInt(KEY_SELECTION_MAX));
    }

    @Override
    protected PlayerSelectPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void showPlayers(List<Player> players, int maxSelection) {
        adapter = new SelectPlayerAdapter(ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
        adapter.setMaxItemSelection(maxSelection);
        playerRecyclerView.setAdapter(adapter);
        adapter.setData(players, true);
    }

    @Override
    public void showSelectedPlayers(List<Player> selectedPlayers) {
        for (Player player : selectedPlayers) {
            adapter.selectPlayer(player);
        }
    }

    @Override
    public void playersSelected(List<Player> players) {
        if (callback != null) {
            callback.onPlayersSelected(players);
        }
    }

    public interface Callback extends GameFragmentCallback {
        void onPlayersSelected(List<Player> players);
    }

    @OnClick(R.id.listButton)
    public void onNextButtonClicked() {
        getPresenter().onNextClicked(adapter.getSelectedPositions());
    }

    private static class PlayerSelectPresenterFactory implements PresenterFactory<PlayerSelectPresenter> {
        private final List<Player> players;
        private final int maxSelection;

        private PlayerSelectPresenterFactory(List<Player> players, int maxSelection) {
            this.players = players;
            this.maxSelection = maxSelection;
        }

        @Override
        public PlayerSelectPresenter createPresenter() {
            return new PlayerSelectPresenter(players, maxSelection);
        }
    }
}
