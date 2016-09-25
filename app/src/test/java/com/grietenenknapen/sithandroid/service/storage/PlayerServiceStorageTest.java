package com.grietenenknapen.sithandroid.service.storage;

import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.BuildConfig;
import com.grietenenknapen.sithandroid.model.database.Favourite;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.database.relations.PlayerFavourite;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.DatabaseCompartment;
import nl.qbusict.cupboard.QueryResultIterable;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class PlayerServiceStorageTest {
    private SithDatabase dbMock;
    private SQLiteDatabase sqLiteDatabaseMock;
    private Cupboard cupboardMock;
    private PlayerServiceStorage playerServiceStorage;
    private Player oldPlayer, newPlayer;
    private List<Player> playerList;

    @Before
    public void setUp() {
        oldPlayer = Player.newBuilder()
                .favourites(new ArrayList<Favourite>())
                ._id(1)
                .name("john")
                .telephoneNumber("0471589366")
                .wins(1)
                .losses(0)
                .build();

        final List<Favourite> favourites = new ArrayList<>();
        final SithCard sithCard = SithCard.newBuilder()
                .cardType(GameCardType.BB8)
                .name("BB8")
                .imageResId("BB8")
                .build();

        final Favourite favourite = Favourite.newBuilder()
                .sithCard(sithCard)
                .timesUsed(1)
                .build();

        favourites.add(favourite);

        newPlayer = Player.newBuilder()
                .favourites(favourites)
                ._id(2)
                .name("Jos")
                .telephoneNumber("0471589366")
                .wins(1)
                .losses(0)
                .build();


        sqLiteDatabaseMock = Mockito.mock(SQLiteDatabase.class);
        cupboardMock = Mockito.mock(Cupboard.class);
        dbMock = Mockito.mock(SithDatabase.class);

        final QueryResultIterable queryResultIterableMock = Mockito.mock(QueryResultIterable.class);

        playerList = new ArrayList<>();
        playerList.add(oldPlayer);

        when(dbMock.openDatabase()).thenReturn(sqLiteDatabaseMock);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock)).thenReturn(Mockito.mock(DatabaseCompartment.class));

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(Player.class)).thenReturn(Mockito.mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(Player.class).query()).thenReturn(queryResultIterableMock);
        when(queryResultIterableMock.list()).thenReturn(playerList);

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class)).thenReturn(Mockito.mock(DatabaseCompartment.QueryBuilder.class));

        playerServiceStorage = new PlayerServiceStorage(dbMock, cupboardMock);
    }

    @Test
    public void testRetrievePlayerAndGetPlayerFromMap_Success() {

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString())).thenReturn(mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query()).thenReturn(mock(QueryResultIterable.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query().list()).thenReturn(new ArrayList<PlayerFavourite>());

        //First get it from database
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).get(Player.class, oldPlayer.getId())).thenReturn(oldPlayer);

        final ServiceCallBack<Player> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);

        playerServiceStorage.retrievePlayerById(oldPlayer.getId(), serviceCallBackMock);

        //Now get it from map
        playerServiceStorage.retrievePlayerById(oldPlayer.getId(), serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(serviceCallBackMock, Mockito.times(2)).onSuccess(oldPlayer);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), atLeastOnce()).query(PlayerFavourite.class);
    }

    @Test
    public void testRetrievePlayerAndGetPlayerFromMap_Fail() {
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString())).thenReturn(mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query()).thenReturn(mock(QueryResultIterable.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query().list()).thenReturn(new ArrayList<PlayerFavourite>());

        //First get it from database
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).get(Player.class, 0)).thenReturn(null);

        final ServiceCallBack<Player> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);

        playerServiceStorage.retrievePlayerById(0, serviceCallBackMock);

        //Now get it from map
        playerServiceStorage.retrievePlayerById(0, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(2)).openDatabase();
        verify(dbMock, Mockito.times(2)).closeDatabase();
        verify(serviceCallBackMock, Mockito.times(2)).onError(anyInt());
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), atLeastOnce()).query(PlayerFavourite.class);
    }


    @Test
    public void testRetrievePlayersAndGetPlayersFromMap_Success() {
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString())).thenReturn(mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query()).thenReturn(mock(QueryResultIterable.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query().list()).thenReturn(new ArrayList<PlayerFavourite>());

        //First get it from database

        final ServiceCallBack<List<Player>> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);

        playerServiceStorage.retrieveAllPlayers(serviceCallBackMock);

        //Now get it from map
        playerServiceStorage.retrieveAllPlayers(serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(serviceCallBackMock, Mockito.times(2)).onSuccess(playerList);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), atLeastOnce()).query(PlayerFavourite.class);
    }

    @Test
    public void testPutPlayer_New_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newPlayer)).thenReturn(newPlayer.getId());
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString(), anyString()))
                .thenReturn(mock(DatabaseCompartment.QueryBuilder.class));

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString(), anyString()).get())
                .thenReturn(null);

        playerServiceStorage.putPlayer(newPlayer, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayer);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayer.getFavourites().get(0));
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), times(1)).put(isA(PlayerFavourite.class));
        verify(serviceCallBackMock, Mockito.times(1)).onSuccess(anyLong());

        final ServiceCallBack<Player> serviceCallBackMockPlayer = Mockito.mock(ServiceCallBack.class);
        playerServiceStorage.retrievePlayerById(newPlayer.getId(), serviceCallBackMockPlayer);

        Robolectric.flushBackgroundThreadScheduler();

        verify(serviceCallBackMockPlayer).onSuccess(newPlayer);
    }

    @Test
    public void testPutPlayer_NotNew_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newPlayer)).thenReturn(newPlayer.getId());
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString(), anyString()))
                .thenReturn(mock(DatabaseCompartment.QueryBuilder.class));

        PlayerFavourite playerFavourite = PlayerFavourite.newBuilder()
                ._id(1)
                .favouriteId(newPlayer.getFavourites().get(0).getId())
                .playerId(newPlayer.getId())
                .build();

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString(), anyString()).get())
                .thenReturn(playerFavourite);

        playerServiceStorage.putPlayer(newPlayer, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        //TODO: test removal of PlayerFavourite and Favourites

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayer);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayer.getFavourites().get(0));
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), never()).put(isA(PlayerFavourite.class));
        verify(serviceCallBackMock, Mockito.times(1)).onSuccess(anyLong());

        //Get player from map
        final ServiceCallBack<Player> serviceCallBackMockPlayer = Mockito.mock(ServiceCallBack.class);
        playerServiceStorage.retrievePlayerById(newPlayer.getId(), serviceCallBackMockPlayer);

        verify(serviceCallBackMockPlayer).onSuccess(newPlayer);
    }

    @Test
    public void testRemovePlayer_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        final ServiceCallBack<Void> serviceCallBackRemoveMock = Mockito.mock(ServiceCallBack.class);

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newPlayer)).thenReturn(newPlayer.getId());
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString(), anyString()))
                .thenReturn(mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()))
                .thenReturn(mock(DatabaseCompartment.QueryBuilder.class));

        PlayerFavourite playerFavourite = PlayerFavourite.newBuilder()
                ._id(1)
                .favouriteId(newPlayer.getFavourites().get(0).getId())
                .playerId(newPlayer.getId())
                .build();

        final List<PlayerFavourite> playerFavourites = new ArrayList<>();
        playerFavourites.add(playerFavourite);

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString(), anyString()).get())
                .thenReturn(playerFavourite);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query())
                .thenReturn(mock(QueryResultIterable.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(PlayerFavourite.class).withSelection(anyString(), anyString()).query().list())
                .thenReturn(playerFavourites);

        playerServiceStorage.putPlayer(newPlayer, serviceCallBackMock);
        playerServiceStorage.removePlayer(newPlayer, serviceCallBackRemoveMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(2)).openDatabase();
        verify(dbMock, Mockito.times(2)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).delete(newPlayer);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).delete(Favourite.class, newPlayer.getFavourites().get(0).getId());
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).delete(isA(PlayerFavourite.class));
        verify(serviceCallBackRemoveMock, Mockito.times(1)).onSuccess(null);


        final ServiceCallBack<Player> serviceCallBackMockPlayer = Mockito.mock(ServiceCallBack.class);
        playerServiceStorage.retrievePlayerById(newPlayer.getId(), serviceCallBackMockPlayer);

        Robolectric.flushBackgroundThreadScheduler();

        verify(serviceCallBackMockPlayer).onError(anyInt());
    }
}

