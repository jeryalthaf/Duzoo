
package com.ola.appathon.food.database;

/**
 * Created by RRaju on 12/11/2014.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteDataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME            = "duzoo.db";
    private static final int    DATABASE_VERSION         = 1;

    public static final String  TABLE_USER               = "user";
    public static final String  TABLE_INTEREST               = "interest";

    public static final String  COLUMN_USER_NAME              = "name";
    public static final String  COLUMN_USER_IMAGE             = "image";
    public static final String  COLUMN_USER_ID                = "object_id";
    public static final String  COLUMN_USER_FOLLOWING         = "following_interests";

    public static final String  COLUMN_INTEREST_NAME                = "name";
    public static final String  COLUMN_INTEREST_IMAGE              = "interest_image";
    public static final String  COLUMN_INTEREST_TYPE     = "interest_type";
    public static final String  COLUMN_INTEREST_FOLLOWERS     = "followers_count";

    // Database creation sql statement
    private static final String USER_CREATE              = "create table " + TABLE_USER
                                                                 + "(" + COLUMN_USER_ID
                                                                 + " text primary key, "
                                                                 + COLUMN_USER_NAME
                                                                 + " text not null, "
                                                                 + COLUMN_USER_IMAGE
                                                                 + " text not null, "
                                                                 + COLUMN_USER_FOLLOWING
                                                                 + " text not null);";

    private static final String INTEREST_CREATE              = "create table "
                                                                 + TABLE_INTEREST
                                                                 + "("
                                                                 + COLUMN_INTEREST_TYPE
                                                                 + " integer primary key , "
                                                                 + COLUMN_INTEREST_IMAGE
                                                                 + " text not null, "
                                                                 + COLUMN_INTEREST_NAME
                                                                 + " text not null, "
                                                                 + COLUMN_INTEREST_FOLLOWERS
                                                                 + " text not null);";

    public MySQLiteDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(USER_CREATE);
        database.execSQL(INTEREST_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteDataHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTEREST);
        onCreate(db);
    }

}
