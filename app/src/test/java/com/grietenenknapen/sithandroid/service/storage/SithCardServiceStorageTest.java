package com.grietenenknapen.sithandroid.service.storage;

import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.BuildConfig;
import com.grietenenknapen.sithandroid.model.database.SithCard;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class SithCardServiceStorageTest {
    private SithDatabase dbMock;
    private SQLiteDatabase sqLiteDatabaseMock;
    private Cupboard cupboardMock;
    private SithCardServiceStorage sithCardServiceStorage;
    private SithCard oldSithCard, newSithCard;
    private List<SithCard> sithCardList;

    @Before
    public void setUp() {
        oldSithCard = SithCard.newBuilder()
                ._id(1l)
                .cardType(GameCardType.BB8)
                .name("BB8")
                .imageResId("BB8")
                .build();

        newSithCard = SithCard.newBuilder()
                ._id(2l)
                .cardType(GameCardType.BB8)
                .name("BB8")
                .imageResId("BB8")
                .build();


        sqLiteDatabaseMock = Mockito.mock(SQLiteDatabase.class);
        cupboardMock = Mockito.mock(Cupboard.class);
        dbMock = Mockito.mock(SithDatabase.class);

        final QueryResultIterable queryResultIterableMock = Mockito.mock(QueryResultIterable.class);

        sithCardList = new ArrayList<>();
        sithCardList.add(oldSithCard);

        when(dbMock.openDatabase()).thenReturn(sqLiteDatabaseMock);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock)).thenReturn(Mockito.mock(DatabaseCompartment.class));

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(SithCard.class)).thenReturn(Mockito.mock(DatabaseCompartment.QueryBuilder.class));
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).query(SithCard.class).query()).thenReturn(queryResultIterableMock);
        when(queryResultIterableMock.list()).thenReturn(sithCardList);

        sithCardServiceStorage = new SithCardServiceStorage(dbMock, cupboardMock);
    }

    @Test
    public void testRetrieveSithCardAndGetSithCardFromMap_Success() {

        //First get it from database
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).get(SithCard.class, oldSithCard.getId())).thenReturn(oldSithCard);

        final ServiceCallBack<SithCard> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);

        sithCardServiceStorage.retrieveSithCardById(oldSithCard.getId(), serviceCallBackMock);

        //Now get it from map
        sithCardServiceStorage.retrieveSithCardById(oldSithCard.getId(), serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(serviceCallBackMock, Mockito.times(2)).onSuccess(oldSithCard);
    }

    @Test
    public void testRetrieveSithCardAndGetSithCardFromMap_Fail() {

        //First get it from database
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).get(SithCard.class, 0)).thenReturn(null);

        final ServiceCallBack<SithCard> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);

        sithCardServiceStorage.retrieveSithCardById(0, serviceCallBackMock);

        //Now get it from map
        sithCardServiceStorage.retrieveSithCardById(0, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(2)).openDatabase();
        verify(dbMock, Mockito.times(2)).closeDatabase();
        verify(serviceCallBackMock, Mockito.times(2)).onError(anyInt());
    }


    @Test
    public void testRetrieveSithCardsAndGetSithCardsFromMap_Success() {

        //First get it from database

        final ServiceCallBack<List<SithCard>> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);

        sithCardServiceStorage.retrieveAllSithCards(serviceCallBackMock);

        //Now get it from map
        sithCardServiceStorage.retrieveAllSithCards(serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(serviceCallBackMock, Mockito.times(2)).onSuccess(sithCardList);
    }

    @Test
    public void testPutSithCard_New_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newSithCard)).thenReturn(newSithCard.getId());

        sithCardServiceStorage.putSithCard(newSithCard, serviceCallBackMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(1)).openDatabase();
        verify(dbMock, Mockito.times(1)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).put(newSithCard);
        verify(serviceCallBackMock, Mockito.times(1)).onSuccess(anyLong());

        final ServiceCallBack<SithCard> serviceCallBackMockSithCard = Mockito.mock(ServiceCallBack.class);
        sithCardServiceStorage.retrieveSithCardById(newSithCard.getId(), serviceCallBackMockSithCard);

        Robolectric.flushBackgroundThreadScheduler();

        verify(serviceCallBackMockSithCard).onSuccess(any(SithCard.class));
    }

    @Test
    public void testRemoveSithCard_Success() {

        final ServiceCallBack<Long> serviceCallBackMock = Mockito.mock(ServiceCallBack.class);
        final ServiceCallBack<Void> serviceCallBackRemoveMock = Mockito.mock(ServiceCallBack.class);

        when(cupboardMock.withDatabase(sqLiteDatabaseMock).put(newSithCard)).thenReturn(newSithCard.getId());

        sithCardServiceStorage.putSithCard(newSithCard, serviceCallBackMock);
        sithCardServiceStorage.removeSithCard(newSithCard, serviceCallBackRemoveMock);

        Robolectric.flushBackgroundThreadScheduler();

        verify(dbMock, Mockito.times(2)).openDatabase();
        verify(dbMock, Mockito.times(2)).closeDatabase();
        verify(cupboardMock.withDatabase(sqLiteDatabaseMock)).delete(newSithCard);
        verify(serviceCallBackRemoveMock, Mockito.times(1)).onSuccess(null);

        final ServiceCallBack<SithCard> serviceCallBackMockSithCard = Mockito.mock(ServiceCallBack.class);
        sithCardServiceStorage.retrieveSithCardById(newSithCard.getId(), serviceCallBackMockSithCard);

        Robolectric.flushBackgroundThreadScheduler();

        verify(serviceCallBackMockSithCard).onError(anyInt());
    }
}

