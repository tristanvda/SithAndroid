package com.grietenenknapen.sithandroid.service.storage;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grietenenknapen.sithandroid.model.database.Favourite;
import com.grietenenknapen.sithandroid.model.database.GamePlayer;
import com.grietenenknapen.sithandroid.model.database.PlayedGame;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.database.relations.GamePlayerRelations;
import com.grietenenknapen.sithandroid.model.database.relations.PlayerFavourite;
import com.grietenenknapen.sithandroid.service.storage.tools.DatabaseDefaults;
import com.grietenenknapen.sithandroid.service.storage.tools.DatabaseManager;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class SithDatabase extends DatabaseManager<SithDatabase.DbHelper> {

    private static SithDatabase instance;

    protected SithDatabase(DbHelper helper) {
        super(helper);
    }

    public static synchronized void initializeInstance(DbHelper helper) {
        if (instance == null) {
            instance = new SithDatabase(helper);
        }
    }

    public static synchronized SithDatabase getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public static class DbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "sithDatabase.db";
        private static final int DATABASE_VERSION = 1;

        static {
            // register our models
            cupboard().register(Favourite.class);
            cupboard().register(GamePlayer.class);
            cupboard().register(PlayedGame.class);
            cupboard().register(Player.class);
            cupboard().register(SithCard.class);
            cupboard().register(GamePlayerRelations.class);
            cupboard().register(PlayerFavourite.class);
        }

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            cupboard().withDatabase(sqLiteDatabase).createTables();
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardBB8());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardBB8());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardChewBacca());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardDarthMaul());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardDartVader());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardFinn());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardHanSolo());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardKyloRen());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardLuke());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardMazKanata());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardObiWanKenobi());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardRey());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardTheEmperor());
            cupboard().withDatabase(sqLiteDatabase).put(DatabaseDefaults.getSithCardYoda());
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            cupboard().withDatabase(sqLiteDatabase).upgradeTables();
        }
    }
}
