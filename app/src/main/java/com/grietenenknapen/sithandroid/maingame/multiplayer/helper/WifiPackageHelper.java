package com.grietenenknapen.sithandroid.maingame.multiplayer.helper;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.EmptyWifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiPackage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiCommandRole;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiCommandSelectRole;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandMessage;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandPeek;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandPlayerYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectPair;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectPlayer;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandSelectSithCard;
import com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseCard;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseId;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponsePairId;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiFlowResponseYesNo;
import com.grietenenknapen.sithandroid.maingame.multiplayer.response.WifiResponseRole;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;

public final class WifiPackageHelper {
    private static final String TAG = WifiPackageHelper.class.getName();

    private WifiPackageHelper() {
    }

    public static Class<? extends WifiPackage> findWifiPackageClass(final String json) {
        final JsonParser parser = new JsonParser();
        final JsonObject obj = parser.parse(json).getAsJsonObject();

        if (obj.has("packageType")) {
            try {
                final JsonElement element = obj.get("packageType");
                final int packageType = element.getAsInt();

                switch (packageType) {
                    case WifiPackage.PackageType.COMMAND_TYPE_CARD_PEEK:
                        return WifiFlowCommandPeek.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_MESSAGE:
                        return WifiFlowCommandMessage.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_SELECT_PAIR:
                        return WifiFlowCommandSelectPair.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_SELECT_PLAYER:
                        return WifiFlowCommandSelectPlayer.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_SELECT_ROLE:
                        return WifiCommandSelectRole.class;
                    case WifiPackage.PackageType.RESPONSE_TYPE_REQUEST_ROLE:
                    case WifiPackage.PackageType.COMMAND_TYPE_GAME_OVER:
                    case WifiPackage.PackageType.COMMAND_TYPE_OK:
                        return EmptyWifiPackage.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_SELECT_SITH_CARD:
                        return WifiFlowCommandSelectSithCard.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_SHOW_PLAYER_YES_NO:
                        return WifiFlowCommandPlayerYesNo.class;
                    case WifiPackage.PackageType.RESPONSE_TYPE_ID:
                        return WifiFlowResponseId.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_REQUEST_YES_NO:
                        return WifiFlowCommandYesNo.class;
                    case WifiPackage.PackageType.RESPONSE_TYPE_PAIR_ID:
                        return WifiFlowResponsePairId.class;
                    case WifiPackage.PackageType.RESPONSE_TYPE_ROLE:
                        return WifiResponseRole.class;
                    case WifiPackage.PackageType.RESPONSE_TYPE_YES_NO:
                        return WifiFlowResponseYesNo.class;
                    case WifiPackage.PackageType.COMMAND_TYPE_ROLE:
                        return WifiCommandRole.class;
                    case WifiPackage.PackageType.RESPONSE_TYPE_CARD:
                        return WifiFlowResponseCard.class;
                }
            } catch (ClassCastException | IllegalStateException e) {
                Log.d(TAG, "could not cast packageType");
            }
        }
        //Json is no WifiPackage
        return null;
    }

    public static Class<? extends UseCase> getUseCaseFromPackage(final WifiPackage wifiPackage) {
        switch (wifiPackage.getPackageType()) {
            case WifiPackage.PackageType.RESPONSE_TYPE_ID:
                return UseCaseId.class;
            case WifiPackage.PackageType.RESPONSE_TYPE_PAIR_ID:
                return UseCasePairId.class;
            case WifiPackage.PackageType.RESPONSE_TYPE_YES_NO:
                return UseCaseYesNo.class;
            case WifiPackage.PackageType.RESPONSE_TYPE_CARD:
                return UseCaseCard.class;
            default:
                return null;
        }
    }
}
