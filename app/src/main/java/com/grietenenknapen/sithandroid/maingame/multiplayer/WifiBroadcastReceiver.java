package com.grietenenknapen.sithandroid.maingame.multiplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.HashSet;

public final class WifiBroadcastReceiver extends BroadcastReceiver {
    private boolean hadConnection = false;

    private final HashSet<WifiReceiverListener> wifiReceiverListeners = new HashSet<>();

    public void addWifiDirectReceiver(final WifiReceiverListener wifiReceiverListener) {
        wifiReceiverListeners.add(wifiReceiverListener);
    }

    public void removeWifiDirectReceiver(final WifiReceiverListener wifiReceiverListener) {
        wifiReceiverListeners.remove(wifiReceiverListener);
    }

    public void clearAllWifiDirectReceivers() {
        wifiReceiverListeners.clear();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (wifiReceiverListeners.isEmpty()) {
            return;
        }

        final String action = intent.getAction();
        switch (action) {
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                final NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                final WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);

                boolean connected = false;

                if (info != null) {

                    if (info.isConnected() && !hadConnection) {
                        connected = true;
                    } else if (!info.isConnectedOrConnecting()) {
                        if (hadConnection) {
                            handleWifiDisconnected(wifiInfo != null ? wifiInfo.getSSID() : "");
                        }
                    }
                }

                if (wifiInfo != null && connected) {
                    handleWifiConnected(wifiInfo.getIpAddress(), wifiInfo.getSSID());
                }
        }
    }

    private void handleWifiConnected(final int ipAddress, final String SSID) {
        hadConnection = true;

        for (WifiReceiverListener wifiDirectReceiverListener : wifiReceiverListeners) {
            wifiDirectReceiverListener.wifiConnected(ipAddress, SSID);
        }
    }

    private void handleWifiDisconnected(final String SSID) {

        if (hadConnection) {
            hadConnection = false;

            for (WifiReceiverListener wifiDirectReceiverListener : wifiReceiverListeners) {
                wifiDirectReceiverListener.wifiDisConnected(SSID);
            }
        }
    }

}