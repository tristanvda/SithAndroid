package com.grietenenknapen.sithandroid.service.storage.tools;


import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.model.database.Favourite;
import com.grietenenknapen.sithandroid.model.database.GamePlayer;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.database.relations.GamePlayerRelations;
import com.grietenenknapen.sithandroid.model.database.relations.PlayerFavourite;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.Cupboard;

public class DbSqlHelperUtils {
    public static List<SithCard> retrieveSithCardsForGamePlayer(final SQLiteDatabase db,
                                                                final Cupboard cupboard,
                                                                final long[] sithCardIds) {

        final List<SithCard> sithCards = new ArrayList<>();

        for (long id : sithCardIds) {
            SithCard sithCard = cupboard.withDatabase(db).get(SithCard.class, id);
            if (sithCard != null) {
                sithCards.add(sithCard);
            }
        }

        return sithCards;
    }

    public static Player retrievePlayerForGamePlayer(final SQLiteDatabase db,
                                                     final Cupboard cupboard,
                                                     final long playerId) {

        Player player = cupboard.withDatabase(db).get(Player.class, playerId);
        List<Favourite> favourites = retrieveFavouritesForPlayer(db, cupboard, playerId);

        if (player != null) {
            player.setFavourites(favourites);
        }

        return player;
    }

    public static GamePlayer retrieveGamePlayerForGamePlayerRelation(final SQLiteDatabase db,
                                                                     final Cupboard cupboard,
                                                                     final GamePlayerRelations gamePlayerRelations) {

        GamePlayer gamePlayer = cupboard.withDatabase(db).get(GamePlayer.class, gamePlayerRelations.getGamePlayerId());

        if (gamePlayer == null){
            return null;
        }

        gamePlayer.setPlayer(retrievePlayerForGamePlayer(db, cupboard, gamePlayerRelations.getGamePlayerId()));
        gamePlayer.setSithCards(retrieveSithCardsForGamePlayer(db, cupboard, gamePlayerRelations.getSithCardIds()));

        return gamePlayer;
    }

    public static List<Favourite> retrieveFavouritesForPlayer(final SQLiteDatabase db,
                                                              final Cupboard cupboard,
                                                              final long playerId) {
        final List<PlayerFavourite> existingPlayerFavourites = cupboard.withDatabase(db)
                .query(PlayerFavourite.class)
                .withSelection(PlayerFavourite.PLAYER_ID + " = ?", String.valueOf(playerId)).query().list();

        final List<Favourite> favourites = new ArrayList<>();

        for (PlayerFavourite playerFavourite : existingPlayerFavourites) {
            Favourite favourite = cupboard.withDatabase(db).get(Favourite.class, playerFavourite.get_id());
            if (favourite != null) {
                favourites.add(favourite);
            }
        }

        return favourites;
    }

}
