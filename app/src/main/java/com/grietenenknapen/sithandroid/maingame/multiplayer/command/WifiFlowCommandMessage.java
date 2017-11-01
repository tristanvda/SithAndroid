package com.grietenenknapen.sithandroid.maingame.multiplayer.command;

import android.support.annotation.IntDef;

import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiFlowPackage;

import java.lang.annotation.Retention;

import static com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandMessage.ResponseType.CODE_FAIL;
import static com.grietenenknapen.sithandroid.maingame.multiplayer.command.WifiFlowCommandMessage.ResponseType.CODE_SUCCESS;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class WifiFlowCommandMessage extends WifiFlowPackage {
    private final String message;
    private final int responseCode;

    @Retention(SOURCE)
    @IntDef({CODE_SUCCESS, CODE_FAIL})

    public @interface ResponseType {
        int CODE_SUCCESS = 1;
        int CODE_FAIL = 2;
    }

    public WifiFlowCommandMessage(final FlowDetails flowDetails,
                                  final String message,
                                  final @ResponseType int responseCode) {

        super(PackageType.COMMAND_TYPE_MESSAGE, flowDetails);
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    @ResponseType
    public int getResponseCode() {
        return responseCode;
    }
}
