package com.grietenenknapen.sithandroid.util;

import android.content.Context;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.grietenenknapen.sithandroid.application.Settings;

public class SMSUtils {

    public static void sendSMS(Context context, final String number, final String text) {

        if (!Settings.isSmsEnabled(context) || TextUtils.isEmpty(number)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(number, null, text, null, null);
            }
        }).start();
    }
}
