package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

public class WifiCommandRole extends WifiPackage {
    private final ActivePlayer activePlayer;

    public WifiCommandRole(final ActivePlayer activePlayer) {
        super(PackageType.COMMAND_TYPE_ROLE);
        this.activePlayer = activePlayer;
    }

    public ActivePlayer getActivePlayer() {
        return activePlayer;
    }
}
