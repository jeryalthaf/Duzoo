
package com.ola.appathon.food.datasource;

/**
 * Created by RRaju on 12/12/2014.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.ola.appathon.food.database.MySQLiteDataHelper;
import com.ola.appathon.food.datasource.Interest;
import com.ola.appathon.food.datasource.User;

import org.w3c.dom.Comment;

public class DataSource {

    // Database fields
    private SQLiteDatabase     database;
    private MySQLiteDataHelper dbHelper;
    private String[]           userAllColumns = { MySQLiteDataHelper.COLUMN_USER_ID,
                                              MySQLiteDataHelper.COLUMN_USER_NAME,
                                              MySQLiteDataHelper.COLUMN_USER_FOLLOWING,
                                              MySQLiteDataHelper.COLUMN_USER_IMAGE    };

    private String[]           interestAllColumns = { MySQLiteDataHelper.COLUMN_INTEREST_TYPE,
                                              MySQLiteDataHelper.COLUMN_INTEREST_NAME,
            MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS,
            MySQLiteDataHelper.COLUMN_INTEREST_IMAGE};

    public DataSource(Context context) {
        dbHelper = new MySQLiteDataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User createUser(String name, String id, String image, String following) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_USER_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_USER_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_USER_IMAGE, image);
        values.put(MySQLiteDataHelper.COLUMN_USER_FOLLOWING,following);

        database.insert(MySQLiteDataHelper.TABLE_USER, null, values);
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_USER,
                userAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public Interest createInterest(int type,String name,String image,int following) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_TYPE, type);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_IMAGE, image);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS,following);

        database.insert(MySQLiteDataHelper.TABLE_INTEREST, null, values);
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_INTEREST,
                interestAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        Interest newInterest = cursorToInterest(cursor);
        cursor.close();
        return newInterest;
    }



    public void UpdateUserFollowing(String id,String following) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_USER_FOLLOWING, following);
        int rows = database.update(MySQLiteDataHelper.TABLE_USER,
                values, MySQLiteDataHelper.COLUMN_USER_ID, new String[] {id});
        Log.i("user",rows+"updated");
    }

    public void UpdateUserImage(String id,String image) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_USER_IMAGE, image);
        int rows = database.update(MySQLiteDataHelper.TABLE_USER,
                values, MySQLiteDataHelper.COLUMN_USER_ID, new String[] {id});
        Log.i("user",rows+"updated");
    }

    public void deleteUser(String id) {
        database.delete(MySQLiteDataHelper.TABLE_USER, MySQLiteDataHelper.COLUMN_USER_ID, new String[] {id});
    }

    public User getUser(String id) {
        User user = new User();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_USER,
                userAllColumns, MySQLiteDataHelper.COLUMN_USER_ID, new String[] {id}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User _user = cursorToUser(cursor);
            user = _user;
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return user;
    }

    public List<Interest> getAllInterests() {
        List<Interest> interests = new ArrayList<Interest>();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_INTEREST,
                interestAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Interest interest = cursorToInterest(cursor);
            interests.add(interest);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return interests;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setName(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_NAME)));
        user.setId(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_ID)));
        user.setImage(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_IMAGE)));
        user.setFollowing(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_FOLLOWING)));

        return user;
    }

    private Interest cursorToInterest(Cursor cursor) {
        Interest interest = new Interest();
        interest.setName(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_NAME)));
        interest.setType(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_TYPE)));
        interest.setImage(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_IMAGE)));
        interest.setFollowers_count(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS)));

        return interest;
    }

}
