package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseYesNo;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.gameflow.YesNoGameFlowPresenter;
import com.grietenenknapen.sithandroid.util.FontCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YesNoGameFlowFragment extends CallbackPresenterFragment<YesNoGameFlowPresenter, YesNoGameFlowPresenter.View, GameFragmentCallback>
        implements YesNoGameFlowPresenter.View, GameFlowFragment<UseCaseYesNo> {

    private static final String PRESENTER_TAG = "yes_no_id_flow_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";
    private static final String KEY_TITLE = "key:title";
    private static final String KEY_FLOW_HIDE_YES = "key:hideYes";

    private FlowDetails flowDetails;
    private UseCaseYesNo gameUseCaseYesNoId;

    @BindView(R.id.menuYes)
    TextView menuYes;
    @BindView(R.id.menuNo)
    TextView menuNo;
    @BindView(R.id.yesNoTitle)
    TextView title;

    public static Bundle createStartBundle(final FlowDetails flowDetails,
                                           final boolean hideYes,
                                           final String title) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putBoolean(KEY_FLOW_HIDE_YES, hideYes);
        bundle.putString(KEY_TITLE, title);

        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowDetails = getArguments().getParcelable(KEY_FLOW_DETAIL);
        getPresenter().setGameUseCaseYesNo(gameUseCaseYesNoId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_yes_no, container, false);
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME);
    }

    private void initLayout() {
        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", getContext());

        menuYes.setTypeface(starWars);
        menuNo.setTypeface(starWars);
        title.setText(getArguments().getString(KEY_TITLE));
    }

    @Override
    public void disableYesButton() {
        menuYes.setVisibility(View.GONE);
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
    protected PresenterFactory<YesNoGameFlowPresenter> getPresenterFactory() {
        return new YesNoGameFlowPresenterFactory((FlowDetails) getArguments().getParcelable(KEY_FLOW_DETAIL),
                getArguments().getBoolean(KEY_FLOW_HIDE_YES));
    }

    @Override
    protected YesNoGameFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void setUseCase(UseCaseYesNo useCase) {
        this.gameUseCaseYesNoId = useCase;
    }

    @Override
    public boolean isNewTask(final FlowDetails flowDetails) {
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

    private static class YesNoGameFlowPresenterFactory implements PresenterFactory<YesNoGameFlowPresenter> {
        private final boolean hideYes;
        private final FlowDetails flowDetails;

        private YesNoGameFlowPresenterFactory(final FlowDetails flowDetails, final boolean hideYes) {
            this.hideYes = hideYes;
            this.flowDetails = flowDetails;
        }

        @Override
        public YesNoGameFlowPresenter createPresenter() {
            return new YesNoGameFlowPresenter(flowDetails, hideYes);
        }
    }

}
