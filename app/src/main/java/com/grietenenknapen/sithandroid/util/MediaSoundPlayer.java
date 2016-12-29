package com.grietenenknapen.sithandroid.util;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MediaSoundPlayer {

    private static MediaSoundPlayer instance = null;
    private Map<String, MediaPlayer> mediaPlayerMap;
    private Map<String, OnSoundPlayListener> soundPlayListenerMap;

    //TODO: refactor this

    public interface OnSoundPlayListener {
        void onSoundPlayDone();
    }

    private MediaSoundPlayer() {
        mediaPlayerMap = Collections.synchronizedMap(new HashMap<String, MediaPlayer>());
        soundPlayListenerMap = Collections.synchronizedMap(new HashMap<String, OnSoundPlayListener>());
    }

    private static MediaSoundPlayer getInstance() {
        if (instance == null) {
            instance = new MediaSoundPlayer();
        }
        return instance;
    }

    private void stop(final String tag) {
        final MediaPlayer mediaPlayer = mediaPlayerMap.get(tag);
        if (mediaPlayer != null) {
            mediaPlayerMap.remove(tag);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public void setSoundPlayListener(OnSoundPlayListener soundPlayListener, final String tag) {
        if (soundPlayListener == null) {
            soundPlayListenerMap.remove(tag);
        } else {
            soundPlayListenerMap.put(tag, soundPlayListener);
        }
    }

    private void playNewSoundFile(final Context context, final int soundRes, final String tag, final boolean loop) {
        final MediaPlayer mediaPlayer;

        if (mediaPlayerMap.containsKey(tag)) {
            stop(tag);
        }

        mediaPlayer = MediaPlayer.create(context, soundRes);
        mediaPlayer.setLooping(loop);
        mediaPlayerMap.put(tag, mediaPlayer);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                final String tag = getTag(mp);

                if (tag == null) {
                    return;
                }

                stop(tag);

                if (soundPlayListenerMap.containsKey(tag)) {
                    soundPlayListenerMap.get(tag).onSoundPlayDone();
                }

            }
        });

        mediaPlayer.start();
    }

    private String getTag(final MediaPlayer mediaPlayer) {
        for (Map.Entry<String, MediaPlayer> entry : mediaPlayerMap.entrySet()) {
            if (entry.getValue().equals(mediaPlayer)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private MediaPlayer getMediaPlayer(final String tag) {
        return mediaPlayerMap.get(tag);
    }

    public static boolean isPlaying(final String tag) {
        MediaSoundPlayer mediaSoundPlayer = MediaSoundPlayer.getInstance();

        final MediaPlayer mediaPlayer = mediaSoundPlayer.getMediaPlayer(tag);
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static void setMediaSoundPlayListener(final OnSoundPlayListener soundPlayListener, final String tag) {
        MediaSoundPlayer mediaSoundPlayer = MediaSoundPlayer.getInstance();
        mediaSoundPlayer.setSoundPlayListener(soundPlayListener, tag);
    }

    public static void playSoundFile(final Context context, final int soundRes, final String tag) {
        MediaSoundPlayer mediaSoundPlayer = MediaSoundPlayer.getInstance();

        mediaSoundPlayer.playNewSoundFile(context, soundRes, tag, false);
    }

    public static void playSoundFileLoop(final Context context, final int soundRes, final String tag) {
        MediaSoundPlayer mediaSoundPlayer = MediaSoundPlayer.getInstance();

        mediaSoundPlayer.playNewSoundFile(context, soundRes, tag, true);
    }

    public static void stopPlayer(final String tag) {

        MediaSoundPlayer mediaSoundPlayer = MediaSoundPlayer.getInstance();
        mediaSoundPlayer.stop(tag);
    }
}
