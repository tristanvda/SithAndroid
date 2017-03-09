package com.grietenenknapen.sithandroid.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.grietenenknapen.sithandroid.maingame.MainGame;

public final class Settings {
    private final static String PREFERENCES_SAVES = "com.grietenenknapen.sithandroid.prefs.saves";
    private final static String SAVES_MAIN_GAME = "main_game_save";
    private final static String SAVES_MAIN_GAME_FLAG = "main_game_save_flag";
    private final static String SETTING_MUSIC = "music";
    private final static String SETTING_SMS = "sms";
    private final static String SETTINGS_BATTERY_SAVING = "battery_saving";
    private final static String RANDOM_COMMENTS = "random_comments";

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
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_SAVES, Context.MODE_PRIVATE);
        final String json = preferences.getString(SAVES_MAIN_GAME, null);

        try {
            return getGson().fromJson(json, MainGame.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static void setSavedMainGame(final Context context,
                                        final MainGame mainGame) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_SAVES, Context.MODE_PRIVATE);

        String json = getGson().toJson(mainGame);
        preferences.edit().putString(SAVES_MAIN_GAME, json).apply();
    }


    public static boolean isMainGameSaved(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_SAVES, Context.MODE_PRIVATE);

        return preferences.getBoolean(SAVES_MAIN_GAME_FLAG, false);
    }

    public static void setMainGameSaved(final Context context,
                                        final boolean mainGameSaved) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_SAVES, Context.MODE_PRIVATE);

        preferences.edit().putBoolean(SAVES_MAIN_GAME_FLAG, mainGameSaved).apply();

    }

    public static boolean isMusicEnabled(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getBoolean(SETTING_MUSIC, false);
    }

    public static boolean isSmsEnabled(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getBoolean(SETTING_SMS, false);
    }

    public static boolean isBatterySavingMode(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getBoolean(SETTINGS_BATTERY_SAVING, false);
    }

    public static boolean isRandomComments(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getBoolean(RANDOM_COMMENTS, false);
    }
}
