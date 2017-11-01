package com.grietenenknapen.sithandroid.maingame.multiplayer.helper;

import android.net.wifi.p2p.WifiP2pManager;

import java.lang.reflect.Method;

public final class WifiDirectHelper {

    private WifiDirectHelper() {
    }

    public static void deletePersistentGroups(final WifiP2pManager manager, final WifiP2pManager.Channel channel) {
        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        methods[i].invoke(manager, channel, netid, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
