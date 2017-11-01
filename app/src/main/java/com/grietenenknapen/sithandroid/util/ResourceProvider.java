package com.grietenenknapen.sithandroid.util;

import android.content.Context;

public final class ResourceProvider {

    private final Context context;

    public ResourceProvider(final Context context) {
        this.context = context.getApplicationContext();
    }

    public final String getString(final int stringId) {
        return context.getString(stringId);
    }
}
