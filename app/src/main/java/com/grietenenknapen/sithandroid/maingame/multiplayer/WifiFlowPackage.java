package com.grietenenknapen.sithandroid.maingame.multiplayer;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;

public abstract class WifiFlowPackage extends WifiPackage {

    //Flow Details to synchronize flow package
    private final FlowDetails flowDetails;

    public WifiFlowPackage(final int packageType,
                           final FlowDetails flowDetails) {

        super(packageType);
        this.flowDetails = flowDetails;
    }

    public FlowDetails getFlowDetails() {
        return flowDetails;
    }

}
