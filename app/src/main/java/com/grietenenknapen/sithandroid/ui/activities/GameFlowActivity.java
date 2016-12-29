package com.grietenenknapen.sithandroid.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.application.Settings;
import com.grietenenknapen.sithandroid.application.SithApplication;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.GameUseCaseYesNoId;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.maingame.usecases.GameUseCaseCard;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.fragments.CardShuffleFragment;
import com.grietenenknapen.sithandroid.ui.fragments.DayFragment;
import com.grietenenknapen.sithandroid.ui.fragments.GameOverFragment;
import com.grietenenknapen.sithandroid.ui.fragments.GamePlayersFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerKillFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.DelayGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SelectPLayerBobaGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SelectPlayerPairGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SelectPlayerSingleGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.ShowPlayerYesNoGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SithCardSelectGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SpeakGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.UserCardPeekGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.YesNoGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.ui.PresenterActivity;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.util.ActivityUtils;
import com.grietenenknapen.sithandroid.util.FragmentUtils;
import com.grietenenknapen.sithandroid.util.MediaSoundPlayer;
import com.grietenenknapen.sithandroid.util.ResourceUtils;
import com.grietenenknapen.sithandroid.util.SithMusicPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameFlowActivity extends PresenterActivity<GameFlowPresenter, GameFlowPresenter.View> implements
        GameFlowPresenter.View, CardShuffleFragment.Callback, PlayerSelectFragment.Callback,
        DayFragment.CallBack, GameOverFragment.Callback,
        GameFragmentCallback {
    private static final String PRESENTER_TAG = "game_flow_presenter";

    @BindView(R.id.activityLayout)
    RelativeLayout activityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaSoundPlayer.stopPlayer(PRESENTER_TAG);
        MediaSoundPlayer.setMediaSoundPlayListener(null, PRESENTER_TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaSoundPlayer.setMediaSoundPlayListener(new MediaSoundPlayer.OnSoundPlayListener() {
            @Override
            public void onSoundPlayDone() {
                getPresenter().onSpeakDone();
            }
        }, PRESENTER_TAG);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MediaSoundPlayer.stopPlayer(PRESENTER_TAG);
    }

    @Override
    public void setGameStatus(@GameFlowPresenter.GameStatus int gameStatus) {
        presenter.setStatus(gameStatus);
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<GameFlowPresenter> getPresenterFactory() {
        final MainGame mainGame;

        if (Settings.isMainGameSaved(this)) {
            mainGame = Settings.getSavedMainGame(this);
        } else {
            mainGame = null;
        }

        return new GameFlowPresenterFactory(((SithApplication) getApplicationContext()).getPlayerService(),
                ((SithApplication) getApplicationContext()).getSithCardService(), mainGame);
    }

    @Override
    protected GameFlowPresenter.View getPresenterView() {
        return this;
    }

    private <T extends GameFlowFragment> boolean isNewTask(FlowDetails flowDetails, Fragment fragment, Class<T> tClass) {
        return fragment == null
                || !(tClass.isInstance(fragment))
                || !fragment.isRemoving()
                || ((GameFlowFragment) fragment).isNewTask(flowDetails);
    }

    private int getFragmentAnimation(Fragment oldFragment) {
        if (oldFragment != null && oldFragment instanceof DayFragment) {
            return FragmentUtils.ANIMATE_SLIDE_DOWN;
        } else {
            return FragmentUtils.ANIMATE_SLIDE_LEFT;
        }
    }

    @Override
    public void goToDelayScreen(long delay, GameUseCase gameUseCase, FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, DelayGameFlowFragment.class)) {
            Fragment newFragment = DelayGameFlowFragment.createInstance(flowDetails, delay);
            ((DelayGameFlowFragment) newFragment).setUseCase(gameUseCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((DelayGameFlowFragment) fragment).setUseCase(gameUseCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToYesNoScreen(final boolean disableYes, final GameUseCaseYesNoId useCase, final FlowDetails flowDetails, final int titleResId) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, YesNoGameFlowFragment.class)) {
            Fragment newFragment = YesNoGameFlowFragment.createInstance(flowDetails, disableYes, getString(titleResId));
            ((YesNoGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((YesNoGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToKilledPlayerYesNoScreen(final ActivePlayer activePlayer, final boolean disableYes, final int titleResId, final GameUseCaseYesNoId useCase, final FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, ShowPlayerYesNoGameFlowFragment.class)) {
            Fragment newFragment = ShowPlayerYesNoGameFlowFragment.createInstance(flowDetails, activePlayer, getString(titleResId), disableYes);
            ((ShowPlayerYesNoGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((ShowPlayerYesNoGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToUserPlayerSelectionScreen(final List<Player> activePlayers, final GameUseCaseId useCase, final FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, SelectPlayerSingleGameFlowFragment.class)) {
            Fragment newFragment = SelectPlayerSingleGameFlowFragment.createInstance(flowDetails, (ArrayList<Player>) activePlayers);
            ((SelectPlayerSingleGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((SelectPlayerSingleGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToUserPlayerSelectionScreen(List<Player> activePlayers, GameUseCaseYesNoId useCase, FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, SelectPLayerBobaGameFlowFragment.class)) {
            Fragment newFragment = SelectPLayerBobaGameFlowFragment.createInstance(flowDetails, (ArrayList<Player>) activePlayers);
            ((SelectPLayerBobaGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((SelectPLayerBobaGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToUserCardSelectionScreen(List<SithCard> availableSithCards, GameUseCaseCard useCase, FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, SithCardSelectGameFlowFragment.class)) {
            Fragment newFragment = SithCardSelectGameFlowFragment.createInstance(flowDetails, (ArrayList<SithCard>) availableSithCards);
            ((SithCardSelectGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((SithCardSelectGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToUserCardPeekScreen(List<ActivePlayer> activePlayers, long delay, GameUseCase useCase, FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, UserCardPeekGameFlowFragment.class)) {
            Fragment newFragment = UserCardPeekGameFlowFragment.createInstance(flowDetails, (ArrayList<ActivePlayer>) activePlayers);
            ((UserCardPeekGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((UserCardPeekGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goToSpeakScreen(int soundResId, int stringResId, GameUseCase useCase, FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, SpeakGameFlowFragment.class)) {
            Fragment newFragment = SpeakGameFlowFragment.createInstance(flowDetails, soundResId, stringResId);
            ((SpeakGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((SpeakGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void goTotUserPairPlayerSelection(List<Player> players, GameUseCasePairId useCase, FlowDetails flowDetails) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, SpeakGameFlowFragment.class)) {
            Fragment newFragment = SelectPlayerPairGameFlowFragment.createInstance(flowDetails, (ArrayList<Player>) players);
            ((SelectPlayerPairGameFlowFragment) newFragment).setUseCase(useCase);
            FragmentUtils.setAnimation(ft, getFragmentAnimation(fragment));
            ft.replace(R.id.container, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((SelectPlayerPairGameFlowFragment) fragment).setUseCase(useCase);
                ft.attach(fragment).commit();
            }
        }
    }

    @Override
    public void showSelectPlayersScreen(List<Player> players) {
        Bundle bundle = PlayerSelectFragment.createArguments(new ArrayList<>(players), 30);
        FragmentUtils.replaceOrAddFragment(this, PlayerSelectFragment.class, R.id.container,
                PlayerSelectFragment.class.getName(), FragmentUtils.ANIMATE_NONE, bundle, false);
    }

    @Override
    public void showKillPlayersScreen(List<Player> activePlayers) {
        Bundle bundle = PlayerKillFragment.createArguments(new ArrayList<>(activePlayers));
        FragmentUtils.replaceOrAddFragment(this, PlayerKillFragment.class, R.id.container,
                PlayerKillFragment.class.getName(), FragmentUtils.ANIMATE_SLIDE_LEFT, bundle, true);
    }

    @Override
    public void goToShuffleScreen(List<Player> players) {
        final int animation = getSupportFragmentManager().findFragmentById(R.id.container) != null ?
                FragmentUtils.ANIMATE_SLIDE_LEFT : FragmentUtils.ANIMATE_NONE;
        Bundle bundle = CardShuffleFragment.createArguments(new ArrayList<>(players));
        FragmentUtils.replaceOrAddFragment(this, CardShuffleFragment.class, R.id.container,
                CardShuffleFragment.class.getName(), animation, bundle, true);
    }

    @Override
    public void goToDayScreen(MainGame game) {
        //FragmentUtils.clearFragmentBackStack(getSupportFragmentManager());

        final int animation = getSupportFragmentManager().findFragmentById(R.id.container) != null ?
                FragmentUtils.ANIMATE_SLIDE_UP : FragmentUtils.ANIMATE_NONE;
        Bundle bundle = DayFragment.createArguments(game);
        FragmentUtils.replaceOrAddFragment(this, DayFragment.class, R.id.container,
                DayFragment.class.getName(), animation, bundle, false);
    }

    @Override
    public void speak(String soundResStringId) {
        int resId = ResourceUtils.getResIdFromResString(this, soundResStringId, ResourceUtils.RES_TYPE_RAW);
        if (resId != 0) {
            MediaSoundPlayer.playSoundFile(this, resId, PRESENTER_TAG);
        } else {
            getPresenter().onSpeakDone();
        }
    }

    @Override
    public void showExitDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.label_warning)
                .content(R.string.game_quit_warning)
                .positiveText(android.R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        getPresenter().quitGame();
                    }
                })
                .negativeText(android.R.string.no)
                .show();
    }

    @Override
    public void showGameOver(List<String> players) {
        FragmentUtils.replaceOrAddFragment(this, GameOverFragment.class, R.id.container,
                GameOverFragment.class.getName(), FragmentUtils.ANIMATE_SLIDE_LEFT,
                GameOverFragment.createArguments((ArrayList<String>) players), false);
    }

    @Override
    public void showError(int stringId) {
        ActivityUtils.showSnackBar(activityLayout, getString(stringId));
    }

    @Override
    public void closeScreen() {
        this.finish();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void saveGame(MainGame mainGame) {
        Settings.setSavedMainGame(this, mainGame);
        Settings.setMainGameSaved(this, true);
    }

    @Override
    public void deleteSavedGame() {
        Settings.setSavedMainGame(this, null);
        Settings.setMainGameSaved(this, false);
    }

    @Override
    public void playMusic(int musicType) {
        SithMusicPlayer.playMusic(this, musicType);
    }

    @Override
    public void stopPlayingMusic() {
        SithMusicPlayer.stopPlaying();
    }

    @Override
    public void onBackPressed() {
        if (!getPresenter().onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onPlayersSelected(List<Player> players) {
        getPresenter().onPlayerSelected(players);
    }

    @Override
    public void onCardsShuffled(List<ActivePlayer> activePlayers) {
        getPresenter().onCardsShuffled(activePlayers);
    }

    @Override
    public void onStartNight() {
        presenter.startNight();
    }

    @Override
    public void onKillPlayerSelected(List<ActivePlayer> activePlayers) {
        presenter.killPlayerSelected(activePlayers);
    }

    @Override
    public void onGamePlayersSelected(List<ActivePlayer> alivePlayers, List<ActivePlayer> killedPlayers) {
        FragmentUtils.replaceOrAddFragment(this, GamePlayersFragment.class, R.id.container,
                GamePlayersFragment.class.getName(), FragmentUtils.ANIMATE_SLIDE_LEFT,
                GamePlayersFragment.createArguments((ArrayList<ActivePlayer>) alivePlayers,
                        (ArrayList<ActivePlayer>) killedPlayers), true);

    }

    @Override
    public void closeGame() {
        this.finish();
    }

    private static class GameFlowPresenterFactory implements PresenterFactory<GameFlowPresenter> {
        private final PlayerService playerService;
        private final SithCardService sithCardService;
        private final MainGame mainGame;

        public GameFlowPresenterFactory(PlayerService playerService,
                                        SithCardService sithCardService,
                                        MainGame mainGame) {
            this.playerService = playerService;
            this.sithCardService = sithCardService;
            this.mainGame = mainGame;
        }

        @Override
        public GameFlowPresenter createPresenter() {
            return new GameFlowPresenter(playerService, sithCardService, mainGame);
        }
    }
}
