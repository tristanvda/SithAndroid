package com.grietenenknapen.sithandroid.maingame.multiplayer.server;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.grietenenknapen.sithandroid.game.Game;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;

import java.util.List;

public interface WifiDirectGameServerManager {

    String INFO_LISTEN_PORT = "listenPort";
    String INFO_AVAILABLE = "available";
    String INFO_AVAILABLE_VISIBLE = "visible";
    String INFO_AVAILABLE_INVISIBLE = "invisible";
    String INFO_DEVICE_NAME = "deviceName";
    String INFO_SERVICE_NAME = "serviceName";
    String INFO_NETWORK_NAME = "networkName";
    String INFO_PASS_PHRASE = "passPhrase";
    String INFO_DEVICE_ADDRESS = "deviceAddress";
    String SERVICE_NAME = "sith_app";
    int SERVER_PORT = 4545;

    void createAndStartHostingServer(@Nullable WifiServerStartListener listener);

    void setClientResponseListener(@Nullable WifiGameServerListener wifiGameServerListener);

    void setGame(@NonNull Game game);

    void setBroadcastReceiver(@Nullable WifiDirectBroadcastReceiver receiver);

    boolean isServerRunning();

    void stopAndDestroyHostingServer();

    void sendWifiFlowPackageToPlayer(@NonNull WifiPackage wifiFlowPackage, long activePlayerId);

    boolean isPlayerConnected(long activePlayer);

    interface WifiServerStartListener {

        void serverStarted();

        void serverStartFailed(String message);

        void serverStartFailed(int messageRes);

    }

    interface WifiGameServerListener {

        void onClientResponse(WifiPackage wifiPackage);

        void onServerError(int messageRes);

    }
}
