package com.grietenenknapen.sithandroid.maingame.multiplayer;

public interface WifiReceiverListener {

    void wifiConnected(int ipAddress, String SSID);

    void wifiDisConnected(String SSID);
}
