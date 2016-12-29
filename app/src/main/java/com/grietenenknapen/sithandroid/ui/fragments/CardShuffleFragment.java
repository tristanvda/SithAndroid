package com.grietenenknapen.sithandroid.ui.fragments;

import android.content.Context;
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
import com.grietenenknapen.sithandroid.application.SithApplication;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.PresenterFragment;
import com.grietenenknapen.sithandroid.ui.adapters.PlayerCardAdapter;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.CardShufflePresenter;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardShuffleFragment extends CallbackPresenterFragment<CardShufflePresenter, CardShufflePresenter.View, CardShuffleFragment.Callback>
        implements CardShufflePresenter.View {
    private static final String PRESENTER_TAG = "card_shuffle_presenter";

    private static final String KEY_PLAYERS = "key:players";

    @BindView(R.id.listTitle)
    TextView titleTextView;
    @BindView(R.id.listRecyclerView)
    RecyclerView playerRecyclerView;
    @BindView(R.id.listButton)
    ImageButton nextButton;

    private PlayerCardAdapter adapter;

    public static Bundle createArguments(final ArrayList<Player> players) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAYERS, players);
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
        callback.setGameStatus(GameFlowPresenter.STATUS_SHUFFLE);
    }

    private void initLayout() {
        titleTextView.setText(getString(R.string.maz_kanata_choose_card));
        nextButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        playerRecyclerView.setLayoutManager(layoutManager);
        playerRecyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.card_margin);
        playerRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<CardShufflePresenter> getPresenterFactory() {
        return new PlayerSelectPresenterFactory(getArguments().<Player>getParcelableArrayList(KEY_PLAYERS),
                ((SithApplication) getContext().getApplicationContext()).getSithCardService());
    }

    @Override
    protected CardShufflePresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void showActivePlayers(List<ActivePlayer> activePlayers) {
        adapter = new PlayerCardAdapter(getActivity(), activePlayers, ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
        playerRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCardShuffled(List<ActivePlayer> activePlayers) {
        callback.onCardsShuffled(activePlayers);
    }

    @OnClick(R.id.listButton)
    public void listButtonClicked() {
        getPresenter().onNextClicked();
    }

    public interface Callback extends GameFragmentCallback {
        void onCardsShuffled(List<ActivePlayer> activePlayers);
    }

    private static class PlayerSelectPresenterFactory implements PresenterFactory<CardShufflePresenter> {
        private final List<Player> players;
        private final SithCardService sithCardService;

        public PlayerSelectPresenterFactory(List<Player> players, SithCardService sithCardService) {
            this.players = players;
            this.sithCardService = sithCardService;
        }

        @Override
        public CardShufflePresenter createPresenter() {
            return new CardShufflePresenter(sithCardService, players);
        }
    }
}
