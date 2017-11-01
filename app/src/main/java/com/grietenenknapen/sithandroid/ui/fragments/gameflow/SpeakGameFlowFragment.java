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
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.gameflow.SpeakGameFlowPresenter;
import com.grietenenknapen.sithandroid.util.FontCache;
import com.grietenenknapen.sithandroid.util.MediaSoundPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakGameFlowFragment extends CallbackPresenterFragment<SpeakGameFlowPresenter, SpeakGameFlowPresenter.View, GameFlowActivity>
        implements SpeakGameFlowPresenter.View, GameFlowFragment {
    private static final String PRESENTER_TAG = "speak_game_flow_presenter";
    private static final String KEY_FLOW_DETAIL = "key:flow_details";
    private static final String KEY_FLOW_SPEAK_TEXT_ID = "key:flow_speak_text_id";
    private static final String KEY_FLOW_SPEAK_SOUND_ID = "key:flow_speak_sound_id";

    private FlowDetails flowDetails;
    private UseCase gameUseCase;

    @BindView(R.id.speakText)
    TextView speakText;

    public static Bundle createStartBundle(final FlowDetails flowDetails,
                                           final int speakSoundId,
                                           final int speakTextId) {

        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FLOW_DETAIL, flowDetails);
        bundle.putInt(KEY_FLOW_SPEAK_SOUND_ID, speakSoundId);
        bundle.putInt(KEY_FLOW_SPEAK_TEXT_ID, speakTextId);

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
        final View v = inflater.inflate(R.layout.fragment_speak, container, false);
        return v;
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
        gameUseCase = callback.getCurrentGameUseCase();
        getPresenter().setGameUseCase(gameUseCase);
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaSoundPlayer.setMediaSoundPlayListener(null, PRESENTER_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaSoundPlayer.setMediaSoundPlayListener(new MediaSoundPlayer.OnSoundPlayListener() {
            @Override
            public void onSoundPlayDone() {
                getPresenter().soundPlayDone();
            }
        }, PRESENTER_TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaSoundPlayer.stopPlayer(PRESENTER_TAG);
    }

    private void initLayout() {
        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", getContext());

        speakText.setTypeface(starWars);
    }

    @Override
    public void speak(int speakSoundId) {
        MediaSoundPlayer.playSoundFile(getContext(), speakSoundId, PRESENTER_TAG);
    }

    @Override
    public void displaySpeakText(int speakTextId) {
        speakText.setText(getString(speakTextId));
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
    protected PresenterFactory<SpeakGameFlowPresenter> getPresenterFactory() {
        return new SpeakFlowPresenterFactory((FlowDetails) getArguments().getParcelable(KEY_FLOW_DETAIL),
                getArguments().getInt(KEY_FLOW_SPEAK_SOUND_ID), getArguments().getInt(KEY_FLOW_SPEAK_TEXT_ID));
    }

    @Override
    protected SpeakGameFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public boolean isNewTask(FlowDetails flowDetails) {
        return !this.flowDetails.equals(flowDetails);
    }

    private static class SpeakFlowPresenterFactory implements PresenterFactory<SpeakGameFlowPresenter> {
        private final int speakTextResId;
        private final int speakSoundResId;
        private final FlowDetails flowDetails;

        private SpeakFlowPresenterFactory(FlowDetails flowDetails,
                                          int speakSoundResId,
                                          int speakTextResId) {
            this.speakTextResId = speakTextResId;
            this.speakSoundResId = speakSoundResId;
            this.flowDetails = flowDetails;
        }

        @Override
        public SpeakGameFlowPresenter createPresenter() {
            return new SpeakGameFlowPresenter(flowDetails, speakSoundResId, speakTextResId);
        }
    }
}
