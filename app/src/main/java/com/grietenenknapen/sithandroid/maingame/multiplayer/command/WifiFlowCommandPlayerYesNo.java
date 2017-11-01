package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;

public class WifiFlowCommandPlayerYesNo extends WifiFlowPackage {
    private final ActivePlayer activePlayer;
    private final String message;
    private final boolean hideYes;

    public WifiFlowCommandPlayerYesNo(final FlowDetails flowDetails,
                                      final ActivePlayer activePlayer,
                                      final String message,
                                      final boolean hideYes) {

        super(PackageType.COMMAND_TYPE_SHOW_PLAYER_YES_NO, flowDetails);
        this.activePlayer = activePlayer;
        this.message = message;
        this.hideYes = hideYes;
    }

    public ActivePlayer getActivePlayer() {
        return activePlayer;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHideYes() {
        return hideYes;
    }
}
