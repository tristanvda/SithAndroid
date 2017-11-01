package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

import java.util.List;

public class WifiFlowCommandPeek extends WifiFlowPackage {
    private final List<ActivePlayer> activePlayers;

    public WifiFlowCommandPeek(final FlowDetails flowDetails,
                               final List<ActivePlayer> activePlayers) {

        super(PackageType.COMMAND_TYPE_CARD_PEEK, flowDetails);
        this.activePlayers = activePlayers;
    }

    public List<ActivePlayer> getActivePlayers() {
        return activePlayers;
    }
}
