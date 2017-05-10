package com.grietenenknapen.sithandroid.ui.fragments.gameflow;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.PresenterFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.gameflow.DelayGameFlowPresenter;
import com.grietenenknapen.sithandroid.util.FontCache;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelayGameFlowFragment extends CallbackPresenterFragment<DelayGameFlowPresenter, DelayGameFlowPresenter.View, GameFragmentCallback>
        implements DelayGameFlowPresenter.View, GameFlowFragment<GameUseCase> {
    private static final int STATIC_SECOND = 1000;

    private static final String PRESENTER_TAG = "delay_game_flow_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";
    private static final String KEY_FLOW_DELAY = "key:flow_delay";

    private FlowDetails flowDetails;
    private GameUseCase gameUseCase;
    private CountDownTimer countDownTimer;

    @BindView(R.id.delayText)
    TextView delayText;

    public static Bundle createStartBundle(final FlowDetails flowDetails,
                                           final long delay) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putLong(KEY_FLOW_DELAY, delay);

        return bundle;
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
        final View v = inflater.inflate(R.layout.fragment_delay, container, false);
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
        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", getContext());

        delayText.setTypeface(starWars);
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    public void updateTimerText(String delay) {
        delayText.setText(delay);
    }

    @Override
    public void startTimer(final long delay) {
        countDownTimer = new CountDownTimer(delay + STATIC_SECOND, STATIC_SECOND) {

            public void onTick(long millisUntilFinished) {
                getPresenter().updateTimer((int) (millisUntilFinished / STATIC_SECOND));
            }

            public void onFinish() {
                getPresenter().onTimerStop();
            }
        }.start();
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
    protected PresenterFactory<DelayGameFlowPresenter> getPresenterFactory() {
        return new DelayGameFlowPresenterFactory((FlowDetails) getArguments().getParcelable(KEY_FLOW_DETAIL),
                getArguments().getLong(KEY_FLOW_DELAY));
    }

    @Override
    protected DelayGameFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void setUseCase(GameUseCase useCase) {
        this.gameUseCase = useCase;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }

    private static class DelayGameFlowPresenterFactory implements PresenterFactory<DelayGameFlowPresenter> {
        private final long delay;
        private final FlowDetails flowDetails;

        private DelayGameFlowPresenterFactory(FlowDetails flowDetails, long delay) {
            this.delay = delay;
            this.flowDetails = flowDetails;
        }

        @Override
        public DelayGameFlowPresenter createPresenter() {
            return new DelayGameFlowPresenter(flowDetails, delay);
        }
    }
}
