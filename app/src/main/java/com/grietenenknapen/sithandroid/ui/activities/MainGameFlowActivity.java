package com.grietenenknapen.sithandroid.ui.activities;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.application.Settings;
import com.grietenenknapen.sithandroid.application.SithApplication;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.maingame.MainGame;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.WifiDirectGameServerManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.WifiDirectGameServerManagerImpl;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.fragments.BackPressFragment;
import com.grietenenknapen.sithandroid.ui.fragments.CardShuffleFragment;
import com.grietenenknapen.sithandroid.ui.fragments.DayFragment;
import com.grietenenknapen.sithandroid.ui.fragments.GameOverFragment;
import com.grietenenknapen.sithandroid.ui.fragments.GamePlayersFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerKillFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.DelayGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFlowActivity;
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
import com.grietenenknapen.sithandroid.util.ResourceProvider;
import com.grietenenknapen.sithandroid.util.ResourceUtils;
import com.grietenenknapen.sithandroid.util.SMSUtils;
import com.grietenenknapen.sithandroid.util.SithMusicPlayer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainGameFlowActivity extends PresenterActivity<GameFlowPresenter, GameFlowPresenter.View> implements
        GameFlowPresenter.View, CardShuffleFragment.Callback, PlayerSelectFragment.Callback,
        GameFlowActivity, DayFragment.CallBack, GameOverFragment.Callback {

    private static final String PRESENTER_TAG = "game_flow_presenter";
    private static final String RANDOM_COMMENT_PREFIX = "random_comment";
    private final WifiDirectBroadcastReceiver receiver = new WifiDirectBroadcastReceiver();
    private final IntentFilter intentFilter = new IntentFilter();

    @BindView(R.id.activityLayout)
    RelativeLayout activityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        ButterKnife.bind(this);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
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

        registerReceiver(receiver, intentFilter);
        presenter.setWifiDirectBroadcastReceiver(receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaSoundPlayer.stopPlayer(PRESENTER_TAG);
        MediaSoundPlayer.setMediaSoundPlayListener(null, PRESENTER_TAG);

        this.receiver.clearAllWifiDirectReceivers();
        unregisterReceiver(receiver);
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
    public UseCase getCurrentGameUseCase() {
        return presenter.getCurrentUseCase();
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

        final ResourceProvider resourceProvider = new ResourceProvider(getApplicationContext());
        final WifiDirectGameServerManager gameServerManager = new WifiDirectGameServerManagerImpl(this,
                (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE),
                (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));

        if (Settings.isRandomComments(this)) {
            return new GameFlowPresenterFactory(((SithApplication) getApplicationContext()).getPlayerService(),
                    ((SithApplication) getApplicationContext()).getSithCardService(), mainGame, gameServerManager, resourceProvider, getRandomResourceList());
        } else {
            return new GameFlowPresenterFactory(((SithApplication) getApplicationContext()).getPlayerService(),
                    ((SithApplication) getApplicationContext()).getSithCardService(), mainGame, gameServerManager, resourceProvider);
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
    public void goToDelayScreen(final long delay, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, DelayGameFlowFragment.class, R.id.container,
                DelayGameFlowFragment.createStartBundle(flowDetails, delay), flowDetails, getFragmentAnimation());
    }

    @Override
    public void goToYesNoScreen(final boolean disableYes,
                                final FlowDetails flowDetails, final int titleResId) {

        FragmentUtils.handleGameFlowFragmentTransaction(this, YesNoGameFlowFragment.class, R.id.container,
                YesNoGameFlowFragment.createStartBundle(flowDetails, disableYes, getString(titleResId)), flowDetails, getFragmentAnimation());
    }

    @Override
    public void goToPlayerYesNoScreen(final ActivePlayer activePlayer, final boolean disableYes,
                                      final int titleResId, final FlowDetails flowDetails) {

        FragmentUtils.handleGameFlowFragmentTransaction(this, ShowPlayerYesNoGameFlowFragment.class, R.id.container,
                ShowPlayerYesNoGameFlowFragment.createStartBundle(flowDetails, activePlayer, getString(titleResId), disableYes), flowDetails,
                getFragmentAnimation());
    }

    @Override
    public void goToUserPlayerSelectionScreen(final List<ActivePlayer> activePlayers, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SelectPlayerSingleGameFlowFragment.class, R.id.container,
                SelectPlayerSingleGameFlowFragment.createStartBundle(flowDetails, activePlayers), flowDetails, getFragmentAnimation());
    }

    @Override
    public void goToUserCardSelectionScreen(final List<SithCard> availableSithCards, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SithCardSelectGameFlowFragment.class, R.id.container,
                SithCardSelectGameFlowFragment.createStartBundle(flowDetails, availableSithCards), flowDetails, getFragmentAnimation());
    }

    @Override
    public void goToUserCardPeekScreen(final List<ActivePlayer> activePlayers, final long delay, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, UserCardPeekGameFlowFragment.class, R.id.container,
                UserCardPeekGameFlowFragment.createStartBundle(flowDetails, activePlayers), flowDetails, getFragmentAnimation());
    }

    @Override
    public void goToSpeakScreen(final int soundResId, final int stringResId, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SpeakGameFlowFragment.class, R.id.container,
                SpeakGameFlowFragment.createStartBundle(flowDetails, soundResId, stringResId), flowDetails, getFragmentAnimation());
    }

    @Override
    public void goTotUserPairPlayerSelection(final List<ActivePlayer> players, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SelectPlayerPairGameFlowFragment.class, R.id.container,
                SelectPlayerPairGameFlowFragment.createStartBundleActive(flowDetails, players), flowDetails, getFragmentAnimation());
    }

    @Override
    public void showSelectPlayersScreen(final List<Player> players) {
        Bundle bundle = PlayerSelectFragment.createArguments(players, 30);
        FragmentUtils.replaceOrAddFragment(this, PlayerSelectFragment.class, R.id.container,
                PlayerSelectFragment.class.getName(), false, FragmentUtils.Animation.ANIMATE_NONE, bundle);
    }

    @Override
    public void showKillPlayersScreen(final List<ActivePlayer> activePlayers) {
        Bundle bundle = PlayerKillFragment.createArguments(activePlayers);
        FragmentUtils.replaceOrAddFragment(this, PlayerKillFragment.class, R.id.container,
                PlayerKillFragment.class.getName(), true, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT, bundle);
    }

    @Override
    public void goToShuffleScreen(final List<Player> players) {
        final int animation = getSupportFragmentManager().findFragmentById(R.id.container) != null ?
                FragmentUtils.Animation.ANIMATE_SLIDE_LEFT : FragmentUtils.Animation.ANIMATE_NONE;
        Bundle bundle = CardShuffleFragment.createArguments(players);
        FragmentUtils.replaceOrAddFragment(this, CardShuffleFragment.class, R.id.container,
                CardShuffleFragment.class.getName(), true, animation, bundle);
    }

    @Override
    public void goToDayScreen(MainGame game) {
        //FragmentUtils.clearFragmentBackStack(getSupportFragmentManager());

        final int animation = getSupportFragmentManager().findFragmentById(R.id.container) != null ?
                FragmentUtils.Animation.ANIMATE_SLIDE_UP : FragmentUtils.Animation.ANIMATE_NONE;
        Bundle bundle = DayFragment.createArguments(game);
        FragmentUtils.replaceOrAddFragment(this, DayFragment.class, R.id.container,
                DayFragment.class.getName(), false, animation, bundle);
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
                GameOverFragment.class.getName(), false, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT,
                GameOverFragment.createArguments(players, winningTeam));
    }

    @Override
    public void showError(final int stringId) {
        ActivityUtils.showSnackBar(activityLayout, getString(stringId));
    }

    @Override
    public void showError(final String error) {
        ActivityUtils.showSnackBar(activityLayout, error);
    }

    @Override
    public void showMessage(final int stringResId) {
        Toast.makeText(this, getString(stringResId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void startWifiDirectLoading() {
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (FragmentUtils.validateFragment(fragment) && fragment instanceof DayFragment) {
            ((DayFragment) fragment).startWifiDirectLoading();
        }
    }

    @Override
    public void stopWifiDirectLoading() {
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (FragmentUtils.validateFragment(fragment) && fragment instanceof DayFragment) {
            ((DayFragment) fragment).stopWifiDirectLoading();
        }
    }

    @Override
    public void updateWifiServerState(final boolean wifiP2PEnabled, final boolean serverRunning) {
        final Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.container);
        if (FragmentUtils.validateFragment(currentFrag)
                && currentFrag instanceof DayFragment) {

            ((DayFragment) currentFrag).updateWifiServerState(wifiP2PEnabled, serverRunning);
        }
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
        final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment != null
                && currentFragment instanceof BackPressFragment
                && ((BackPressFragment) currentFragment).onBackPressed()) {
            return;
        }

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
                GamePlayersFragment.class.getName(), true, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT,
                GamePlayersFragment.createArguments(alivePlayers, killedPlayers));
    }

    @Override
    public void onServerButtonClicked() {
        presenter.onServerButtonClicked();
    }

    @Override
    public Pair<Boolean, Boolean> getWifiServerState() {
        return new Pair<>(getPresenter().isWifiP2PEnabled(), getPresenter().isServerRunning());
    }

    @Override
    public void closeGame() {
        this.finish();
    }

    private static class GameFlowPresenterFactory implements PresenterFactory<GameFlowPresenter> {
        private final PlayerService playerService;
        private final SithCardService sithCardService;
        private final MainGame mainGame;
        private final WifiDirectGameServerManager wifiDirectGameServerManager;
        private List<Pair<Integer, Integer>> randomResourceList;
        private final ResourceProvider resourceProvider;

        GameFlowPresenterFactory(final PlayerService playerService,
                                 final SithCardService sithCardService,
                                 final MainGame mainGame,
                                 final WifiDirectGameServerManager wifiDirectGameServerManager,
                                 final ResourceProvider resourceProvider) {

            this.playerService = playerService;
            this.sithCardService = sithCardService;
            this.mainGame = mainGame;
            this.wifiDirectGameServerManager = wifiDirectGameServerManager;
            this.resourceProvider = resourceProvider;
        }

        GameFlowPresenterFactory(final PlayerService playerService,
                                 final SithCardService sithCardService,
                                 final MainGame mainGame,
                                 final WifiDirectGameServerManager wifiDirectGameServerManager,
                                 final ResourceProvider resourceProvider,
                                 final List<Pair<Integer, Integer>> randomResourceList) {

            this.playerService = playerService;
            this.sithCardService = sithCardService;
            this.mainGame = mainGame;
            this.wifiDirectGameServerManager = wifiDirectGameServerManager;
            this.randomResourceList = randomResourceList;
            this.resourceProvider = resourceProvider;
        }

        @Override
        public GameFlowPresenter createPresenter() {
            if (randomResourceList != null) {
                return new GameFlowPresenter(playerService, sithCardService, mainGame, wifiDirectGameServerManager, resourceProvider, randomResourceList);
            } else {
                return new GameFlowPresenter(playerService, sithCardService, mainGame, wifiDirectGameServerManager, resourceProvider);
            }
        }
    }
}
