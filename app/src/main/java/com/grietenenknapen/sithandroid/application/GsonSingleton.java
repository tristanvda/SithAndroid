package com.grietenenknapen.sithandroid.application;

import com.google.gson.Gson;

public final class GsonSingleton {

    private static Gson gson;

    private GsonSingleton() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

}
