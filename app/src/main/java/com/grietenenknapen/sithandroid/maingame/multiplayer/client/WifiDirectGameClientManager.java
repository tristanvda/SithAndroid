package com.grietenenknapen.sithandroid.maingame.multiplayer.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;

public interface WifiDirectGameClientManager {

    void connectToWifiP2pServer(@NonNull WifiP2pService service, @Nullable OnConnectToServerListener listener);

    void setBroadcastReceiver(@Nullable WifiBroadcastReceiver receiver);

    void setServerResponseListener(@Nullable WifiDirectGameClientManager.WifiServerClientListener wifiGameServerListener);

    void sendWifiFlowPackageToPlayer(@NonNull WifiPackage wifiFlowPackage);

    boolean isConnectedToServer();

    void stopAndDestroyClientToServerConnection();

    interface OnConnectToServerListener {

        void onConnectedToServer();

        void onConnectionError(@StringRes int messageRes);
    }

    interface WifiServerClientListener {

        void onServerResponse(WifiPackage wifiPackage);

        void onServerError(@StringRes int messageRes);
    }
}
