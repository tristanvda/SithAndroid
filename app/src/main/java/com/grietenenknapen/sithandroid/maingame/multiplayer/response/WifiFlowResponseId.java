package com.grietenenknapen.sithandroid.maingame.multiplayer.response;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;


public class WifiFlowResponseId extends WifiFlowPackage {
    private final long id;

    public WifiFlowResponseId(final FlowDetails flowDetails, final long id) {
        super(PackageType.RESPONSE_TYPE_ID, flowDetails);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
