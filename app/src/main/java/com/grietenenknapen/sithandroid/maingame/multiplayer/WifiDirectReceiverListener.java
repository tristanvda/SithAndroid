package com.grietenenknapen.sithandroid.maingame.multiplayer;

import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;


public interface WifiDirectReceiverListener {

    void p2pStateEnabled();

    void p2pStateDisabled();

    void p2pDeviceConnected(NetworkInfo connectionInfo);

    void p2pDeviceDisconnected(NetworkInfo connectionInfo);

    void p2pThisDeviceChanged(WifiP2pDevice status);

}
