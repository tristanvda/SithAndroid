package com.grietenenknapen.sithandroid.maingame.multiplayer.response;

import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;

public class WifiResponseRole extends WifiPackage {
    private final long playerId;

    public WifiResponseRole(final long playerId) {

        super(PackageType.RESPONSE_TYPE_ROLE);
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }
}
