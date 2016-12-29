package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.PresenterFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.gameflow.ShowPlayerYesNoGameFlowPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowPlayerYesNoGameFlowFragment extends CallbackPresenterFragment<ShowPlayerYesNoGameFlowPresenter, ShowPlayerYesNoGameFlowPresenter.View, GameFragmentCallback>
        implements ShowPlayerYesNoGameFlowPresenter.View, GameFlowFragment<GameUseCaseYesNoId> {

    private static final String PRESENTER_TAG = "show_player_game_flow_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";
    private static final String KEY_FLOW_ACTIVE_PLAYER = "key:active_player";
    private static final String KEY_TITLE = "key:title";
    private static final String KEY_FLOW_HIDE_YES = "key:hideYes";

    private GameUseCaseYesNoId gameUseCase;
    private FlowDetails flowDetails;

    @BindView(R.id.showPlayerTitle)
    TextView titleText;
    @BindView(R.id.playerCardName)
    TextView playerCardName;
    @BindView(R.id.menuYes)
    TextView buttonYes;

    public static ShowPlayerYesNoGameFlowFragment createInstance(final FlowDetails flowDetails,
                                                                 final ActivePlayer activePlayer,
                                                                 final String title,
                                                                 final boolean hideYes) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putParcelable(KEY_FLOW_ACTIVE_PLAYER, activePlayer);
        bundle.putBoolean(KEY_FLOW_HIDE_YES, hideYes);
        bundle.putString(KEY_TITLE, title);

        ShowPlayerYesNoGameFlowFragment fragment = new ShowPlayerYesNoGameFlowFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        getPresenter().setGameUseCaseYesNo(gameUseCase);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_show_player, container, false);
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
        titleText.setText(getArguments().getString(KEY_TITLE));
    }

    @Override
    public void displayPlayer(ActivePlayer activePlayer) {
        playerCardName.setText(activePlayer.getName());
    }

    @Override
    public void disableYesButton() {
        buttonYes.setVisibility(View.GONE);
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
    protected PresenterFactory<ShowPlayerYesNoGameFlowPresenter> getPresenterFactory() {
        return new ShowPlayerGameFlowPresenterFactory((FlowDetails) getArguments().getParcelable(KEY_FLOW_DETAIL),
                (ActivePlayer) getArguments().getParcelable(KEY_FLOW_ACTIVE_PLAYER),
                getArguments().getBoolean(KEY_FLOW_HIDE_YES));
    }

    @Override
    protected ShowPlayerYesNoGameFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void setUseCase(GameUseCaseYesNoId useCase) {
        this.gameUseCase = useCase;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }

    @OnClick(R.id.menuYes)
    public void onMenuYesClicked() {
        getPresenter().onAnswerClicked(true);
    }

    @OnClick(R.id.menuNo)
    public void onMenuNoClicked() {
        getPresenter().onAnswerClicked(false);
    }


    private static class ShowPlayerGameFlowPresenterFactory implements PresenterFactory<ShowPlayerYesNoGameFlowPresenter> {
        private final FlowDetails flowDetails;
        private final ActivePlayer activePlayer;
        private final boolean hideYes;

        private ShowPlayerGameFlowPresenterFactory(FlowDetails flowDetails, ActivePlayer activePlayer, boolean hideYes) {
            this.flowDetails = flowDetails;
            this.activePlayer = activePlayer;
            this.hideYes = hideYes;
        }

        @Override
        public ShowPlayerYesNoGameFlowPresenter createPresenter() {
            return new ShowPlayerYesNoGameFlowPresenter(flowDetails, activePlayer, hideYes);
        }
    }
}
