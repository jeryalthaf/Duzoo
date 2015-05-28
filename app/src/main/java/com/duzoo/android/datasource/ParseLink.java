
package com.duzoo.android.datasource;

import android.graphics.Bitmap;

import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

/**
 * Created by RRaju on 3/27/2015.
 */
public class ParseLink {

    static Date lastUpdatedPost, lastUpdatedComment, lastUpdateMessage;

    public ParseLink() {
    }

    public static void getPosts() {

        lastUpdatedPost = new Date(DuzooPreferenceManager.getLongKey(DuzooConstants.KEY_POST_TIMESTAMP));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post").setLimit(30);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                if (e == null && posts.size() != 0) {
                    DuzooPreferenceManager.putKey(DuzooConstants.KEY_POST_TIMESTAMP,
                            System.currentTimeMillis());
                    for (final ParseObject post : posts) {
                        if (post.getBoolean(DuzooConstants.PARSE_POST_HAS_MEDIA)) {
                            ParseFile image = post.getParseFile(DuzooConstants.PARSE_POST_IMAGE);
                            image.getDataInBackground();

                        }
                        initFavAndVoteIfPresent(post);
                    }
                }
            }
        });
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_POST_ID, System.currentTimeMillis());
    }

    private static void initFavAndVoteIfPresent(final ParseObject post) {

        ParseQuery query = ParseQuery.getQuery("Post");
        query.fromLocalDatastore();
        query.getInBackground(post.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null) {
                    parseObject.put(DuzooConstants.PARSE_POST_COMMENT_COUNT, post.getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT));
                    parseObject.pinInBackground();
                } else {
                    if (post.getString(DuzooConstants.PARSE_USER_FACEBOOK_ID).contentEquals("1416425798673992"))
                        post.put(DuzooConstants.PARSE_POST_FAVORITE, true);
                    else
                        post.put(DuzooConstants.PARSE_POST_FAVORITE, false);
                    post.put(DuzooConstants.PARSE_POST_DELETED, false);
                    post.put(DuzooConstants.PARSE_POST_MY_VOTE, 0);
                    post.pinInBackground();
                }
            }

        });
    }

    public static void getComments() {
        Date date = new Date(DuzooPreferenceManager.getLongKey(DuzooConstants.KEY_COMMENT_TIMESTAMP));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment").whereGreaterThan("createdAt", date);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> comments, ParseException e) {
                if (e == null && comments.size() != 0) {
                    DuzooPreferenceManager.putKey(DuzooConstants.KEY_COMMENT_TIMESTAMP,
                            System.currentTimeMillis());
                    ParseObject.pinAllInBackground(comments);
                }
            }
        });
    }

    public static void getMessages() {

        Date date = new Date(
                DuzooPreferenceManager.getLongKey(DuzooConstants.KEY_MESSAGE_TIMESTAMP));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message").whereGreaterThan("createdAt", date);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                if (e == null && messages.size() != 0) {
                    DuzooPreferenceManager.putKey(DuzooConstants.KEY_MESSAGE_TIMESTAMP,
                            System.currentTimeMillis());
                    ParseObject.pinAllInBackground(messages);
                }
            }
        });
    }

    public static void updatePostVote(final String postId, final int vote) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post")
                .whereEqualTo("objectId", postId);

        try {
            query.getInBackground(postId, new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject post, ParseException e) {
                    if (vote == 1)
                        post.put("upvotes", post.getInt("upvotes") + 1);
                    else
                        post.put("downvotes", post.getInt("downvotes") + 1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                            }
                        }

                    });
                }
            });
        } catch (Exception ex) {
        }
    }

    public static void createUserOnParse(String name, String id, String url, String email)
            throws NullPointerException {

        ParseUser user = new ParseUser();
        user.put(DuzooConstants.PARSE_USER_NAME, name);
        user.setUsername(id);
        user.setPassword(id);
        user.put(DuzooConstants.PARSE_USER_IMAGE, url);
        user.put(DuzooConstants.PARSE_USER_FACEBOOK_ID, id);
        user.put(DuzooConstants.PARSE_USER_EMAIL, email);

        DuzooPreferenceManager.putKey(DuzooConstants.KEY_FACEBOOK_ID, id);
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_USER_NAME, name);
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_USER_IMAGE, url);
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_SIGNED_UP, true);
        DuzooPreferenceManager.putKey(DuzooConstants.PARSE_USER_EMAIL, email);
        user.signUpInBackground();
    }


    public static void updatePostComments(String postId) throws NullPointerException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post")
                .whereEqualTo("objectId", postId);
        try {
            query.getInBackground(postId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject post, ParseException e) {
                    post.put(DuzooConstants.PARSE_POST_COMMENT_COUNT,
                            post.getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT) + 1);
                    post.saveInBackground();
                    post.pinInBackground();
                }
            });
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
