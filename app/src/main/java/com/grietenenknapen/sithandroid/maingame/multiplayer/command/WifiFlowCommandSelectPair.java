package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public class WifiFlowCommandSelectPair extends WifiFlowPackage {
    private final List<ActivePlayer> activePlayers;

    public WifiFlowCommandSelectPair(final FlowDetails flowDetails,
                                     final List<ActivePlayer> activePlayers) {

        super(PackageType.COMMAND_TYPE_SELECT_PAIR, flowDetails);
        this.activePlayers = activePlayers;
    }

    public List<ActivePlayer> getActivePlayers() {
        return activePlayers;
    }
}
