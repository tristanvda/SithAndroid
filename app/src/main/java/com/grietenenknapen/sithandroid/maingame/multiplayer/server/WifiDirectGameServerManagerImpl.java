package com.grietenenknapen.sithandroid.maingame.multiplayer.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.Game;
import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketHandler;
import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.EmptyWifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectReceiverListenerAdapter;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiCommandRole;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiCommandSelectRole;
import com.grietenenknapen.sithandroid.maingame.multiplayer.helper.QueueStrategy;
import com.grietenenknapen.sithandroid.maingame.multiplayer.helper.SingleQueueStrategy;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiResponseRole;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

/*
 * Wifi direct server responsible for handling clients
 * This server will accept client connections and offer Roles
 * When a client selects
 *
 */
public class WifiDirectGameServerManagerImpl implements WifiDirectGameServerManager {
    private static final String TAG = WifiDirectGameServerManager.class.getName();
    private static final int START_DELAY = 1000;
    private final Map<Long, Client> playerIdsClients;
    private final WifiP2pManager manager;
    private final WifiManager wifiManager;
    private WifiP2pManager.Channel channel;
    private final PowerManager.WakeLock wakeLock;
    private final WifiManager.WifiLock wifiLock;

    private Game game;
    private GroupOwnerSocketRunner socketRunner;
    private WifiGameServerListener wifiGameServerListener;
    private Context applicationContext;
    private WifiDirectBroadcastReceiver receiver = null;
    private WifiServerStartListener tempStartServerListener = null;
    private boolean groupCreationStarted = false;
    private boolean stoppingServer = false;

    //Currently the queues will have a default strategy which only can have a single Queue
    //Later this can be changed by multiple strategies
    private QueueStrategy<WifiPackage> clientQueueStrategy = new SingleQueueStrategy<>();
    private QueueStrategy<Integer> errorQueueStrategy = new SingleQueueStrategy<>();
    private Queue<WifiPackage> clientResponseQueue = clientQueueStrategy.createQueue();
    private Queue<Integer> errorResQueue = errorQueueStrategy.createQueue();

    @SuppressLint("UseSparseArrays")
    public WifiDirectGameServerManagerImpl(
            final Context context,
            final WifiP2pManager manager,
            final WifiManager wifiManager,
            final PowerManager powerManager) {

        this.manager = manager;
        this.wifiManager = wifiManager;
        this.applicationContext = context.getApplicationContext();
        this.playerIdsClients = Collections.synchronizedMap(new HashMap<Long, Client>());
        this.wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        this.wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public void createAndStartHostingServer(@Nullable final WifiServerStartListener listener) {
        //Stop server first
        this.channel = manager.initialize(applicationContext.getApplicationContext(), Looper.getMainLooper(), null);
        stoppingServer = false;
        stopAndDestroyHostingServer();
        this.tempStartServerListener = listener;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createGroup();
            }
        }, START_DELAY);
    }

    private void createGroup() {
        groupCreationStarted = true;
        manager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {
                if (tempStartServerListener != null) {
                    tempStartServerListener.serverStartFailed(R.string.creation_server_group_failed);
                    tempStartServerListener = null;
                }
            }
        });
    }

    private void createLocalService(final String networkName, final String passPhrase, final String address) {
        //Create a string map containing information about your service.
        final Map<String, String> record = new HashMap<>();
        record.put(WifiDirectGameServerManager.INFO_LISTEN_PORT, String.valueOf(WifiDirectGameServerManager.SERVER_PORT));
        record.put(WifiDirectGameServerManager.INFO_DEVICE_NAME, android.os.Build.MODEL);
        record.put(WifiDirectGameServerManager.INFO_AVAILABLE, WifiDirectGameServerManager.INFO_AVAILABLE_VISIBLE);
        record.put(WifiDirectGameServerManager.INFO_SERVICE_NAME, WifiDirectGameServerManager.SERVICE_NAME);
        record.put(WifiDirectGameServerManager.INFO_NETWORK_NAME, networkName);
        record.put(WifiDirectGameServerManager.INFO_PASS_PHRASE, passPhrase);
        record.put(WifiDirectGameServerManager.INFO_DEVICE_ADDRESS, address);

        final WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance(WifiDirectGameServerManager.SERVICE_NAME, "_presence._tcp", record);

        manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                if (tempStartServerListener != null) {
                    tempStartServerListener.serverStarted();
                    tempStartServerListener = null;
                }
                startListeningForClients();
            }

            @Override
            public void onFailure(int arg0) {
                if (tempStartServerListener != null) {
                    stopAndDestroyHostingServer();
                    tempStartServerListener.serverStartFailed(R.string.creation_server_service_failed);
                    tempStartServerListener = null;
                }
            }
        });
    }

    private final DeviceSocketHandler serverDeviceSocketHandler = new DeviceSocketHandler(new DeviceSocketHandler.GroupOwnerHandlerListener() {

        @Override
        public void onDeviceConnected(final DeviceSocketManager deviceSocketManager) {
            //Do nothing yet
        }

        @Override
        public void onDevicePackageReceived(final DeviceSocketManager deviceSocketManager, final WifiPackage wifiPackage) {
            switch (wifiPackage.getPackageType()) {
                case WifiPackage.PackageType.RESPONSE_TYPE_REQUEST_ROLE:
                    if (game.isGameOver()) {
                        deviceSocketManager.write(new EmptyWifiPackage(WifiCommandRole.PackageType.COMMAND_TYPE_GAME_OVER));
                    } else {
                        final ArrayList<ActivePlayer> availableActivePlayers = new ArrayList<>();
                        for (ActivePlayer activePlayer : game.getActivePlayers()) {
                            if (!isPlayerConnected(activePlayer.getPlayerId())) {
                                availableActivePlayers.add(activePlayer);
                            }
                        }
                        deviceSocketManager.write(new WifiCommandSelectRole(availableActivePlayers));
                    }
                    return;
                case WifiPackage.PackageType.RESPONSE_TYPE_ROLE:
                    final WifiResponseRole wifiResponseRole = (WifiResponseRole) wifiPackage;
                    final long playerId = wifiResponseRole.getPlayerId();
                    if (!isPlayerConnected(playerId)) {
                        final ActivePlayer activePlayer = findActivePlayer(playerId);
                        if (activePlayer != null) {
                            playerIdsClients.put(playerId, new Client(deviceSocketManager, activePlayer));
                            //Inform the client that the selection was ok
                            deviceSocketManager.write(new WifiCommandRole(activePlayer));
                        }
                    }
                    return;
            }

            handleOnClientResponse(wifiPackage);
        }

        @Override
        public void onDeviceDisconnected(final DeviceSocketManager deviceSocketManager) {
            for (Iterator<Map.Entry<Long, Client>> it = playerIdsClients.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Long, Client> entry = it.next();
                if (entry.getValue().getRoleManager().equals(deviceSocketManager)) {
                    it.remove();
                }
            }
        }

        @Override
        public void onRunnerFailed() {
            stopAndDestroyHostingServer();
            handleOnServerError(R.string.server_failed);
        }
    });

    private ActivePlayer findActivePlayer(final long playerId) {
        for (ActivePlayer activePlayer : game.getActivePlayers()) {
            if (activePlayer.getPlayerId() == playerId) {
                return activePlayer;
            }
        }
        return null;
    }

    private void startListeningForClients() {
        try {
            stopAndDestroySocketRunner();
            socketRunner = new GroupOwnerSocketRunner(serverDeviceSocketHandler, wakeLock, wifiLock);
            socketRunner.start();
        } catch (IOException e) {
            Log.d(TAG, "Failed to create a server thread - " + e.getMessage());
        }
    }

    @Override
    public boolean isPlayerConnected(final long activePlayerId) {
        for (long id : playerIdsClients.keySet()) {
            final Client client = playerIdsClients.get(id);

            if (activePlayerId == id
                    && client.getRoleManager().isConnected()) {

                return true;
            }
        }
        return false;
    }

    @Override
    public void setClientResponseListener(@Nullable final WifiGameServerListener wifiGameServerListener) {
        this.wifiGameServerListener = wifiGameServerListener;

        if (this.wifiGameServerListener != null) {
            if (!errorResQueue.isEmpty()) {
                for (int resId : errorResQueue) {
                    if (!stoppingServer) {
                        this.wifiGameServerListener.onServerError(resId);
                    }
                }
            } else if (!clientResponseQueue.isEmpty()) {
                for (WifiPackage wifiPackage : clientResponseQueue) {
                    this.wifiGameServerListener.onClientResponse(wifiPackage);
                }
            }
        }
    }

    private void handleOnClientResponse(final WifiPackage wifiPackage) {
        if (wifiGameServerListener != null) {
            wifiGameServerListener.onClientResponse(wifiPackage);
        } else {
            clientQueueStrategy.addToQueue(clientResponseQueue, wifiPackage);
        }
    }

    private void handleOnServerError(@StringRes final int intRes) {
        if (stoppingServer) {
            return;
        }

        if (wifiGameServerListener != null) {
            wifiGameServerListener.onServerError(intRes);
        } else {
            errorQueueStrategy.addToQueue(errorResQueue, intRes);
        }
    }

    @Override
    public void setGame(@NonNull final Game game) {
        this.game = game;
    }

    private WifiDirectReceiverListenerAdapter wifiDirectReceiverListenerAdapter = new WifiDirectReceiverListenerAdapter() {

        @Override
        public void p2pStateDisabled() {
            if (isServerRunning()) {
                handleOnServerError(R.string.server_failed_p2p);
            }
            stopAndDestroyHostingServer();
        }

        @Override
        public void p2pThisDeviceChanged(final WifiP2pDevice status) {
            if (status.status == WifiP2pDevice.FAILED
                    || status.status == WifiP2pDevice.UNAVAILABLE) {

                stopAndDestroyHostingServer();
                handleOnServerError(R.string.server_failed);
            }
        }

        @Override
        public void p2pDeviceConnected(final NetworkInfo connectionInfo) {
            if (!groupCreationStarted) {
                return;
            }
            manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                @Override
                public void onConnectionInfoAvailable(final WifiP2pInfo info) {
                    if (!info.isGroupOwner) {
                        tempStartServerListener.serverStartFailed(R.string.creation_server_group_owner_failed);
                        tempStartServerListener = null;
                        return;
                    }

                    final String address = info.groupOwnerAddress.getHostAddress();
                    manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {

                        @Override
                        public void onGroupInfoAvailable(final WifiP2pGroup group) {
                            groupCreationStarted = false;
                            if (group != null) {
                                createLocalService(group.getNetworkName(), group.getPassphrase(), address);
                            } else {
                                tempStartServerListener.serverStartFailed(R.string.creation_server_group_owner_failed);
                                tempStartServerListener = null;
                            }
                        }
                    });
                }
            });
        }
    };

    @Override
    public void setBroadcastReceiver(@Nullable final WifiDirectBroadcastReceiver receiver) {
        this.receiver = receiver;

        if (this.receiver != null) {
            this.receiver.addWifiDirectReceiver(wifiDirectReceiverListenerAdapter);
        }
    }

    @Override
    public boolean isServerRunning() {
        return socketRunner != null && socketRunner.isRunning();
    }

    @Override
    public void stopAndDestroyHostingServer() {
        stoppingServer = true;
        tempStartServerListener = null;
        stopAndDestroySocketRunner();

        if (channel != null) {
            manager.clearLocalServices(channel, null);
            manager.removeGroup(channel, null);
            manager.cancelConnect(channel, null);
        }

        //This is to reconnect
        wifiManager.disconnect();
        wifiManager.reconnect();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stoppingServer = false;
            }
        }, 200);
    }

    private void stopAndDestroySocketRunner() {
        for (Client client : playerIdsClients.values()) {
            client.getRoleManager().shutDown();
        }

        if (socketRunner != null) {
            socketRunner.shutDown();
            socketRunner = null;
        }
    }

    @Override
    public void sendWifiFlowPackageToPlayer(@NonNull final WifiPackage wifiFlowPackage, final long activePlayerId) {
        Client client = playerIdsClients.get(activePlayerId);

        if (client == null
                || !client.getRoleManager().isConnected()) {

            return;
        }
        client.getRoleManager().write(wifiFlowPackage);
    }
}
