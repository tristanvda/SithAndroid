package com.grietenenknapen.sithandroid.application;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.grietenenknapen.sithandroid.service.PlayedGameService;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.service.storage.PlayedGameServiceStorage;
import com.grietenenknapen.sithandroid.service.storage.PlayerServiceStorage;
import com.grietenenknapen.sithandroid.service.storage.SithCardServiceStorage;
import com.grietenenknapen.sithandroid.service.storage.SithDatabase;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardFactory;

public class SithApplication extends Application {
    private PlayerService playerService;
    private PlayedGameService playedGameService;
    private SithCardService sithCardService;

    @Override
    public void onCreate() {
        super.onCreate();
        Bugsnag.init(this);

        final Cupboard cupboard = CupboardFactory.cupboard();
        SithDatabase.initializeInstance(new SithDatabase.DbHelper(this));

        final SithDatabase sithDatabase = SithDatabase.getInstance();

        playerService = new PlayerServiceStorage(sithDatabase, cupboard);
        playedGameService = new PlayedGameServiceStorage(sithDatabase, cupboard);
        sithCardService = new SithCardServiceStorage(sithDatabase, cupboard);
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    public PlayedGameService getPlayedGameService() {
        return playedGameService;
    }

    public SithCardService getSithCardService() {
        return sithCardService;
    }
}
