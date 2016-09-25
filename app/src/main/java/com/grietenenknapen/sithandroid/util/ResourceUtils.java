package com.grietenenknapen.sithandroid.util;


import android.content.Context;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class ResourceUtils {

    @Retention(SOURCE)
    @StringDef({RES_TYPE_RAW, RES_TYPE_DRAWABLE, RES_TYPE_STRING})
    public @interface ResType {
    }

    public static final String RES_TYPE_RAW = "raw";
    public static final String RES_TYPE_DRAWABLE = "drawable";
    public static final String RES_TYPE_STRING = "string";

    private ResourceUtils() {

    }

    /**
     * Fetch the resource Id for the corresponding resource name and type
     *
     * @param context
     * @param resString
     * @param resType
     * @return A valid resource Id or 0 when no valid Id was found
     */
    public static final int getResIdFromResString(final Context context,
                                                  final String resString,
                                                  @ResType final String resType) {

        return context.getResources().getIdentifier(resString, resType, context.getPackageName());

    }
}
