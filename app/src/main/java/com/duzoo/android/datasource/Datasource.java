package com.duzoo.android.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rraju on 5/22/2015.
 */
public class Datasource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteDataHelper dbHelper;
    private String[] postAllColumns = {MySQLiteDataHelper.COLUMN_POST_ID,
            MySQLiteDataHelper.COLUMN_POST_FAV,
            MySQLiteDataHelper.COLUMN_POST_VOTE,
            MySQLiteDataHelper.COLUMN_POST_DELETED
    };

    public Datasource(Context context) {
        dbHelper = new MySQLiteDataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Post createPost(String id) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_POST_FAV, 0);
        values.put(MySQLiteDataHelper.COLUMN_POST_VOTE, 0);
        values.put(MySQLiteDataHelper.COLUMN_POST_DELETED, 0);

        try {
            database.insert(MySQLiteDataHelper.TABLE_POST, null, values);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                    postAllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            Post post = cursorToPost(cursor);
            cursor.close();
            return post;
        } catch (SQLiteConstraintException ex) {
            return null;
        }
    }

    public void updatePostVote(String id, int vote) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_VOTE, vote);

        String whereClause = MySQLiteDataHelper.COLUMN_POST_ID + " = ?";
        String whereArgs[] = new String[]{id};
        int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                values, whereClause, whereArgs);
    }

    public void updatePostFav(String id, int fav) {
        ContentValues values = new ContentValues();
        // 1 for fav, 0 for not fav
        values.put(MySQLiteDataHelper.COLUMN_POST_FAV, fav);

        String whereClause = MySQLiteDataHelper.COLUMN_POST_ID + " = ?";
        String whereArgs[] = new String[]{id};
        int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                values, whereClause, whereArgs);
    }

    public void deletePost(String id) {
        ContentValues values = new ContentValues();
        // 1 for fav, 0 for not fav
        values.put(MySQLiteDataHelper.COLUMN_POST_DELETED, 1);

        String whereClause = MySQLiteDataHelper.COLUMN_POST_ID + " = ?";
        String whereArgs[] = new String[]{id};
        int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                values, whereClause, whereArgs);
    }

    public Post getPost(String id) {
        Post post = new Post();
        String whereClause = MySQLiteDataHelper.COLUMN_POST_ID + " = ?";
        String whereArgs[] = new String[]{id};
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                postAllColumns, whereClause, whereArgs, null, null, null);

        try {
            cursor.moveToFirst();
            post = cursorToPost(cursor);
            // make sure to close the cursor
            cursor.close();
            return post;
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
    }

    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();
        post.setId(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_ID)));
        post.setVote(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_VOTE)));
        post.setIsDeleted(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_DELETED)));
        post.setIsFavorite(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_FAV)));

        return post;
    }


}

