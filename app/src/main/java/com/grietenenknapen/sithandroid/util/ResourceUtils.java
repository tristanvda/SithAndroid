package com.grietenenknapen.sithandroid.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.StringDef;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class ResourceUtils {

    @Retention(SOURCE)
    @StringDef({RES_TYPE_RAW, RES_TYPE_DRAWABLE, RES_TYPE_STRING, RES_TYPE_COLOR})
    public @interface ResType {
    }

    public static final String RES_TYPE_RAW = "raw";
    public static final String RES_TYPE_COLOR = "color";
    public static final String RES_TYPE_DRAWABLE = "drawable";
    public static final String RES_TYPE_STRING = "string";

    private static final double DEFAULT_CARD_ITEM_PERCENTAGE = 0.40;

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
    public static int getResIdFromResString(final Context context,
                                            final String resString,
                                            @ResType final String resType) {

        return context.getResources().getIdentifier(resString, resType, context.getPackageName());

    }

    public static float getPxFromDp(final Context context, final float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int getScreenWidth(final WindowManager windowManager) {
        Display display = windowManager.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getDefaultCardItemSize(final WindowManager windowManager) {
        return (int) (getScreenWidth(windowManager) * DEFAULT_CARD_ITEM_PERCENTAGE);
    }
}
