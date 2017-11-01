package com.grietenenknapen.sithandroid.maingame.multiplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_CARD_PEEK;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_MESSAGE;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_OK;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_REQUEST_YES_NO;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_ROLE;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_SELECT_PAIR;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_SELECT_PLAYER;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_SELECT_ROLE;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_SELECT_SITH_CARD;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.COMMAND_TYPE_SHOW_PLAYER_YES_NO;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.RESPONSE_TYPE_CARD;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.RESPONSE_TYPE_ID;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.RESPONSE_TYPE_PAIR_ID;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.RESPONSE_TYPE_REQUEST_ROLE;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.RESPONSE_TYPE_ROLE;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage.PackageType.RESPONSE_TYPE_YES_NO;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class WifiPackage {

    private final int packageType;

    @Retention(SOURCE)
    @IntDef({COMMAND_TYPE_SELECT_PLAYER, COMMAND_TYPE_SELECT_PAIR, COMMAND_TYPE_SELECT_SITH_CARD,
            COMMAND_TYPE_CARD_PEEK, COMMAND_TYPE_SHOW_PLAYER_YES_NO, COMMAND_TYPE_MESSAGE, COMMAND_TYPE_SELECT_ROLE,
            RESPONSE_TYPE_ID, RESPONSE_TYPE_PAIR_ID, RESPONSE_TYPE_YES_NO, RESPONSE_TYPE_ROLE, RESPONSE_TYPE_REQUEST_ROLE,
            COMMAND_TYPE_REQUEST_YES_NO, RESPONSE_TYPE_CARD, COMMAND_TYPE_OK, COMMAND_TYPE_ROLE})

    public @interface PackageType {
        int COMMAND_TYPE_SELECT_PLAYER = 1;
        int COMMAND_TYPE_SELECT_PAIR = 2;
        int COMMAND_TYPE_SELECT_SITH_CARD = 3;
        int COMMAND_TYPE_CARD_PEEK = 4;
        int COMMAND_TYPE_SHOW_PLAYER_YES_NO = 5;
        int COMMAND_TYPE_MESSAGE = 6;
        int COMMAND_TYPE_SELECT_ROLE = 7;
        int COMMAND_TYPE_REQUEST_YES_NO = 8;
        int COMMAND_TYPE_OK = 9;
        int COMMAND_TYPE_ROLE = 10;
        int RESPONSE_TYPE_ID = 51;
        int RESPONSE_TYPE_PAIR_ID = 52;
        int RESPONSE_TYPE_YES_NO = 53;
        int RESPONSE_TYPE_ROLE = 54;
        int RESPONSE_TYPE_REQUEST_ROLE = 55;
        int RESPONSE_TYPE_CARD = 56;
    }

    public WifiPackage(final int packageType) {
        this.packageType = packageType;
    }

    public int getPackageType() {
        return packageType;
    }
}
