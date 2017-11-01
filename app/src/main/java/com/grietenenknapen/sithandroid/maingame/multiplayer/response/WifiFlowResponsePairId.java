package com.grietenenknapen.sithandroid.maingame.multiplayer.response;

import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;

public class WifiFlowResponsePairId extends WifiFlowPackage {
    private final long idOne;
    private final long idTwo;

    public WifiFlowResponsePairId(final FlowDetails flowDetails,
                                  final long idOne,
                                  final long idTwo) {

        super(PackageType.RESPONSE_TYPE_PAIR_ID, flowDetails);
        this.idOne = idOne;
        this.idTwo = idTwo;
    }

    public Pair<Long, Long> getPairId() {
        return new Pair<>(idOne, idTwo);
    }
}
