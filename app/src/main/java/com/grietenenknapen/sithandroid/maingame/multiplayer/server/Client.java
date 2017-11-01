package com.grietenenknapen.sithandroid.maingame.multiplayer.server;

import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketManager;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

class Client {
    private DeviceSocketManager roleManager;
    private ActivePlayer activePlayer;

    Client(final DeviceSocketManager roleManager, final ActivePlayer activePlayer) {
        this.roleManager = roleManager;
        this.activePlayer = activePlayer;
    }

    DeviceSocketManager getRoleManager() {
        return roleManager;
    }

    public ActivePlayer getActivePlayer() {
        return activePlayer;
    }
}
