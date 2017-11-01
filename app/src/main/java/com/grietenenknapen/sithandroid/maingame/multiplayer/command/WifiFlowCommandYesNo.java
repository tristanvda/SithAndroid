package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;

public class WifiFlowCommandYesNo extends WifiFlowPackage {

    private final String message;
    private final boolean hideYes;

    public WifiFlowCommandYesNo(final FlowDetails flowDetails,
                                final String message,
                                final boolean hideYes) {

        super(PackageType.COMMAND_TYPE_REQUEST_YES_NO, flowDetails);
        this.message = message;
        this.hideYes = hideYes;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHideYes() {
        return hideYes;
    }
}
