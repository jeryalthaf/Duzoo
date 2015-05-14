
package com.duzoo.android.database;

/**
 * Created by RRaju on 12/11/2014.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteDataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "duzoo.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_USER = "user";
    public static final String TABLE_INTEREST = "interest";
    public static final String TABLE_POST = "post";
    public static final String TABLE_COMMENT = "comment";
    public static final String TABLE_MESSAGE = "message";

    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_IMAGE = "image";
    public static final String COLUMN_USER_ID = "object_id";

    public static final String COLUMN_INTEREST_NAME = "name";
    public static final String COLUMN_INTEREST_IMAGE = "interest_image";
    public static final String COLUMN_INTEREST_TYPE = "interest_type";
    public static final String COLUMN_INTEREST_FOLLOWERS = "followers_count";

    public static final String COLUMN_POST_USER_NAME = "name";
    public static final String COLUMN_POST_CONTENT = "content";
    public static final String COLUMN_POST_ID = "object_id";
    public static final String COLUMN_POST_INTEREST_TYPE = "type";
    public static final String COLUMN_POST_UP_VOTES = "upvotes";
    public static final String COLUMN_POST_DOWN_VOTES = "downvotes";
    public static final String COLUMN_POST_USER_IMAGE = "userImage";
    public static final String COLUMN_POST_MY_VOTE = "myVote";
    public static final String COLUMN_POST_CREATED_AT = "timestamp";
    public static final String COLUMN_POST_COMMENT_COUNT = "comment_count";
    public static final String COLUMN_POST_HAS_MEDIA = "has_media";
    public static final String COLUMN_POST_IMAGE_LOCAL_URL = "image_local_url";

    public static final String COLUMN_COMMENT_NAME = "name";
    public static final String COLUMN_COMMENT_CONTENT = "content";
    public static final String COLUMN_COMMENT_ID = "object_id";
    public static final String COLUMN_COMMENT_POST_ID = "post_id";
    public static final String COLUMN_COMMENT_CREATED_AT = "timestamp";

    public static final String COLUMN_MESSAGE_NAME = "name";
    public static final String COLUMN_MESSAGE_CONTENT = "content";
    public static final String COLUMN_MESSAGE_ID = "object_id";
    public static final String COLUMN_MESSAGE_INTEREST_ID = "interest_id";
    public static final String COLUMN_MESSAGE_CREATED_AT = "createdAt";
    public static final String COLUMN_MESSAGE_IS_SENT_BY_ME = "sentByMe";
    // Database creation sql statement
    private static final String USER_CREATE = "create table " + TABLE_USER
            + "(" + COLUMN_USER_ID
            + " text primary key, "
            + COLUMN_USER_NAME
            + " text not null, "
            + COLUMN_USER_IMAGE
            + " text not null);";

    private static final String INTEREST_CREATE = "create table "
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

    private static final String POST_CREATE = "create table "
            + TABLE_POST
            + "("
            + COLUMN_POST_ID
            + " text primary key , "
            + COLUMN_POST_USER_NAME
            + " text not null, "
            + COLUMN_POST_USER_IMAGE
            + " text not null, "
            + COLUMN_POST_CONTENT
            + " text not null, "
            + COLUMN_POST_UP_VOTES
            + " integer not null, "
            + COLUMN_POST_IMAGE_LOCAL_URL
            + " text , "
            + COLUMN_POST_HAS_MEDIA
            + " integer not null, "
            + COLUMN_POST_COMMENT_COUNT
            + " integer not null, "
            + COLUMN_POST_INTEREST_TYPE
            + " integer not null, "
            + COLUMN_POST_DOWN_VOTES
            + " integer not null, "
            + COLUMN_POST_MY_VOTE
            + " integer not null, "
            + COLUMN_POST_CREATED_AT
            + " real not null);";

    private static final String COMMENT_CREATE = "create table "
            + TABLE_COMMENT
            + "("
            + COLUMN_COMMENT_ID
            + " text primary key , "
            + COLUMN_COMMENT_NAME
            + " text not null, "
            + COLUMN_COMMENT_CONTENT
            + " text not null, "
            + COLUMN_COMMENT_POST_ID
            + " text not null, "
            + COLUMN_COMMENT_CREATED_AT
            + " real not null);";

    private static final String MESSAGE_CREATE = "create table "
            + TABLE_MESSAGE
            + "("
            + COLUMN_MESSAGE_ID
            + " text primary key , "
            + COLUMN_MESSAGE_NAME
            + " text not null, "
            + COLUMN_MESSAGE_CONTENT
            + " text not null, "
            + COLUMN_MESSAGE_INTEREST_ID
            + " int not null, "
            + COLUMN_MESSAGE_IS_SENT_BY_ME
            + " integer not null, "
            + COLUMN_MESSAGE_CREATED_AT
            + " real not null);";

    public MySQLiteDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(USER_CREATE);
        database.execSQL(INTEREST_CREATE);
        database.execSQL(POST_CREATE);
        database.execSQL(COMMENT_CREATE);
        database.execSQL(MESSAGE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteDataHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTEREST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);
    }

}
