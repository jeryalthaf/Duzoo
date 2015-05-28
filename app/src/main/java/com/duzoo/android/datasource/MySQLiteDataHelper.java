package com.duzoo.android.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rraju on 5/22/2015.
 */
public class MySQLiteDataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME            = "duzoo.db";
    private static final int    DATABASE_VERSION         = 1;

    public static final String  TABLE_POST               = "post";

    public static final String  COLUMN_POST_ID              = "id";
    public static final String  COLUMN_POST_VOTE            = "vote";
    public static final String  COLUMN_POST_FAV                = "fav";
    public static final String  COLUMN_POST_DELETED                = "del";

    // Database creation sql statement
    private static final String POST_CREATE              = "create table " + TABLE_POST
            + "(" + COLUMN_POST_ID
            + " text primary key, "
            + COLUMN_POST_VOTE
            + " integer not null, "
            + COLUMN_POST_DELETED
            + " integer not null, "
            + COLUMN_POST_FAV
            + " integer not null);";

    public MySQLiteDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(POST_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}