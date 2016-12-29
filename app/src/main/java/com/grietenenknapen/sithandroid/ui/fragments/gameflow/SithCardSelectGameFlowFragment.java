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
import com.grietenenknapen.sithandroid.maingame.usecases.GameUseCaseCard;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.PresenterFragment;
import com.grietenenknapen.sithandroid.ui.adapters.CardAdapter;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.gameflow.SithCardSelectGameFlowPresenter;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SithCardSelectGameFlowFragment extends CallbackPresenterFragment<SithCardSelectGameFlowPresenter, SithCardSelectGameFlowPresenter.View, GameFragmentCallback>
        implements SithCardSelectGameFlowPresenter.View, GameFlowFragment<GameUseCaseCard> {

    private static final String PRESENTER_TAG = "sith_card_select_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";

    private static final String KEY_SITH_CARDS = "key:sith_cards";

    @BindView(R.id.listTitle)
    TextView titleTextView;
    @BindView(R.id.listRecyclerView)
    RecyclerView cardRecyclerView;
    @BindView(R.id.listButton)
    ImageButton nextButton;

    private CardAdapter adapter;
    private GameUseCaseCard gameUseCase;
    private FlowDetails flowDetails;


    public static SithCardSelectGameFlowFragment createInstance(final FlowDetails flowDetails,
                                                                final ArrayList<SithCard> sithCards) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelableArrayList(KEY_SITH_CARDS, sithCards);

        SithCardSelectGameFlowFragment fragment = new SithCardSelectGameFlowFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        getPresenter().setGameUseCase(gameUseCase);
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
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
    }

    private void initLayout() {
        titleTextView.setText(getString(R.string.han_solo_choose_card));
        nextButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        cardRecyclerView.setLayoutManager(layoutManager);
        cardRecyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.card_margin);
        cardRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void showSithCards(List<SithCard> sithCards) {
        adapter = new CardAdapter(getContext(), sithCards, ResourceUtils.getDefaultCardItemSize(getActivity().getWindowManager()));
        adapter.setMaxItemSelection(1);
        adapter.setOnCardSelectListener(new CardAdapter.OnCardSelectListener() {
            @Override
            public void onCardSelectionChanged(List<SithCard> selectedCards) {
                if (selectedCards.size() > 0) {
                    getPresenter().sithCardChosen(selectedCards.get(0));
                }
            }
        });
        cardRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showSelectedSithCard(SithCard selectedCard) {
        adapter.selectSithCard(selectedCard);
    }

    @Override
    public void enableNext() {
        nextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableNext() {
        nextButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.listButton)
    public void listButtonClicked() {
        getPresenter().onNextClicked();
    }

    @Override
    protected String getPresenterTag() {
        FlowDetails flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        if (flowDetails != null) {
            return PRESENTER_TAG + flowDetails.getRound() + flowDetails.getStep() + flowDetails.getTurn();
        }
        return PRESENTER_TAG;    }

    @Override
    protected PresenterFactory<SithCardSelectGameFlowPresenter> getPresenterFactory() {
        return new SithCardSelectGameFlowFragment.SithCardSelectGameFlowPresenterFactory((FlowDetails) getArguments().getParcelable(KEY_FLOW_DETAIL),
                getArguments().<SithCard>getParcelableArrayList(KEY_SITH_CARDS));
    }

    @Override
    protected SithCardSelectGameFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void setUseCase(GameUseCaseCard useCase) {
        this.gameUseCase = useCase;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }

    private static class SithCardSelectGameFlowPresenterFactory implements PresenterFactory<SithCardSelectGameFlowPresenter> {
        private final List<SithCard> sithCards;
        private final FlowDetails flowDetails;

        private SithCardSelectGameFlowPresenterFactory(final FlowDetails flowDetails,
                                                       final List<SithCard> sithCards) {
            this.sithCards = sithCards;
            this.flowDetails = flowDetails;
        }

        @Override
        public SithCardSelectGameFlowPresenter createPresenter() {
            return new SithCardSelectGameFlowPresenter(flowDetails, sithCards);
        }
    }
}
