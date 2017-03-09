package com.grietenenknapen.sithandroid.service.storage.tools;

import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.GameCardType;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDefaults extends DatabaseDefaultsStandard {

    public static List<Player> generateBros() {
        List<Player> players = new ArrayList<>();

        players.add(Player.newBuilder()
                .name("Tristan")
                .telephoneNumber("0483377495")
                .build());

        players.add(Player.newBuilder()
                .name("Wouter")
                .telephoneNumber("0483286052")
                .build());

        players.add(Player.newBuilder()
                .name("Gerry")
                .telephoneNumber("0473391236")
                .build());

        players.add(Player.newBuilder()
                .name("Timo")
                .telephoneNumber("0499176790")
                .build());

        players.add(Player.newBuilder()
                .name("Matthy")
                .telephoneNumber("0499318491")
                .build());

        players.add(Player.newBuilder()
                .name("Babs")
                .telephoneNumber("0497029096")
                .build());

        players.add(Player.newBuilder()
                .name("Pieter")
                .telephoneNumber("0478546890")
                .build());

        return players;
    }

}
