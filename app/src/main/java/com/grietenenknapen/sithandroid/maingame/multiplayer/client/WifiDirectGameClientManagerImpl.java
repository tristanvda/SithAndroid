package com.grietenenknapen.sithandroid.maingame.multiplayer.client;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketHandler;
import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiReceiverListener;
import com.grietenenknapen.sithandroid.maingame.multiplayer.helper.QueueStrategy;
import com.grietenenknapen.sithandroid.maingame.multiplayer.helper.SingleQueueStrategy;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.WifiDirectGameServerManager;

import java.util.List;
import java.util.Queue;

public class WifiDirectGameClientManagerImpl implements WifiDirectGameClientManager {
    private static final String TAG = WifiDirectGameClientManager.class.getName();
    private static final long[] VIBRATE_PATTERN = new long[]{0, 800, 200, 300, 200, 800};


    private final WifiManager manager;
    private final PowerManager.WakeLock wakeLock;
    private final WifiManager.WifiLock wifiLock;
    private final Vibrator vibrator;
    private final boolean vibrateEnabled;

    private static final int CONNECT_DELAY = 1000;

    private ClientSocketRunner socketRunner;
    private WifiBroadcastReceiver receiver;
    private WifiServerClientListener wifiServerClientListener;
    private OnConnectToServerListener tempConnectToServerListener;
    private DeviceSocketManager serverSocketManager;
    private WifiP2pService wifiP2pService;
    private WifiConfiguration wifiConfig;
    private int netId = -1;

    //Currently the queues will have a default strategy which only can have a single Queue
    //Later this can be changed by multiple strategies
    private QueueStrategy<WifiPackage> clientQueueStrategy = new SingleQueueStrategy<>();
    private QueueStrategy<Integer> errorQueueStrategy = new SingleQueueStrategy<>();

    private Queue<WifiPackage> serverResponseQueue = clientQueueStrategy.createQueue();
    private Queue<Integer> errorResQueue = errorQueueStrategy.createQueue();

    public WifiDirectGameClientManagerImpl(final WifiManager manager,
                                           final PowerManager powerManager,
                                           final Vibrator vibrator,
                                           final boolean vibrateEnabled) {

        this.manager = manager;
        this.vibrator = vibrator;
        this.vibrateEnabled = vibrateEnabled;
        this.wifiLock = manager.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        this.wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public void connectToWifiP2pServer(@NonNull final WifiP2pService service,
                                       @Nullable final OnConnectToServerListener listener) {

        this.tempConnectToServerListener = listener;
        this.wifiP2pService = service;
        this.wifiConfig = new WifiConfiguration();
        this.wifiConfig.SSID = String.format("\"%s\"", service.networkName);
        this.wifiConfig.preSharedKey = String.format("\"%s\"", service.passPhrase);
        this.wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

        netId = manager.addNetwork(wifiConfig);
        manager.disconnect();
        enableNetwork(wifiConfig.SSID);
        manager.reconnect();
    }

    private void enableNetwork(final String SSID) {
        final List<WifiConfiguration> networks = manager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : networks) {
            if (wifiConfig.SSID.equals(SSID)) {
                manager.enableNetwork(wifiConfig.networkId, true);
            } else {
                manager.disableNetwork(wifiConfig.networkId);
            }
        }
        manager.reconnect();
    }

    private void enableDisabledNetworks() {
        final List<WifiConfiguration> networks = manager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : networks) {
            manager.enableNetwork(wifiConfig.networkId, false);
        }
        manager.reconnect();
    }

    @Override
    public void setBroadcastReceiver(@Nullable final WifiBroadcastReceiver receiver) {
        this.receiver = receiver;

        if (this.receiver != null) {
            this.receiver.addWifiDirectReceiver(wifiDirectReceiverListenerAdapter);
        }
    }

    @Override
    public void setServerResponseListener(final WifiServerClientListener wifiServerClientListener) {
        this.wifiServerClientListener = wifiServerClientListener;

        if (this.wifiServerClientListener != null) {
            if (!errorResQueue.isEmpty()) {
                for (int resId : errorResQueue) {
                    this.wifiServerClientListener.onServerError(resId);
                }
            } else if (!serverResponseQueue.isEmpty()) {
                for (WifiPackage wifiPackage : serverResponseQueue) {
                    this.wifiServerClientListener.onServerResponse(wifiPackage);
                }
            }
        }
    }

    private void handleOnServerResponse(final WifiPackage wifiPackage) {
        if (wifiServerClientListener != null) {
            wifiServerClientListener.onServerResponse(wifiPackage);
        } else {
            clientQueueStrategy.addToQueue(serverResponseQueue, wifiPackage);
        }
    }

    private void handleOnServerError(@StringRes final int intRes) {
        if (wifiServerClientListener != null) {
            wifiServerClientListener.onServerError(intRes);
        } else {
            errorQueueStrategy.addToQueue(errorResQueue, intRes);
        }
    }

    @Override
    public void sendWifiFlowPackageToPlayer(@NonNull final WifiPackage wifiFlowPackage) {
        if (serverSocketManager != null) {
            serverSocketManager.write(wifiFlowPackage);
        }
    }

    @Override
    public boolean isConnectedToServer() {
        return serverSocketManager != null && serverSocketManager.isConnected();
    }

    @Override
    public void stopAndDestroyClientToServerConnection() {
        destroySocket();

        manager.disconnect();
        manager.removeNetwork(netId);
        netId = -1;
        enableDisabledNetworks();

        errorResQueue.clear();
        serverResponseQueue.clear();
    }

    private void destroySocket() {
        if (socketRunner != null) {
            socketRunner.shutDown();
            socketRunner = null;
        }
        if (serverSocketManager != null) {
            serverSocketManager.shutDown();
            serverSocketManager = null;
        }
    }

    private DeviceSocketHandler clientSocketHandler = new DeviceSocketHandler(new DeviceSocketHandler.GroupOwnerHandlerListener() {

        @Override
        public void onDeviceConnected(final DeviceSocketManager deviceSocketManager) {
            serverSocketManager = deviceSocketManager;
            //Once connected to the server, request the role
            deviceSocketManager.write(new WifiPackage(WifiPackage.PackageType.RESPONSE_TYPE_REQUEST_ROLE));
        }

        @Override
        public void onDevicePackageReceived(final DeviceSocketManager deviceSocketManager, final WifiPackage wifiPackage) {
            if (wifiPackage instanceof WifiFlowPackage && vibrateEnabled) {
                vibrator.vibrate(VIBRATE_PATTERN, -1);
            }
            handleOnServerResponse(wifiPackage);
        }

        @Override
        public void onDeviceDisconnected(final DeviceSocketManager deviceSocketManager) {
            //TODO: might want to connect here again or in on resume
            stopAndDestroyClientToServerConnection();
            handleOnServerError(R.string.server_disconnected);
        }

        @Override
        public void onRunnerFailed() {
            stopAndDestroyClientToServerConnection();
            handleOnServerError(R.string.connection_to_server_failed);
        }
    });

    private WifiReceiverListener wifiDirectReceiverListenerAdapter = new WifiReceiverListener() {

        @Override
        public void wifiConnected(final int ipAddress, final String SSID) {
            if (!wifiConfig.SSID.equals(SSID)) {
                return;
            }

            destroySocket();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    socketRunner = new ClientSocketRunner(clientSocketHandler, wifiP2pService.deviceAddress, wifiLock, wakeLock);
                    socketRunner.start();
                    if (tempConnectToServerListener != null) {
                        tempConnectToServerListener.onConnectedToServer();
                    }
                }
            }, CONNECT_DELAY);
        }

        @Override
        public void wifiDisConnected(final String SSID) {
            if (!wifiConfig.SSID.equals(SSID)) {
                return;
            }
            stopAndDestroyClientToServerConnection();
            handleOnServerError(R.string.server_disconnected);
        }
    };

}