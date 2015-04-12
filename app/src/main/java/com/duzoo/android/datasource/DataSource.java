
package com.duzoo.android.datasource;

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

import com.duzoo.android.database.MySQLiteDataHelper;
import com.duzoo.android.datasource.Interest;
import com.duzoo.android.datasource.User;

import org.w3c.dom.Comment;

public class DataSource {

    // Database fields
    private SQLiteDatabase     database;
    private MySQLiteDataHelper dbHelper;
    private String[]           userAllColumns     = { MySQLiteDataHelper.COLUMN_USER_ID,
                                                  MySQLiteDataHelper.COLUMN_USER_NAME,
                                                  MySQLiteDataHelper.COLUMN_USER_IMAGE };

    private String[]           interestAllColumns = { MySQLiteDataHelper.COLUMN_INTEREST_TYPE,
                                                  MySQLiteDataHelper.COLUMN_INTEREST_NAME,
                                                  MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS,
                                                  MySQLiteDataHelper.COLUMN_INTEREST_IMAGE };

    private String[]           postAllColumns = { MySQLiteDataHelper.COLUMN_POST_ID,
            MySQLiteDataHelper.COLUMN_POST_USER_NAME,
            MySQLiteDataHelper.COLUMN_POST_USER_IMAGE,
            MySQLiteDataHelper.COLUMN_POST_CONTENT,
            MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES,
            MySQLiteDataHelper.COLUMN_POST_CREATED_AT,
            MySQLiteDataHelper.COLUMN_POST_UP_VOTES,
            MySQLiteDataHelper.COLUMN_POST_MY_VOTE };

    private String[]           commentAllColumns = { MySQLiteDataHelper.COLUMN_COMMENT_ID,
            MySQLiteDataHelper.COLUMN_COMMENT_NAME,
            MySQLiteDataHelper.COLUMN_COMMENT_CONTENT,
            MySQLiteDataHelper.COLUMN_COMMENT_POST_ID,
            MySQLiteDataHelper.COLUMN_COMMENT_CREATED_AT };

    private String[]           messageAllColumns = { MySQLiteDataHelper.COLUMN_MESSAGE_ID,
            MySQLiteDataHelper.COLUMN_MESSAGE_NAME,
            MySQLiteDataHelper.COLUMN_MESSAGE_CONTENT,
            MySQLiteDataHelper.COLUMN_MESSAGE_INTEREST_ID,
            MySQLiteDataHelper.COLUMN_MESSAGE_CREATED_AT };



    public DataSource(Context context) {
        dbHelper = new MySQLiteDataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User createUser(String name, String id, String image) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_USER_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_USER_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_USER_IMAGE, image);

        database.insert(MySQLiteDataHelper.TABLE_USER, null, values);
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_USER,
                userAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public Post createPost(String name, String id, String content, String createdAt,int up,int down,String image) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_USER_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_POST_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_POST_CONTENT, content);
        values.put(MySQLiteDataHelper.COLUMN_POST_CREATED_AT, createdAt);
        values.put(MySQLiteDataHelper.COLUMN_POST_UP_VOTES, up);
        values.put(MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES, down);
        values.put(MySQLiteDataHelper.COLUMN_POST_MY_VOTE, 0);
        values.put(MySQLiteDataHelper.COLUMN_POST_USER_IMAGE, image);


        database.insert(MySQLiteDataHelper.TABLE_POST, null, values);
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                postAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        Post newPost = cursorToPost(cursor);
        cursor.close();
        return newPost;
    }

    public Interest createInterest(int type, String name, String image, int following) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_TYPE, type);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_IMAGE, image);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS, following);

        database.insert(MySQLiteDataHelper.TABLE_INTEREST, null, values);
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_INTEREST,
                interestAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        Interest newInterest = cursorToInterest(cursor);
        cursor.close();
        return newInterest;
    }

    public void updateUserFollowing(String id, String following) {
        ContentValues values = new ContentValues();
        int rows = database.update(MySQLiteDataHelper.TABLE_USER,
                values, MySQLiteDataHelper.COLUMN_USER_ID, new String[] { id });
        Log.i("user", rows + "updated");
    }

    public void updateUserImage(String id, String image) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_USER_IMAGE, image);
        int rows = database.update(MySQLiteDataHelper.TABLE_USER,
                values, MySQLiteDataHelper.COLUMN_USER_ID, new String[] { id });
        Log.i("user", rows + "updated");
    }

    public void deleteUser(String id) {
        database.delete(MySQLiteDataHelper.TABLE_USER, MySQLiteDataHelper.COLUMN_USER_ID,
                new String[] { id });
    }

    public void updatePostStatus(String id, int vote) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_MY_VOTE, vote);
        int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                values, MySQLiteDataHelper.COLUMN_POST_ID+"=?", new String[] { id });
        Log.i("user", rows + "updated");
    }

    public User getUser(String id) {
        User user = new User();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_USER,
                userAllColumns, MySQLiteDataHelper.COLUMN_USER_ID, new String[] { id }, null, null,
                null);

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

    public Post getPost(String id) {
        Post post = new Post();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                postAllColumns, MySQLiteDataHelper.COLUMN_POST_ID + "=?", new String[] { id }, null, null,
                null);

        cursor.moveToFirst();
        post = cursorToPost(cursor);
        // make sure to close the cursor
        cursor.close();
        return post;
    }

    public void updatePost(String id,int upvotes,int downvotes) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_UP_VOTES, upvotes);
        values.put(MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES, downvotes);
        int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                values, MySQLiteDataHelper.COLUMN_POST_ID+"=?", new String[] { id });
        Log.i("user", rows + "updated");
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

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<Post>();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                postAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Post post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setName(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_NAME)));
        user.setId(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_ID)));
        user.setImage(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_IMAGE)));

        return user;
    }

    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();
        post.setName(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_USER_NAME)));
        post.setId(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_ID)));
        post.setUpvotes(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_UP_VOTES)));
        post.setDownvotes(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES)));
        post.setContent(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_CONTENT)));
        post.setCreatedAt(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_CREATED_AT)));
        post.setVote(cursor.getInt(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_MY_VOTE)));
        post.setUserImage(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_POST_USER_NAME)));
        return post;
    }

    private Interest cursorToInterest(Cursor cursor) {
        Interest interest = new Interest();
        interest.setName(cursor.getString(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_NAME)));
        interest.setType(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_TYPE)));
        interest.setImage(cursor.getString(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_IMAGE)));
        interest.setFollowers_count(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS)));

        return interest;
    }

}
