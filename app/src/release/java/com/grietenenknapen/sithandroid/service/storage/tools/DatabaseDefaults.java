package com.grietenenknapen.sithandroid.service.storage.tools;


import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.GameCardType;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDefaults extends DatabaseDefaultsStandard {

    public static List<Player> generateBros() {
        final List<Player> players = new ArrayList<>();
        return players;
    }

}
