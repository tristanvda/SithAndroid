package com.grietenenknapen.sithandroid.service.storage.tools;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class DatabaseManager<T extends SQLiteOpenHelper> {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private T mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    protected DatabaseManager(T helper) {
        mDatabaseHelper = helper;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }
}
