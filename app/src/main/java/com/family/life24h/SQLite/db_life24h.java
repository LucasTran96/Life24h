package com.family.life24h.SQLite;

/*
 *  Date created: 12/18/2019
 *  Last updated: 12/18/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class db_life24h extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_life24h";
    public static final int DATABASE_VERSION = 2;

    private static db_life24h instance;


    public static db_life24h getInstance(Context context){
        if(instance == null)
            instance = new db_life24h(context);
        return instance;
    }

    private db_life24h(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean checkTableExist(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();

        if (tableName == null || db == null || !db.isOpen())
            return false;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }


}
