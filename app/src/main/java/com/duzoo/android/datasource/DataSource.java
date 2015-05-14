
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
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.duzoo.android.database.MySQLiteDataHelper;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteDataHelper dbHelper;
    private String[] userAllColumns = {MySQLiteDataHelper.COLUMN_USER_ID,
            MySQLiteDataHelper.COLUMN_USER_NAME,
            MySQLiteDataHelper.COLUMN_USER_IMAGE};

    private String[] interestAllColumns = {MySQLiteDataHelper.COLUMN_INTEREST_TYPE,
            MySQLiteDataHelper.COLUMN_INTEREST_NAME,
            MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS,
            MySQLiteDataHelper.COLUMN_INTEREST_IMAGE};

    private String[] postAllColumns = {MySQLiteDataHelper.COLUMN_POST_ID,
            MySQLiteDataHelper.COLUMN_POST_USER_NAME,
            MySQLiteDataHelper.COLUMN_POST_USER_IMAGE,
            MySQLiteDataHelper.COLUMN_POST_CONTENT,
            MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES,
            MySQLiteDataHelper.COLUMN_POST_CREATED_AT,
            MySQLiteDataHelper.COLUMN_POST_UP_VOTES,
            MySQLiteDataHelper.COLUMN_POST_INTEREST_TYPE,
            MySQLiteDataHelper.COLUMN_POST_COMMENT_COUNT,
            MySQLiteDataHelper.COLUMN_POST_HAS_MEDIA,
            MySQLiteDataHelper.COLUMN_POST_IMAGE_LOCAL_URL,
            MySQLiteDataHelper.COLUMN_POST_MY_VOTE};

    private String[] commentAllColumns = {MySQLiteDataHelper.COLUMN_COMMENT_ID,
            MySQLiteDataHelper.COLUMN_COMMENT_NAME,
            MySQLiteDataHelper.COLUMN_COMMENT_CONTENT,
            MySQLiteDataHelper.COLUMN_COMMENT_POST_ID,
            MySQLiteDataHelper.COLUMN_COMMENT_CREATED_AT};

    private String[] messageAllColumns = {MySQLiteDataHelper.COLUMN_MESSAGE_ID,
            MySQLiteDataHelper.COLUMN_MESSAGE_NAME,
            MySQLiteDataHelper.COLUMN_MESSAGE_CONTENT,
            MySQLiteDataHelper.COLUMN_MESSAGE_INTEREST_ID,
            MySQLiteDataHelper.COLUMN_MESSAGE_CREATED_AT,
            MySQLiteDataHelper.COLUMN_MESSAGE_IS_SENT_BY_ME
    };

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
        try {
            database.insert(MySQLiteDataHelper.TABLE_USER, null, values);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_USER,
                    userAllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            User newUser = cursorToUser(cursor);
            cursor.close();
            return newUser;
        } catch (SQLException ex) {
            return null;
        }
    }

    public Comment createComment(String name, String id, String content,String postId,double timestamp) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_COMMENT_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_COMMENT_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_COMMENT_POST_ID, postId);
        values.put(MySQLiteDataHelper.COLUMN_COMMENT_CONTENT, content);
        values.put(MySQLiteDataHelper.COLUMN_COMMENT_CREATED_AT, timestamp);
        try {
            database.insert(MySQLiteDataHelper.TABLE_COMMENT, null, values);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_COMMENT,
                    commentAllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            Comment comment = cursorToComment(cursor);
            cursor.close();
            return comment;
        } catch (SQLException ex) {
            return null;
        }
    }

    public Post createPost(String name, String id, String content, double timestamp, int up,
                           int down, int type, String image, int commentCount,boolean hasMedia,String localImageUrl) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_USER_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_POST_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_POST_CONTENT, content);
        values.put(MySQLiteDataHelper.COLUMN_POST_CREATED_AT, timestamp);
        values.put(MySQLiteDataHelper.COLUMN_POST_UP_VOTES, up);
        values.put(MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES, down);
        values.put(MySQLiteDataHelper.COLUMN_POST_MY_VOTE, 0);
        values.put(MySQLiteDataHelper.COLUMN_POST_USER_IMAGE, image);
        values.put(MySQLiteDataHelper.COLUMN_POST_INTEREST_TYPE, type);
        values.put(MySQLiteDataHelper.COLUMN_POST_COMMENT_COUNT, commentCount);
        if(hasMedia)
            values.put(MySQLiteDataHelper.COLUMN_POST_HAS_MEDIA, 1);
        else
            values.put(MySQLiteDataHelper.COLUMN_POST_HAS_MEDIA, 0);
        if(hasMedia)
            values.put(MySQLiteDataHelper.COLUMN_POST_IMAGE_LOCAL_URL, localImageUrl);
        try {
            database.insert(MySQLiteDataHelper.TABLE_POST, null, values);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                    postAllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            Post newPost = cursorToPost(cursor, false);
            cursor.close();
            return newPost;
        } catch (SQLException ex) {
            return null;
        }
    }

    public Message createMessage(String name, String id, String content, double createdAt,
                                 int interestType,
                                 int isSentByMe) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_MESSAGE_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_MESSAGE_ID, id);
        values.put(MySQLiteDataHelper.COLUMN_MESSAGE_CONTENT, content);
        values.put(MySQLiteDataHelper.COLUMN_MESSAGE_CREATED_AT, createdAt);
        values.put(MySQLiteDataHelper.COLUMN_MESSAGE_INTEREST_ID, interestType);
        values.put(MySQLiteDataHelper.COLUMN_MESSAGE_IS_SENT_BY_ME, isSentByMe);

        try {
            database.insert(MySQLiteDataHelper.TABLE_MESSAGE, null, values);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_MESSAGE,
                    messageAllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            Message message = cursorToMessage(cursor);
            cursor.close();
            return message;
        } catch (SQLiteConstraintException ex) {
            return null;
        }
    }

    public Interest createInterest(int type, String name, String image, int following) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_NAME, name);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_TYPE, type);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_IMAGE, image);
        values.put(MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS, following);

        try {
            database.insert(MySQLiteDataHelper.TABLE_INTEREST, null, values);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_INTEREST,
                    interestAllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            Interest newInterest = cursorToInterest(cursor);
            cursor.close();
            return newInterest;
        } catch (SQLException ex) {
            return null;
        }
    }

    public void updateUserFollowing(String id, String following) {
        ContentValues values = new ContentValues();
        int rows = database.update(MySQLiteDataHelper.TABLE_USER,
                values, MySQLiteDataHelper.COLUMN_USER_ID, new String[]{id});
        Log.i("user", rows + "updated");
    }

    public void updateUserImage(String id, String image) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_USER_IMAGE, image);
        int rows = database.update(MySQLiteDataHelper.TABLE_USER,
                values, MySQLiteDataHelper.COLUMN_USER_ID, new String[]{id});
        Log.i("user", rows + "updated");
    }

    public void deleteUser(String id) {
        database.delete(MySQLiteDataHelper.TABLE_USER, MySQLiteDataHelper.COLUMN_USER_ID,
                new String[]{id});
    }

    public void updatePostStatus(String id, int vote) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_MY_VOTE, vote);
        try {
            int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                    values, MySQLiteDataHelper.COLUMN_POST_ID + "=?", new String[]{id});
            Log.i("user", rows + "updated");
        } catch (SQLException ex) {
        }
    }

    public User getUser(String id) {
        User user = new User();
        try {
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_USER,
                    userAllColumns, MySQLiteDataHelper.COLUMN_USER_ID, new String[]{id}, null, null,
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
        } catch (SQLException ex) {
            return null;
        }
    }

    public Post getPost(String id) {
        Post post = new Post();

        try {
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                    postAllColumns, MySQLiteDataHelper.COLUMN_POST_ID + "=?", new String[]{id},
                    null, null,
                    null);

            cursor.moveToFirst();
            post = cursorToPost(cursor, true);
            // make sure to close the cursor
            cursor.close();
            return post;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void updatePost(String id, int upvotes, int downvotes, int commentCount) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.COLUMN_POST_UP_VOTES, upvotes);
        values.put(MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES, downvotes);
        values.put(MySQLiteDataHelper.COLUMN_POST_COMMENT_COUNT, commentCount);
        try {
            int rows = database.update(MySQLiteDataHelper.TABLE_POST,
                    values, MySQLiteDataHelper.COLUMN_POST_ID + "=?", new String[]{id});
            Log.i("user", rows + "updated");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Interest> getAllInterests() {
        List<Interest> interests = new ArrayList<Interest>();

        try {
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Message> getAllMessages(int type) {
        List<Message> messages = new ArrayList<Message>();

        try {
            String interest = String.valueOf(type);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_MESSAGE,
                    messageAllColumns, MySQLiteDataHelper.COLUMN_MESSAGE_INTEREST_ID + "=?",
                    new String[]{interest}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Message message = cursorToMessage(cursor);
                messages.add(message);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return messages;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Message cursorToMessage(Cursor cursor) {
        Message message = new Message();
        if (cursor != null) {
            message.setName(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_MESSAGE_NAME)));
            message.setId(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_MESSAGE_ID)));
            message.setContent(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_MESSAGE_CONTENT)));
            message.setInterestType(cursor.getInt(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_MESSAGE_INTEREST_ID)));
            message.setTimeStamp(cursor.getDouble(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_MESSAGE_CREATED_AT)));
            message.setSentByMe(cursor.getInt(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_MESSAGE_IS_SENT_BY_ME)));
            return message;
        } else
            return null;
    }

    public List<Post> getAllPosts(int type) {
        List<Post> posts = new ArrayList<Post>();

        try {
            String interest = String.valueOf(type);
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_POST,
                    postAllColumns, MySQLiteDataHelper.COLUMN_POST_INTEREST_TYPE + "=?",
                    new String[]{interest},
                    null, null, null);

            cursor.moveToLast();
            while (!cursor.isBeforeFirst()) {
                Post post = cursorToPost(cursor, false);
                posts.add(post);
                cursor.moveToPrevious();
            }
            // make sure to close the cursor
            cursor.close();
            return posts;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Comment> getCommentByPost(String postId) {
        List<Comment> comments = new ArrayList<Comment>();

        try {
            Cursor cursor = database.query(MySQLiteDataHelper.TABLE_COMMENT,
                    commentAllColumns, MySQLiteDataHelper.COLUMN_COMMENT_POST_ID + "=?",
                    new String[]{postId},
                    null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Comment comment = cursorToComment(cursor);
                comments.add(comment);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return comments;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        if (cursor != null && cursor.moveToFirst()) {

            user.setName(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_USER_NAME)));
            user.setId(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_USER_ID)));
            user.setImage(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_USER_IMAGE)));
            return user;
        } else
            return null;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        if (cursor != null) {

            comment.setName(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_COMMENT_NAME)));
            comment.setObject_id(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_COMMENT_ID)));
            comment.setPost_id(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_COMMENT_POST_ID)));
            comment.setContent(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_COMMENT_CONTENT)));
            comment.setTimestamp(cursor.getDouble(cursor.getColumnIndex(MySQLiteDataHelper.COLUMN_COMMENT_CREATED_AT)));
            return comment;
        } else
            return null;
    }

    private Post cursorToPost(Cursor cursor, boolean moveToFirstCheck) {
        Post post = new Post();
        if (moveToFirstCheck) {

            if (cursor != null && cursor.moveToFirst()) {
                return getPostFromCursor(cursor, post);
            } else
                return null;
        } else {
            if (cursor != null)
                return getPostFromCursor(cursor, post);
            else
                return null;
        }
    }

    private Post getPostFromCursor(Cursor cursor, Post post) {
        post.setName(cursor.getString(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_USER_NAME)));
        post.setId(cursor.getString(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_ID)));
        post.setUpvotes(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_UP_VOTES)));
        post.setDownvotes(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_DOWN_VOTES)));
        post.setContent(cursor.getString(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_CONTENT)));
        post.setTimestamp(cursor.getDouble(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_CREATED_AT)));
        post.setVote(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_MY_VOTE)));
        post.setType(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_INTEREST_TYPE)));
        post.setUserImage(cursor.getString(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_USER_IMAGE)));
        post.setCommentCount(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_COMMENT_COUNT)));
        post.setHasMedia(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_HAS_MEDIA)));
        if(cursor.getInt(cursor
                .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_HAS_MEDIA))==1)
           post.setImageUrl(cursor.getString(cursor
                   .getColumnIndex(MySQLiteDataHelper.COLUMN_POST_IMAGE_LOCAL_URL)));


        return post;
    }

    private Interest cursorToInterest(Cursor cursor) {
        Interest interest = new Interest();
        if (cursor != null && cursor.moveToFirst()) {

            interest.setName(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_NAME)));
            interest.setType(cursor.getInt(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_TYPE)));
            interest.setImage(cursor.getString(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_IMAGE)));
            interest.setFollowers_count(cursor.getInt(cursor
                    .getColumnIndex(MySQLiteDataHelper.COLUMN_INTEREST_FOLLOWERS)));
            return interest;
        } else
            return null;
    }

}
