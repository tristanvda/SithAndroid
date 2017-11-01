package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public class WifiFlowCommandSelectPlayer extends WifiFlowPackage {
    private final List<ActivePlayer> activePlayers;
    private final String message;

    public WifiFlowCommandSelectPlayer(final FlowDetails flowDetails,
                                       final List<ActivePlayer> activePlayers,
                                       final String message) {

        super(WifiPackage.PackageType.COMMAND_TYPE_SELECT_PLAYER, flowDetails);
        this.activePlayers = activePlayers;
        this.message = message;
    }

    public List<ActivePlayer> getActivePlayers() {
        return activePlayers;
    }

    public String getMessage() {
        return message;
    }
}
