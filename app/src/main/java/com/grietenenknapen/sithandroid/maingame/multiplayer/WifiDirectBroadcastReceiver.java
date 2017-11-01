package com.grietenenknapen.sithandroid.maingame.multiplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.HashSet;

public final class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private final HashSet<WifiDirectReceiverListener> wifiDirectReceiverListeners = new HashSet<>();

    public void addWifiDirectReceiver(final WifiDirectReceiverListener wifiDirectReceiverListener) {
        wifiDirectReceiverListeners.add(wifiDirectReceiverListener);
    }

    public void removeWifiDirectReceiver(final WifiDirectReceiverListener wifiDirectReceiverListener) {
        wifiDirectReceiverListeners.remove(wifiDirectReceiverListener);
    }

    public void clearAllWifiDirectReceivers() {
        wifiDirectReceiverListeners.clear();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (wifiDirectReceiverListeners.isEmpty()) {
            return;
        }

        final String action = intent.getAction();
        switch (action) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                final int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, WifiP2pManager.WIFI_P2P_STATE_DISABLED);

                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // Wifi Direct mode is enabled
                    handleP2pStateEnabled();
                } else {
                    handleP2pStateDisabled();
                }
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                NetworkInfo networkInfo = intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                if (networkInfo.isConnected()) {
                    handleP2pDeviceConnected(networkInfo);
                } else {
                    handleP2pDeviceDisconnected(networkInfo);
                }
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                WifiP2pDevice device = intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                handleP2pThisDeviceChanged(device);
                break;
        }
    }

    private void handleP2pStateEnabled() {
        for (WifiDirectReceiverListener wifiDirectReceiverListener : wifiDirectReceiverListeners) {
            wifiDirectReceiverListener.p2pStateEnabled();
        }
    }

    private void handleP2pStateDisabled() {
        for (WifiDirectReceiverListener wifiDirectReceiverListener : wifiDirectReceiverListeners) {
            wifiDirectReceiverListener.p2pStateDisabled();
        }
    }

    private void handleP2pDeviceConnected(final NetworkInfo networkInfo) {
        for (WifiDirectReceiverListener wifiDirectReceiverListener : wifiDirectReceiverListeners) {
            wifiDirectReceiverListener.p2pDeviceConnected(networkInfo);
        }
    }

    private void handleP2pDeviceDisconnected(final NetworkInfo networkInfo) {
        for (WifiDirectReceiverListener wifiDirectReceiverListener : wifiDirectReceiverListeners) {
            wifiDirectReceiverListener.p2pDeviceDisconnected(networkInfo);
        }
    }

    private void handleP2pThisDeviceChanged(final WifiP2pDevice wifiP2pDevice) {
        for (WifiDirectReceiverListener wifiDirectReceiverListener : wifiDirectReceiverListeners) {
            wifiDirectReceiverListener.p2pThisDeviceChanged(wifiP2pDevice);
        }
    }

}