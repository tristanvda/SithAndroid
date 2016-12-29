package com.grietenenknapen.sithandroid.ui.presenters.gameflow;

import android.os.Handler;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

public class SpeakGameFlowPresenter extends Presenter<SpeakGameFlowPresenter.View> {

    private int speakTextId;
    private int speakSoundId;
    private final FlowDetails flowDetails;
    private GameUseCase gameUseCase;
    private boolean soundPlayed;

    public SpeakGameFlowPresenter(FlowDetails flowDetails,
                                  final int speakSoundId, final
                                  int speakTextId) {
        this.speakSoundId = speakSoundId;
        this.speakTextId = speakTextId;
        this.flowDetails = flowDetails;
    }

    @Override
    protected void onViewBound() {
        getView().displaySpeakText(speakTextId);
        if (!soundPlayed) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getView() != null) {
                        getView().speak(speakSoundId);
                    }
                }
            }, 1000);
        } else {
            soundPlayDone();
        }
    }

    public void soundPlayDone() {
        gameUseCase.onExecuteStep(flowDetails.getStep());
    }

    public void setGameUseCase(GameUseCase gameUseCase) {
        this.gameUseCase = gameUseCase;
    }

    public interface View extends PresenterView {
        void speak(int speakSoundId);

        void displaySpeakText(int speakTextId);
    }
}
