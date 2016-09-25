package com.grietenenknapen.sithandroid.service.storage;

import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.GamePlayer;
import com.grietenenknapen.sithandroid.model.database.PlayedGame;
import com.grietenenknapen.sithandroid.model.database.relations.GamePlayerRelations;
import com.grietenenknapen.sithandroid.service.PlayedGameService;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;
import com.grietenenknapen.sithandroid.service.storage.SithDatabase;
import com.grietenenknapen.sithandroid.service.storage.tools.DbSqlHelperUtils;
import com.grietenenknapen.sithandroid.service.storage.tools.StorageBackgroundWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.QueryResultIterable;

public class PlayedGameServiceStorage implements PlayedGameService {
    private final Map<Long, PlayedGame> playedGameMap;
    private final SithDatabase sithDatabase;
    private final Cupboard cupboard;


    public PlayedGameServiceStorage(final SithDatabase sithDatabase, final Cupboard cupboard) {
        this.sithDatabase = sithDatabase;
        playedGameMap = Collections.synchronizedMap(new LinkedHashMap<Long, PlayedGame>());
        this.cupboard = cupboard;
    }


    @Override
    public void retrievePlayedGameById(final long id, final ServiceCallBack<PlayedGame> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<PlayedGame>() {
            @Override
            public PlayedGame onExecute() {
                if (!playedGameMap.isEmpty() && playedGameMap.containsKey(id)) {
                    return playedGameMap.get(id);
                }
                final SQLiteDatabase db = sithDatabase.openDatabase();

                final PlayedGame playedGame = cupboard.withDatabase(db).get(PlayedGame.class, id);

                List<GamePlayer> gamePlayers = findGamePlayers(id, db, cupboard);

                sithDatabase.closeDatabase();

                if (playedGame != null) {
                    playedGame.setGamePlayers(gamePlayers);
                    playedGameMap.put(playedGame.getId(), playedGame);
                    return playedGame;
                } else {
                    return null;
                }
            }

            @Override
            public void postExecute(PlayedGame response) {
                if (response != null) {
                    serviceCallBack.onSuccess(response);
                } else {
                    serviceCallBack.onError(R.string.played_game_not_found);
                }
            }
        });


    }

    @Override
    public void retrieveAllPlayedGames(final ServiceCallBack<List<PlayedGame>> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<List<PlayedGame>>() {
            @Override
            public List<PlayedGame> onExecute() {
                if (!playedGameMap.isEmpty()) {
                    return new ArrayList<>(playedGameMap.values());
                }

                final SQLiteDatabase db = sithDatabase.openDatabase();

                QueryResultIterable<PlayedGame> playedGamesItr = null;
                List<PlayedGame> playedGames = null;

                boolean success = true;

                try {
                    playedGamesItr = cupboard.withDatabase(db).query(PlayedGame.class).query();

                    playedGames = playedGamesItr.list();
                    playedGameMap.clear();

                    for (PlayedGame playedGame : playedGames) {
                        List<GamePlayer> gamePlayers = findGamePlayers(playedGame.getId(), db, cupboard);
                        playedGame.setGamePlayers(gamePlayers);
                    }
                } catch (Exception e) {
                    success = false;
                } finally {
                    if (playedGamesItr != null) {
                        playedGamesItr.close();
                    }
                }

                sithDatabase.closeDatabase();

                if (success) {
                    return playedGames;
                } else {
                    return null;
                }
            }

            @Override
            public void postExecute(List<PlayedGame> response) {
                if (response != null) {
                    serviceCallBack.onSuccess(response);
                } else {
                    serviceCallBack.onError(R.string.service_fetch_data_error);
                }
            }
        });
    }

    @Override
    public void putPlayedGame(final PlayedGame playedGame, final ServiceCallBack<Long> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Long>() {
            @Override
            public Long onExecute() {
                final SQLiteDatabase db = sithDatabase.openDatabase();
                final long id = cupboard.withDatabase(db).put(playedGame);

                //We don't need to delete because the favourites only get added or udpated
                for (GamePlayer gamePlayer : playedGame.getGamePlayers()) {
                    final long gamePlayerId = cupboard.withDatabase(db).put(gamePlayer);

                    final GamePlayerRelations existingGamePlayerRelations = cupboard.withDatabase(db)
                            .query(GamePlayerRelations.class)
                            .withSelection(GamePlayerRelations.GAME_PLAYER_ID + " = ? ", String.valueOf(gamePlayerId)).get();

                    if (existingGamePlayerRelations == null) {

                        final GamePlayerRelations gamePlayerRelations = GamePlayerRelations.newBuilder()
                                .gamePlayerId(gamePlayerId)
                                .playedGameId(playedGame.getId())
                                .sithCardIds(gamePlayer.getSithCardIds())
                                .build();
                        cupboard.withDatabase(db).put(gamePlayerRelations);
                    }

                    //TODO: remove all the playerFavourites and favourites that are not in the playedgame anymore
                }

                playedGameMap.put(id, playedGame);

                sithDatabase.closeDatabase();

                return id;
            }

            @Override
            public void postExecute(Long response) {
                serviceCallBack.onSuccess(response);
            }
        });
    }

    @Override
    public void removePlayedGame(final PlayedGame playedGame, final ServiceCallBack<Void> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Void>() {
            @Override
            public Void onExecute() {
                final SQLiteDatabase db = sithDatabase.openDatabase();

                cupboard.withDatabase(db).delete(playedGame);

                for (GamePlayer gamePlayer : playedGame.getGamePlayers()) {
                    cupboard.withDatabase(db).delete(GamePlayer.class, gamePlayer.getId());
                }

                cupboard.withDatabase(db).delete(GamePlayerRelations.class,
                        GamePlayerRelations.PLAYED_GAME_ID + " = ? ", String.valueOf(playedGame.getId()));

                sithDatabase.closeDatabase();

                return null;
            }

            @Override
            public void postExecute(Void response) {
                serviceCallBack.onSuccess(null);
            }
        });
    }

    private static List<GamePlayer> findGamePlayers(final long id, final SQLiteDatabase db, final Cupboard cupboard) {
        QueryResultIterable<GamePlayerRelations> gamePlayerRelationsItr = null;

        List<GamePlayer> gamePlayers = new ArrayList<>();

        try {
            gamePlayerRelationsItr = cupboard.withDatabase(db).query(GamePlayerRelations.class)
                    .withSelection(GamePlayerRelations.PLAYED_GAME_ID + " = ?", String.valueOf(id)).query();

            for (GamePlayerRelations gamePlayerRelation : gamePlayerRelationsItr) {
                GamePlayer gamePlayer = DbSqlHelperUtils.retrieveGamePlayerForGamePlayerRelation(db,
                        cupboard, gamePlayerRelation);
                if (gamePlayer != null) {
                    gamePlayers.add(gamePlayer);
                }
            }
        } finally {
            if (gamePlayerRelationsItr != null) {
                gamePlayerRelationsItr.close();
            }
        }
        return gamePlayers;
    }
}
