package com.grietenenknapen.sithandroid.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputValidations {
    private static final String PHONE_REGEX = "^\\+?[0-9. ()-]{10,25}$";
    private static final int NICK_NAME_MAX_LENGTH = 9;

    private InputValidations() {
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    public static boolean validateNickName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }

        return name.length() < NICK_NAME_MAX_LENGTH;
    }
}
