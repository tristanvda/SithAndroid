package com.grietenenknapen.sithandroid.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.application.Settings;
import com.grietenenknapen.sithandroid.application.SithApplication;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.usecasetemplate.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.fragments.CardShuffleFragment;
import com.grietenenknapen.sithandroid.ui.fragments.DayFragment;
import com.grietenenknapen.sithandroid.ui.fragments.GameOverFragment;
import com.grietenenknapen.sithandroid.ui.fragments.GamePlayersFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerKillFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.DelayGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
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
import com.grietenenknapen.sithandroid.util.SMSUtils;
import com.grietenenknapen.sithandroid.util.SithMusicPlayer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameFlowActivity extends PresenterActivity<GameFlowPresenter, GameFlowPresenter.View> implements
        GameFlowPresenter.View, CardShuffleFragment.Callback, PlayerSelectFragment.Callback,
        DayFragment.CallBack, GameOverFragment.Callback,
        GameFragmentCallback {
    private static final String PRESENTER_TAG = "game_flow_presenter";
    private static final String RANDOM_COMMENT_PREFIX = "random_comment";

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
    protected void onDestroy() {
        super.onDestroy();
        SithMusicPlayer.stopPlaying();
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

        if (Settings.isRandomComments(this)) {
            return new GameFlowPresenterFactory(((SithApplication) getApplicationContext()).getPlayerService(),
                    ((SithApplication) getApplicationContext()).getSithCardService(), mainGame, getRandomResourceList());
        } else {
            return new GameFlowPresenterFactory(((SithApplication) getApplicationContext()).getPlayerService(),
                    ((SithApplication) getApplicationContext()).getSithCardService(), mainGame);
        }
    }

    @Override
    protected GameFlowPresenter.View getPresenterView() {
        return this;
    }


    private int getFragmentAnimation() {
        final Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (oldFragment != null && oldFragment instanceof DayFragment) {
            return FragmentUtils.Animation.ANIMATE_SLIDE_DOWN;
        } else {
            return FragmentUtils.Animation.ANIMATE_SLIDE_LEFT;
        }
    }

    @Override
    public void goToDelayScreen(final long delay, final GameUseCase gameUseCase, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, DelayGameFlowFragment.class, R.id.container,
                DelayGameFlowFragment.createStartBundle(flowDetails, delay), flowDetails, gameUseCase, getFragmentAnimation());
    }

    @Override
    public void goToYesNoScreen(final boolean disableYes, final UseCaseYesNo gameUseCase,
                                final FlowDetails flowDetails, final int titleResId) {

        FragmentUtils.handleGameFlowFragmentTransaction(this, YesNoGameFlowFragment.class, R.id.container,
                YesNoGameFlowFragment.createStartBundle(flowDetails, disableYes, getString(titleResId)), flowDetails, gameUseCase, getFragmentAnimation());
    }

    @Override
    public void goToPlayerYesNoScreen(final ActivePlayer activePlayer, final boolean disableYes,
                                            final int titleResId, final UseCaseYesNo gameUseCase, final FlowDetails flowDetails) {

        FragmentUtils.handleGameFlowFragmentTransaction(this, ShowPlayerYesNoGameFlowFragment.class, R.id.container,
                ShowPlayerYesNoGameFlowFragment.createStartBundle(flowDetails, activePlayer, getString(titleResId), disableYes), flowDetails, gameUseCase, getFragmentAnimation());
    }

    @Override
    public void goToUserPlayerSelectionScreen(final List<Player> activePlayers, final UseCaseId useCase, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SelectPlayerSingleGameFlowFragment.class, R.id.container,
                SelectPlayerSingleGameFlowFragment.createStartBundle(flowDetails, (ArrayList<Player>) activePlayers), flowDetails, useCase, getFragmentAnimation());
    }

    @Override
    public void goToUserCardSelectionScreen(final List<SithCard> availableSithCards, final UseCaseCard useCase, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SithCardSelectGameFlowFragment.class, R.id.container,
                SithCardSelectGameFlowFragment.createStartBundle(flowDetails, (ArrayList<SithCard>) availableSithCards), flowDetails, useCase, getFragmentAnimation());
    }

    @Override
    public void goToUserCardPeekScreen(final List<ActivePlayer> activePlayers, final long delay, GameUseCase useCase, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, UserCardPeekGameFlowFragment.class, R.id.container,
                UserCardPeekGameFlowFragment.createStartBundle(flowDetails, (ArrayList<ActivePlayer>) activePlayers), flowDetails, useCase, getFragmentAnimation());
    }

    @Override
    public void goToSpeakScreen(final int soundResId, final int stringResId, final GameUseCase useCase, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SpeakGameFlowFragment.class, R.id.container,
                SpeakGameFlowFragment.createStartBundle(flowDetails, soundResId, stringResId), flowDetails, useCase, getFragmentAnimation());
    }

    @Override
    public void goTotUserPairPlayerSelection(final List<Player> players, final UseCasePairId useCase, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SelectPlayerPairGameFlowFragment.class, R.id.container,
                SelectPlayerPairGameFlowFragment.createStartBundle(flowDetails, (ArrayList<Player>) players), flowDetails, useCase, getFragmentAnimation());
    }

    @Override
    public void showSelectPlayersScreen(final List<Player> players) {
        Bundle bundle = PlayerSelectFragment.createArguments(new ArrayList<>(players), 30);
        FragmentUtils.replaceOrAddFragment(this, PlayerSelectFragment.class, R.id.container,
                PlayerSelectFragment.class.getName(), FragmentUtils.Animation.ANIMATE_NONE, bundle, false);
    }

    @Override
    public void showKillPlayersScreen(final List<Player> activePlayers) {
        Bundle bundle = PlayerKillFragment.createArguments(new ArrayList<>(activePlayers));
        FragmentUtils.replaceOrAddFragment(this, PlayerKillFragment.class, R.id.container,
                PlayerKillFragment.class.getName(), FragmentUtils.Animation.ANIMATE_SLIDE_LEFT, bundle, true);
    }

    @Override
    public void goToShuffleScreen(final List<Player> players) {
        final int animation = getSupportFragmentManager().findFragmentById(R.id.container) != null ?
                FragmentUtils.Animation.ANIMATE_SLIDE_LEFT : FragmentUtils.Animation.ANIMATE_NONE;
        Bundle bundle = CardShuffleFragment.createArguments(new ArrayList<>(players));
        FragmentUtils.replaceOrAddFragment(this, CardShuffleFragment.class, R.id.container,
                CardShuffleFragment.class.getName(), animation, bundle, true);
    }

    @Override
    public void goToDayScreen(MainGame game) {
        //FragmentUtils.clearFragmentBackStack(getSupportFragmentManager());

        final int animation = getSupportFragmentManager().findFragmentById(R.id.container) != null ?
                FragmentUtils.Animation.ANIMATE_SLIDE_UP : FragmentUtils.Animation.ANIMATE_NONE;
        Bundle bundle = DayFragment.createArguments(game);
        FragmentUtils.replaceOrAddFragment(this, DayFragment.class, R.id.container,
                DayFragment.class.getName(), animation, bundle, false);
    }

    @Override
    public void speak(final int soundResId) {
        if (soundResId != 0) {
            MediaSoundPlayer.playSoundFile(this, soundResId, PRESENTER_TAG);
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
    public void showGameOver(final List<ActivePlayer> players, @GameTeam.Team final int winningTeam) {
        FragmentUtils.replaceOrAddFragment(this, GameOverFragment.class, R.id.container,
                GameOverFragment.class.getName(), FragmentUtils.Animation.ANIMATE_SLIDE_LEFT,
                GameOverFragment.createArguments((ArrayList<ActivePlayer>) players, winningTeam), false);
    }

    @Override
    public void showError(final int stringId) {
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
    public void saveGame(final MainGame mainGame) {
        Settings.setSavedMainGame(this, mainGame);
        Settings.setMainGameSaved(this, true);
    }

    @Override
    public void deleteSavedGame() {
        Settings.setSavedMainGame(this, null);
        Settings.setMainGameSaved(this, false);
    }

    @Override
    public void playMusic(final int musicType) {
        SithMusicPlayer.playMusic(this, musicType);
    }

    @Override
    public void stopPlayingMusic() {
        SithMusicPlayer.stopPlaying();
    }

    @Override
    public void sendSMS(final int stringResId, final String number) {
        SMSUtils.sendSMS(this, getString(stringResId), number);
    }

    @Override
    public void sendSMS(final String text, final String number) {
        SMSUtils.sendSMS(this, text, number);
    }

    private List<Pair<Integer, Integer>> getRandomResourceList() {
        final Field[] fields = R.raw.class.getFields();
        final List<Pair<Integer, Integer>> resourceList = new ArrayList<>();

        for (Field field : fields) {
            final String name = field.getName();
            if (name.startsWith(RANDOM_COMMENT_PREFIX)) {
                final int stringResId = ResourceUtils.getResIdFromResString(this, name, ResourceUtils.RES_TYPE_STRING);
                try {
                    final Pair<Integer, Integer> pair = new Pair<>(field.getInt(field), stringResId);
                    resourceList.add(pair);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return resourceList;
    }

    @Override
    public int getRawResourceId(final String resourceName) {
        return ResourceUtils.getResIdFromResString(this, resourceName, ResourceUtils.RES_TYPE_RAW);
    }

    @Override
    public void onBackPressed() {
        if (!getPresenter().onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onPlayersSelected(final List<Player> players) {
        getPresenter().onPlayerSelected(players);
    }

    @Override
    public void onCardsShuffled(final List<ActivePlayer> activePlayers) {
        getPresenter().onCardsShuffled(activePlayers);
    }

    @Override
    public void onStartNight() {
        presenter.startNight();
    }

    @Override
    public void onKillPlayerSelected(final List<ActivePlayer> activePlayers) {
        presenter.killPlayerSelected(activePlayers);
    }

    @Override
    public void onGamePlayersSelected(final List<ActivePlayer> alivePlayers, final List<ActivePlayer> killedPlayers) {
        FragmentUtils.replaceOrAddFragment(this, GamePlayersFragment.class, R.id.container,
                GamePlayersFragment.class.getName(), FragmentUtils.Animation.ANIMATE_SLIDE_LEFT,
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
        private List<Pair<Integer, Integer>> randomResourceList;

        GameFlowPresenterFactory(final PlayerService playerService,
                                 final SithCardService sithCardService,
                                 final MainGame mainGame) {
            this.playerService = playerService;
            this.sithCardService = sithCardService;
            this.mainGame = mainGame;
        }

        GameFlowPresenterFactory(final PlayerService playerService,
                                 final SithCardService sithCardService,
                                 final MainGame mainGame,
                                 final List<Pair<Integer, Integer>> randomResourceList) {
            this.playerService = playerService;
            this.sithCardService = sithCardService;
            this.mainGame = mainGame;
            this.randomResourceList = randomResourceList;
        }

        @Override
        public GameFlowPresenter createPresenter() {
            if (randomResourceList != null) {
                return new GameFlowPresenter(playerService, sithCardService, mainGame, randomResourceList);
            } else {
                return new GameFlowPresenter(playerService, sithCardService, mainGame);
            }
        }
    }
}
