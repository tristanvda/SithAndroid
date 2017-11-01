package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;
import com.grietenenknapen.sithandroid.model.database.SithCard;

import java.util.List;

public class WifiFlowCommandSelectSithCard extends WifiFlowPackage {
    private final List<SithCard> sithCards;

    public WifiFlowCommandSelectSithCard(final FlowDetails flowDetails,
                                         final List<SithCard> sithCards) {

        super(PackageType.COMMAND_TYPE_SELECT_SITH_CARD, flowDetails);
        this.sithCards = sithCards;
    }

    public List<SithCard> getSithCards() {
        return sithCards;
    }
}
