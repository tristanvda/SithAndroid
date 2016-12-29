package com.grietenenknapen.sithandroid.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.grietenenknapen.sithandroid.maingame.MainGame;

public final class Settings {
    private final static String PREFERENCES_SAVES = "com.grietenenknapen.sithandroid.prefs.saves";
    private final static String SAVES_MAIN_GAME = "main_game_save";
    private final static String SAVES_MAIN_GAME_FLAG = "main_game_save_flag";
    private static Gson GSON_INSTANCE;

    private Settings() {
    }

    private static Gson getGson() {
        if (GSON_INSTANCE == null) {
            GSON_INSTANCE = new Gson();
        }

        return GSON_INSTANCE;
    }

    public static MainGame getSavedMainGame(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SAVES_MAIN_GAME, Context.MODE_PRIVATE);
        final String json = preferences.getString(SAVES_MAIN_GAME, null);

        try {
            return getGson().fromJson(json, MainGame.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static void setSavedMainGame(final Context context,
                                        final MainGame mainGame) {
        SharedPreferences preferences = context.getSharedPreferences(SAVES_MAIN_GAME, Context.MODE_PRIVATE);

        String json = getGson().toJson(mainGame);
        preferences.edit().putString(SAVES_MAIN_GAME, json).apply();
    }


    public static boolean isMainGameSaved(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SAVES_MAIN_GAME, Context.MODE_PRIVATE);

        return preferences.getBoolean(SAVES_MAIN_GAME_FLAG, false);
    }

    public static void setMainGameSaved(final Context context,
                                        final boolean mainGameSaved) {
        SharedPreferences preferences = context.getSharedPreferences(SAVES_MAIN_GAME, Context.MODE_PRIVATE);

        preferences.edit().putBoolean(SAVES_MAIN_GAME_FLAG, mainGameSaved).apply();

    }
}
