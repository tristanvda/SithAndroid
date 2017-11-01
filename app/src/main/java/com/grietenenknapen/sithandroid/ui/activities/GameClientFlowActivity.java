package com.grietenenknapen.sithandroid.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiDirectGameClientManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiDirectGameClientManagerImpl;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiP2pService;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.PresenterActivity;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.fragments.ClientErrorFragment;
import com.grietenenknapen.sithandroid.ui.fragments.LoadingFragment;
import com.grietenenknapen.sithandroid.ui.fragments.PlayerSelectFragment;
import com.grietenenknapen.sithandroid.ui.fragments.UserRoleFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFlowActivity;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SelectPlayerPairGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SelectPlayerSingleGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.ShowPlayerYesNoGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.SithCardSelectGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.UserCardPeekGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.YesNoGameFlowFragment;
import com.grietenenknapen.sithandroid.ui.presenters.GameClientFlowPresenter;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.util.FragmentUtils;

import java.util.List;

import butterknife.ButterKnife;

public class GameClientFlowActivity extends PresenterActivity<GameClientFlowPresenter, GameClientFlowPresenter.View>
        implements GameClientFlowPresenter.View, ClientErrorFragment.Callback, PlayerSelectFragment.Callback, GameFlowActivity {

    private final static String KEY_WIFI_P2P_SERVICE = "wifi_p2p_service";
    private static final String PRESENTER_TAG = "game_client_flow_presenter";
    private final WifiBroadcastReceiver receiver = new WifiBroadcastReceiver();
    private final IntentFilter intentFilter = new IntentFilter();

    public static Intent createStartIntent(final Context context, final WifiP2pService wifiP2pService) {
        Intent intent = new Intent(context, GameClientFlowActivity.class);
        intent.putExtra(KEY_WIFI_P2P_SERVICE, wifiP2pService);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        ButterKnife.bind(this);

        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, intentFilter);
        presenter.setWifiDirectBroadcastReceiver(receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.receiver.clearAllWifiDirectReceivers();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.label_warning)
                .content(R.string.game_quit_warning)
                .positiveText(android.R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        finish();

                    }
                })
                .negativeText(android.R.string.no)
                .show();
    }


    @Override
    public void showError(@StringRes final int errorResId) {
        final String errorString = getString(errorResId);
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof ClientErrorFragment) {
            return;
        }
        FragmentUtils.replaceOrAddFragment(this, ClientErrorFragment.class, R.id.container, errorString,
                false, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT, ClientErrorFragment.createArguments(errorString));
    }

    @Override
    public void showError(final String error) {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof ClientErrorFragment) {
            return;
        }
        FragmentUtils.replaceOrAddFragment(this, ClientErrorFragment.class, R.id.container, error,
                false, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT, ClientErrorFragment.createArguments(error));
    }

    @Override
    public void showLoadingScreen() {
        FragmentUtils.replaceOrAddFragment(this, LoadingFragment.class, R.id.container, LoadingFragment.class.getName(),
                false, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void goToRequestRoleScreen(final List<ActivePlayer> activePlayers) {
        FragmentUtils.replaceOrAddFragment(this, PlayerSelectFragment.class, R.id.container, PlayerSelectFragment.class.getName(),
                false, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT,
                PlayerSelectFragment.createArguments(PlayerSelectFragment.createPlayersList(activePlayers), 1, getString(R.string.client_identify), R.drawable.ic_play));
    }

    @Override
    public void goToYesNoScreen(final boolean disableYes,
                                final FlowDetails flowDetails, final String title) {

        FragmentUtils.handleGameFlowFragmentTransaction(this, YesNoGameFlowFragment.class, R.id.container,
                YesNoGameFlowFragment.createStartBundle(flowDetails, disableYes, title),
                flowDetails, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void goToPlayerYesNoScreen(final ActivePlayer activePlayer, final boolean disableYes,
                                      final String title, final FlowDetails flowDetails) {

        FragmentUtils.handleGameFlowFragmentTransaction(this, ShowPlayerYesNoGameFlowFragment.class, R.id.container,
                ShowPlayerYesNoGameFlowFragment.createStartBundle(flowDetails, activePlayer, title, disableYes),
                flowDetails, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void goToUserPlayerSelectionScreen(final List<ActivePlayer> activePlayers, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SelectPlayerSingleGameFlowFragment.class, R.id.container,
                SelectPlayerSingleGameFlowFragment.createStartBundle(flowDetails, activePlayers),
                flowDetails, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void goToUserCardSelectionScreen(final List<SithCard> availableSithCards, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SithCardSelectGameFlowFragment.class, R.id.container,
                SithCardSelectGameFlowFragment.createStartBundle(flowDetails, availableSithCards),
                flowDetails, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void goToUserCardPeekScreen(final List<ActivePlayer> activePlayers, final long delay, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, UserCardPeekGameFlowFragment.class, R.id.container,
                UserCardPeekGameFlowFragment.createStartBundle(flowDetails, activePlayers),
                flowDetails, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void goTotUserPairPlayerSelection(final List<ActivePlayer> players, final FlowDetails flowDetails) {
        FragmentUtils.handleGameFlowFragmentTransaction(this, SelectPlayerPairGameFlowFragment.class, R.id.container,
                SelectPlayerPairGameFlowFragment.createStartBundleActive(flowDetails, players),
                flowDetails, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT);
    }

    @Override
    public void showUserRoleScreen(final ActivePlayer activePlayer) {
        FragmentUtils.replaceOrAddFragment(this, UserRoleFragment.class, R.id.container, UserRoleFragment.class.getName(),
                false, FragmentUtils.Animation.ANIMATE_SLIDE_LEFT, UserRoleFragment.createArguments(activePlayer));
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<GameClientFlowPresenter> getPresenterFactory() {
        final WifiP2pService wifiP2pService = getIntent().getParcelableExtra(KEY_WIFI_P2P_SERVICE);
        final WifiDirectGameClientManager wifiDirectGameClientManager =
                new WifiDirectGameClientManagerImpl((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        return new GameClientFlowPresenterFactory(wifiDirectGameClientManager, wifiP2pService);
    }

    @Override
    protected GameClientFlowPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void onRefreshClicked() {
        presenter.startConnectionIfNotRunning();
    }

    @Override
    public void onCloseClicked() {
        this.finish();
    }

    @Override
    public void setGameStatus(@GameFlowPresenter.GameStatus final int gameStatus) {
        //Do nothing here
    }

    @Override
    public UseCase getCurrentGameUseCase() {
        return getPresenter().getResponseUseCase();
    }

    @Override
    public void onPlayersSelected(final List<Player> players) {
        if (players.size() > 0) {
            getPresenter().onRoleSelected(players.get(0));
        }
    }

    private static final class GameClientFlowPresenterFactory implements PresenterFactory<GameClientFlowPresenter> {
        private final WifiDirectGameClientManager wifiDirectGameClientManager;
        private final WifiP2pService wifiP2pService;

        private GameClientFlowPresenterFactory(final WifiDirectGameClientManager wifiDirectGameClientManager, final WifiP2pService wifiP2pService) {
            this.wifiDirectGameClientManager = wifiDirectGameClientManager;
            this.wifiP2pService = wifiP2pService;
        }

        @Override
        public GameClientFlowPresenter createPresenter() {
            return new GameClientFlowPresenter(wifiDirectGameClientManager, wifiP2pService);
        }
    }
}
