package com.grietenenknapen.sithandroid.service.storage;

import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.BuildConfig;
import com.grietenenknapen.sithandroid.model.database.GamePlayer;
import com.grietenenknapen.sithandroid.model.database.PlayedGame;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.database.relations.GamePlayerRelations;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.model.game.GameSide;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class PlayedGameServiceStorageTest {
    private SithDatabase dbMock;
    private SQLiteDatabase sqLiteDatabaseMock;
    private Cupboard cupboardMock;
    private PlayedGameServiceStorage playedGameServiceStorage;
    private PlayedGame newPlayedGame;
    private List<GamePlayerRelations> gamePlayerRelationsList;

    @Before
    public void setUp() {
        final List<SithCard> sithCards = new ArrayList<>();
        sithCards.add(SithCard.newBuilder()
                ._id(1L)
                .cardType(GameCardType.BB8)
                .imageResId("test" )
                .build());


        final List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(GamePlayer.newBuilder()
                ._id(1L)
                .player(Player.newBuilder().build())
                .side(GameSide.JEDI)
                .sithCards(sithCards)
                .build());

        gamePlayerRelationsList = new ArrayList<>();

        gamePlayerRelationsList.add(GamePlayerRelations.newBuilder()
                ._id(1)
                .gamePlayerId(2)
                .playedGameId(3)
                .sithCardIds(new long[]{1, 2})
                .build());

        newPlayedGame = PlayedGame.newBuilder()
                ._id(1L)
                .rounds(1)
                .startTime(1)
                .stopTime(1)
                .gamePlayers(gamePlayers)
                .build();

        sqLiteDatabaseMock = Mockito.mock(SQLiteDatabase.class);
        cupboardMock = Mockito.mock(Cupboard.class);
        dbMock = Mockito.mock(SithDatabase.class);

        final QueryResultIterable queryResultIterableMock = Mockito.mock(QueryResultIterable.class);

        when(dbMock.openDatabase()).thenReturn(sqLiteDatabaseMock);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock)).thenReturn(Mockito.mock(DatabaseCompartment.class));

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(GamePlayerRelations.class)).thenReturn(Mockito.mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(GamePlayerRelations.class).query()).thenReturn(queryResultIterableMock);

        playedGameServiceStorage = new PlayedGameServiceStorage(dbMock, cupboardMock);
    }

    @Test
    public void testPutPlayer_New_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newPlayedGame)).thenReturn(newPlayedGame.getId());
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(GamePlayerRelations.class).withSelection(anyString(), anyString()))
                .thenReturn(mock(DatabaseCompartment.QueryBuilder.class));

        playedGameServiceStorage.putPlayedGame(newPlayedGame, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayedGame);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayedGame.getGamePlayers().get(0));
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), times(1)).put(isA(GamePlayerRelations.class));
        verify(serviceCallBackMock, Mockito.times(1)).onSuccess(anyLong());

        final ServiceCallBack<PlayedGame> serviceCallBackMockPlayer = Mockito.mock(ServiceCallBack.class);
        playedGameServiceStorage.retrievePlayedGameById(newPlayedGame.getId(),serviceCallBackMockPlayer);

        Robolectric.flushBackgroundThreadScheduler();

        verify(serviceCallBackMockPlayer).onSuccess(any(PlayedGame.class));
    }

    @Test
    public void testPutPlayer_NotNew_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newPlayedGame)).thenReturn(newPlayedGame.getId());
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(GamePlayerRelations.class).withSelection(anyString(), anyString()))
                .thenReturn(mock(DatabaseCompartment.QueryBuilder.class));

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(GamePlayerRelations.class).query().list()).thenReturn(gamePlayerRelationsList);

       when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(GamePlayerRelations.class).withSelection(anyString(), anyString()).get())
       .thenReturn(gamePlayerRelationsList.get(0));

        playedGameServiceStorage.putPlayedGame(newPlayedGame, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayedGame);
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newPlayedGame.getGamePlayers().get(0));
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock), never()).put(isA(GamePlayerRelations.class));
        verify(serviceCallBackMock, Mockito.times(1)).onSuccess(anyLong());

        final ServiceCallBack<PlayedGame> serviceCallBackMockPlayer = Mockito.mock(ServiceCallBack.class);
        playedGameServiceStorage.retrievePlayedGameById(newPlayedGame.getId(),serviceCallBackMockPlayer);

        Robolectric.flushBackgroundThreadScheduler();

        verify(serviceCallBackMockPlayer).onSuccess(any(PlayedGame.class));
    }
}
