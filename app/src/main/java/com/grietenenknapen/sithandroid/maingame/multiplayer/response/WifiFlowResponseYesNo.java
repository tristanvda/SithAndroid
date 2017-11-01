package com.grietenenknapen.sithandroid.maingame.multiplayer.response;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;

public class WifiFlowResponseYesNo extends WifiFlowPackage {
    private final boolean response;

    public WifiFlowResponseYesNo(final FlowDetails flowDetails,
                                 final boolean response) {

        super(PackageType.RESPONSE_TYPE_YES_NO, flowDetails);
        this.response = response;
    }

    public boolean isYes() {
        return response;
    }
}
