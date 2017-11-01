package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

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
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.activities.MainGameFlowActivity;
import com.grietenenknapen.sithandroid.ui.adapters.PlayerCardAdapter;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.gameflow.UserCardPeekGameFlowPresenter;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCardPeekGameFlowFragment extends CallbackPresenterFragment<UserCardPeekGameFlowPresenter, UserCardPeekGameFlowPresenter.View, GameFlowActivity>
        implements UserCardPeekGameFlowPresenter.View, GameFlowFragment {
    private static final String PRESENTER_TAG = "user_card_peek_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";

    private static final String KEY_PLAYERS = "key:players";

    @BindView(R.id.listTitle)
    TextView titleTextView;
    @BindView(R.id.listRecyclerView)
    RecyclerView playerRecyclerView;
    @BindView(R.id.listButton)
    ImageButton nextButton;

    private PlayerCardAdapter adapter;
    private FlowDetails flowDetails;

    public static Bundle createStartBundle(final FlowDetails flowDetails,
                                           final List<ActivePlayer> activePlayers) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelableArrayList(KEY_PLAYERS, new ArrayList<>(activePlayers));

        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
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
        final UseCase gameUseCase = callback.getCurrentGameUseCase();
        getPresenter().setGameUseCase(gameUseCase);
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
    public void showActivePlayers(List<ActivePlayer> activePlayers) {
        adapter = new PlayerCardAdapter(activePlayers, ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
        adapter.setOnCardSelectListener(new PlayerCardAdapter.OnCardSelectListener() {
            @Override
            public void onCardSelected(ActivePlayer activePlayer) {
                getPresenter().activePlayerChosen(activePlayer);
            }
        });
        playerRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showSelectedPlayer(ActivePlayer activePlayer) {
        adapter.flipActivePlayer(activePlayer);
    }

    @Override
    public void disableList() {
        adapter.setDisableClick(true);
    }

    @Override
    public void enableNext() {
        nextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableNext() {
        nextButton.setVisibility(View.GONE);
    }

    @Override
    protected String getPresenterTag() {
        FlowDetails flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        if (flowDetails != null) {
            return PRESENTER_TAG + flowDetails.getRound() + flowDetails.getStep() + flowDetails.getTurn();
        }
        return PRESENTER_TAG;
    }

    @OnClick(R.id.listButton)
    public void listButtonClicked() {
        getPresenter().onNextClicked();
    }

    @Override
    protected PresenterFactory<UserCardPeekGameFlowPresenter> getPresenterFactory() {
        return new UserCardPeekGameFlowPresenterFactory((FlowDetails) getArguments().getParcelable(KEY_FLOW_DETAIL),
                getArguments().<ActivePlayer>getParcelableArrayList(KEY_PLAYERS));
    }

    @Override
    protected UserCardPeekGameFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }

    private static class UserCardPeekGameFlowPresenterFactory implements PresenterFactory<UserCardPeekGameFlowPresenter> {
        private final List<ActivePlayer> activePlayers;
        private final FlowDetails flowDetails;

        private UserCardPeekGameFlowPresenterFactory(final FlowDetails flowDetails,
                                                     final List<ActivePlayer> activePlayers) {
            this.activePlayers = activePlayers;
            this.flowDetails = flowDetails;
        }

        @Override
        public UserCardPeekGameFlowPresenter createPresenter() {
            return new UserCardPeekGameFlowPresenter(flowDetails, activePlayers);
        }
    }
}
