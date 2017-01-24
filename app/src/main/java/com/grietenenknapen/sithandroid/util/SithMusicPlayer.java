package com.grietenenknapen.sithandroid.util;


import android.content.Context;

import com.grietenenknapen.sithandroid.application.Settings;

public class SithMusicPlayer {

    public static final int MUSIC_TYPE_HAN_SOLO = 0;
    public static final int MUSIC_TYPE_BB8 = 1;
    public static final int MUSIC_TYPE_SITH = 2;
    public static final int MUSIC_TYPE_MAZ_KANATA = 3;
    public static final int MUSIC_TYPE_BOBA_FETT = 4;

    private static final String HAN_SOLO_PREFIX = "han_solo_";
    private static final String BB8_PREFIX = "bb8_";
    private static final String SITH_PREFIX = "sith_";
    private static final String BOBA_FETT_PREFIX = "boba_fett_";
    private static final String MAZ_KANATA_PREFIX = "maz_kanata_";

    private static final String TAG = "Sith_music_player";

    private SithMusicPlayer() {
    }

    public static void playMusic(final Context context, final int musicType) {

        if (!Settings.isMusicEnabled(context)) {
            return;
        }

        String prefix = HAN_SOLO_PREFIX;
        int number = 1;

        switch (musicType) {
            case MUSIC_TYPE_HAN_SOLO:
                prefix = HAN_SOLO_PREFIX;
                number = MathUtils.generateRandomInteger(1, 5);
                break;
            case MUSIC_TYPE_BB8:
                prefix = BB8_PREFIX;
                number = MathUtils.generateRandomInteger(1, 3);
                break;
            case MUSIC_TYPE_SITH:
                prefix = SITH_PREFIX;
                number = MathUtils.generateRandomInteger(1, 6);
                break;
            case MUSIC_TYPE_MAZ_KANATA:
                prefix = MAZ_KANATA_PREFIX;
                number = MathUtils.generateRandomInteger(1, 6);
                break;
            case MUSIC_TYPE_BOBA_FETT:
                prefix = BOBA_FETT_PREFIX;
                number = MathUtils.generateRandomInteger(1, 6);
                break;
        }

        final String resourceResStr = prefix + String.valueOf(number);
        final int resourceId = ResourceUtils.getResIdFromResString(context, resourceResStr, ResourceUtils.RES_TYPE_RAW);

        MediaSoundPlayer.playSoundFileLoop(context, resourceId, TAG);
    }

    public static void stopPlaying() {
        MediaSoundPlayer.stopPlayer(TAG);
    }


}
