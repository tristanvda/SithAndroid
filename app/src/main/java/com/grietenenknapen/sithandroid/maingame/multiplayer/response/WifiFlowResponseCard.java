package com.grietenenknapen.sithandroid.maingame.multiplayer.response;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.model.database.SithCard;


public class WifiFlowResponseCard extends WifiFlowPackage {
    private final SithCard card;

    public WifiFlowResponseCard(final FlowDetails flowDetails, final SithCard card) {
        super(PackageType.RESPONSE_TYPE_CARD, flowDetails);
        this.card = card;
    }

    public SithCard getCard() {
        return card;
    }
}
