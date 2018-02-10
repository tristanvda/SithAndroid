package com.grietenenknapen.sithandroid.util;


import android.content.Context;
import android.util.Log;

import com.grietenenknapen.sithandroid.application.Settings;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    private static SithMusicPlayer instance;
    private final Map<Integer, Set<Integer>> cacheMap = new ConcurrentHashMap<>();

    private static final String TAG = "Sith_music_player";

    private SithMusicPlayer() {
        cacheMap.put(MUSIC_TYPE_HAN_SOLO, new HashSet<Integer>());
        cacheMap.put(MUSIC_TYPE_BB8, new HashSet<Integer>());
        cacheMap.put(MUSIC_TYPE_SITH, new HashSet<Integer>());
        cacheMap.put(MUSIC_TYPE_MAZ_KANATA, new HashSet<Integer>());
        cacheMap.put(MUSIC_TYPE_BOBA_FETT, new HashSet<Integer>());
    }

    public static SithMusicPlayer getInstance() {
        if (instance == null) {
            instance = new SithMusicPlayer();
        }

        return instance;
    }

    /*
    * Finds a random key that is not in cache yet
    * This clears the cache when the cache is full
    * When the cache is not found, this will just return a random number
     */
    private int getNewMusicNumberFromCache(final int key, final int minNum, final int maxNum) {
        final Set<Integer> cache = cacheMap.get(key);
        if (cache != null) {
            if (cache.size() >= maxNum + 1 - minNum) {
                cache.clear();
            }
            final int random = MathUtils.getRandomWithExclusion(minNum, maxNum, toInt(cache));
            cache.add(random);
            return random;
        }
        return MathUtils.generateRandomInteger(minNum, maxNum);
    }

    private int[] toInt(Set<Integer> set) {
        int[] a = new int[set.size()];
        int i = 0;
        for (Integer val : set) a[i++] = val;
        return a;
    }

    public void playMusic(final Context context, final int musicType) {

        if (!Settings.isMusicEnabled(context)) {
            return;
        }

        String prefix = HAN_SOLO_PREFIX;
        int number = 1;

        switch (musicType) {
            case MUSIC_TYPE_HAN_SOLO:
                prefix = HAN_SOLO_PREFIX;
                number = getNewMusicNumberFromCache(MUSIC_TYPE_HAN_SOLO, 1, 5);
                break;
            case MUSIC_TYPE_BB8:
                prefix = BB8_PREFIX;
                number = getNewMusicNumberFromCache(MUSIC_TYPE_BB8, 1, 3);
                break;
            case MUSIC_TYPE_SITH:
                prefix = SITH_PREFIX;
                number = getNewMusicNumberFromCache(MUSIC_TYPE_SITH, 1, 6);
                break;
            case MUSIC_TYPE_MAZ_KANATA:
                prefix = MAZ_KANATA_PREFIX;
                number = getNewMusicNumberFromCache(MUSIC_TYPE_MAZ_KANATA, 1, 6);
                break;
            case MUSIC_TYPE_BOBA_FETT:
                prefix = BOBA_FETT_PREFIX;
                number = getNewMusicNumberFromCache(MUSIC_TYPE_BOBA_FETT, 1, 6);
                break;
        }

        final String resourceResStr = prefix + String.valueOf(number);
        final int resourceId = ResourceUtils.getResIdFromResString(context, resourceResStr, ResourceUtils.RES_TYPE_RAW);

        MediaSoundPlayer.playSoundFileLoop(context, resourceId, TAG);
    }

    public void stopPlaying() {
        MediaSoundPlayer.stopPlayer(TAG);
    }

}
