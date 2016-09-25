package com.grietenenknapen.sithandroid.service.storage;

import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.Favourite;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.relations.PlayerFavourite;
import com.grietenenknapen.sithandroid.service.PlayerService;
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

public class PlayerServiceStorage implements PlayerService {
    private final Map<Long, Player> playerMap;
    private final SithDatabase sithDatabase;
    private final Cupboard cupboard;

    public PlayerServiceStorage(final SithDatabase sithDatabase, final Cupboard cupboard) {
        this.sithDatabase = sithDatabase;
        playerMap = Collections.synchronizedMap(new LinkedHashMap<Long, Player>());
        this.cupboard = cupboard;
    }

    @Override
    public void retrievePlayerById(final long id, final ServiceCallBack<Player> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Player>() {

            @Override
            public Player onExecute() {
                if (!playerMap.isEmpty() && playerMap.containsKey(id)) {
                    return playerMap.get(id);
                }
                final SQLiteDatabase db = sithDatabase.openDatabase();

                final Player player = cupboard.withDatabase(db).get(Player.class, id);

                final List<Favourite> favourites = DbSqlHelperUtils.retrieveFavouritesForPlayer(db, cupboard, id);

                sithDatabase.closeDatabase();

                if (player != null) {
                    player.setFavourites(favourites);
                    playerMap.put(player.getId(), player);
                }
                return player;
            }

            @Override
            public void postExecute(Player response) {
                if (response != null) {
                    serviceCallBack.onSuccess(response);
                } else {
                    serviceCallBack.onError(R.string.player_not_found);
                }
            }
        });
    }

    @Override
    public void retrieveAllPlayers(final ServiceCallBack<List<Player>> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<List<Player>>() {
            @Override
            public List<Player> onExecute() {
                if (!playerMap.isEmpty()) {
                    return new ArrayList<Player>(playerMap.values());
                }
                final SQLiteDatabase db = sithDatabase.openDatabase();

                QueryResultIterable<Player> playersItr = null;
                List<Player> players = null;

                boolean success = true;

                try {
                    playersItr = cupboard.withDatabase(db).query(Player.class).query();

                    players = playersItr.list();
                    playerMap.clear();

                    for (Player player : players) {
                        final List<Favourite> favourites = DbSqlHelperUtils.retrieveFavouritesForPlayer(db, cupboard, player.getId());
                        player.setFavourites(favourites);
                        playerMap.put(player.getId(), player);
                    }
                } catch (Exception e) {
                    success = false;
                } finally {
                    if (playersItr != null) {
                        playersItr.close();
                    }
                }

                sithDatabase.closeDatabase();

                if (success) {
                    return players;
                } else {
                    return null;
                }
            }

            @Override
            public void postExecute(List<Player> response) {
                if (response != null) {
                    serviceCallBack.onSuccess(response);
                } else {
                    serviceCallBack.onError(R.string.service_fetch_data_error);
                }

            }
        });
    }


    @Override
    public void putPlayer(final Player player, final ServiceCallBack<Long> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Long>() {
            @Override
            public Long onExecute() {
                final SQLiteDatabase db = sithDatabase.openDatabase();
                final long id = cupboard.withDatabase(db).put(player);

                //We don't need to delete because the favourites only get added or udpated
                for (Favourite favourite : player.getFavourites()) {
                    final long favouriteId = cupboard.withDatabase(db).put(favourite);

                    //TODO: remove all the playerFavourites and favourites that are not in the user anymore

                    final PlayerFavourite existingPlayerFavourite = cupboard.withDatabase(db)
                            .query(PlayerFavourite.class)
                            .withSelection(PlayerFavourite.FAVOURITE_ID + " = ? AND " + PlayerFavourite.PLAYER_ID + " = ?",
                                    new String[]{String.valueOf(favouriteId), String.valueOf(player.getId())}).get();

                    if (existingPlayerFavourite == null) {
                        final PlayerFavourite newPlayerFavourite = PlayerFavourite.newBuilder()
                                .favouriteId(favouriteId)
                                .playerId(player.getId())
                                .build();
                        cupboard.withDatabase(db).put(newPlayerFavourite);
                    }
                }

                playerMap.put(id, player);

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
    public void removePlayer(final Player player, final ServiceCallBack<Void> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Boolean>() {
            @Override
            public Boolean onExecute() {
                final SQLiteDatabase db = sithDatabase.openDatabase();

                QueryResultIterable<PlayerFavourite> playerFavouritesItr = null;

                boolean success = true;

                try {
                    playerFavouritesItr = cupboard.withDatabase(db)
                            .query(PlayerFavourite.class).withSelection(PlayerFavourite.PLAYER_ID + " = ?", String.valueOf(player.getId())).query();
                    for (PlayerFavourite playerFavourite : playerFavouritesItr.list()) {
                        cupboard.withDatabase(db).delete(Favourite.class, playerFavourite.getFavouriteId());
                        cupboard.withDatabase(db).delete(playerFavourite);
                    }

                    cupboard.withDatabase(db).delete(player);
                    playerMap.remove(player.getId());
                } catch (Exception e) {
                    success = false;
                } finally {
                    // close the cursor
                    if (playerFavouritesItr != null) {
                        playerFavouritesItr.close();
                    }
                }

                sithDatabase.closeDatabase();

                return success;
            }

            @Override
            public void postExecute(Boolean succeed) {
                if (succeed) {
                    serviceCallBack.onSuccess(null);
                } else {
                    serviceCallBack.onError(R.string.service_fetch_data_error);
                }
            }
        });

    }

}
