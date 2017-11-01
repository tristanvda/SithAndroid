package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public class WifiCommandSelectRole extends WifiPackage {
    private final List<ActivePlayer> activePlayers;

    public WifiCommandSelectRole(final List<ActivePlayer> activePlayers) {
        super(PackageType.COMMAND_TYPE_SELECT_ROLE);
        this.activePlayers = activePlayers;
    }

    public List<ActivePlayer> getActivePlayers() {
        return activePlayers;
    }
}
