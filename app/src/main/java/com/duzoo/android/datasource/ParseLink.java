
package com.duzoo.android.datasource;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.application.UIController;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.Date;
import java.util.List;

/**
 * Created by RRaju on 3/27/2015.
 */
public class ParseLink {
    static DataSource db;
    static LoadToast loadToast;

    public enum currentActivity {
        duzoo,
        home,
        comments
    }
    public ParseLink() {
    }

    public static void getInterests() throws NullPointerException {

        /*
         * db = new DataSource(MyApplication.getContext()); db.open(); if
         * (db.getAllInterests().size() == 8) return; ParseQuery<ParseObject>
         * query = ParseQuery.getQuery("Interest"); query.findInBackground(new
         * FindCallback<ParseObject>() {
         * @Override public void done(List<ParseObject> interests,
         * ParseException e) { for (ParseObject interest : interests) { String
         * name = new String(interest.getString("interestName")); int type =
         * interest.getInt("interestType"); int followers =
         * interest.getInt("followersCount"); String image =
         * ImageUtil.convertParseFileToString(interest
         * .getParseFile("interestImage")); db.createInterest(type, name, image,
         * followers); } } });
         */
    }

    public static boolean getPosts() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        if (db == null)
            db = new DataSource(MyApplication.getContext());
        db.open();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                if (e == null && posts.size() != 0) {
                    DuzooPreferenceManager.putKey(DuzooConstants.KEY_POST_TIMESTAMP,
                            System.currentTimeMillis());
                    for (ParseObject post : posts) {
                        try {
                            String content = new String(post
                                    .getString(DuzooConstants.PARSE_POST_CONTENT));
                            String objectId = new String(post.getObjectId());
                            int upVotes = post.getInt(DuzooConstants.PARSE_POST_UPVOTES);
                            int downVotes = post.getInt(DuzooConstants.PARSE_POST_DOWNVOTES);
                            int commentCount = post.getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT);
                            String userName = new String(post
                                    .getString(DuzooConstants.PARSE_POST_USER_NAME));
                            String userImage = new String(post
                                    .getString(DuzooConstants.PARSE_POST_USER_IMAGE));
                            double timestamp = post.getDouble(DuzooConstants.PARSE_POST_TIMESTAMP);
                            int type = post.getInt(DuzooConstants.PARSE_POST_INTEREST_TYPE);
                            Post _post = db.getPost(objectId);
                            boolean hasMedia = post.getBoolean(DuzooConstants.PARSE_POST_HAS_MEDIA);
                            if (hasMedia) {
                                ParseFile file = post.getParseFile(DuzooConstants.PARSE_POST_IMAGE);
                                Util.saveImageToAppDirectory(file, String.valueOf(timestamp));
                            }
                            if (_post != null)
                                db.updatePost(objectId, upVotes, downVotes, commentCount);
                            else
                                db.createPost(userName, objectId, content, timestamp, upVotes,
                                        downVotes, type, userImage, commentCount, hasMedia, String.valueOf(timestamp));

                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        return true;
    }

    public static boolean getComments() {
        Date date = new Date(
                DuzooPreferenceManager.getLongKey(DuzooConstants.KEY_COMMENT_TIMESTAMP));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
                /*.whereGreaterThan(
                        "updatedAt", date.getTime());*/
        if (db == null)
            db = new DataSource(MyApplication.getContext());
        db.open();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> comments, ParseException e) {
                if (e == null && comments.size() != 0) {
                    DuzooPreferenceManager.putKey(DuzooConstants.KEY_COMMENT_TIMESTAMP,
                            System.currentTimeMillis());
                    for (ParseObject comment : comments) {
                        try {
                            String content = new String(comment
                                    .getString(DuzooConstants.PARSE_COMMENT_CONTENT));
                            String objectId = new String(comment.getObjectId());
                            String postId = new String(comment
                                    .getString(DuzooConstants.PARSE_COMMENT_POST_ID));
                            String userName = new String(comment
                                    .getString(DuzooConstants.PARSE_COMMENT_USER_NAME));
                            double timestamp = comment
                                    .getDouble(DuzooConstants.PARSE_COMMENT_TIMESTAMP);
                            db.createComment(userName, objectId, content, postId, timestamp);

                        } catch (NullPointerException ex) {
                            ex.printStackTrace();

                        }
                    }
                }
            }
        });
        return true;
    }

    public static void getMessages() {

        if (db == null)
            db = new DataSource(MyApplication.getContext());
        db.open();
        Date date = new Date(
                DuzooPreferenceManager.getLongKey(DuzooConstants.KEY_MESSAGE_TIMESTAMP));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        // .whereGreaterThan("updatedAt", date.getTime());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                DuzooPreferenceManager.putKey(DuzooConstants.KEY_MESSAGE_TIMESTAMP,
                        System.currentTimeMillis());
                if (e == null && messages.size() != 0) {
                    for (ParseObject message : messages) {
                        try {
                            String content = new String(message
                                    .getString(DuzooConstants.PARSE_MESSAGE_CONTENT));
                            String objectId = new String(message.getObjectId());
                            int type = message.getInt(DuzooConstants.PARSE_MESSAGE_INTEREST_TYPE);
                            String userName = new String(message
                                    .getString(DuzooConstants.PARSE_MESSAGE_USER_NAME));
                            double timeStamp = message
                                    .getDouble(DuzooConstants.PARSE_MESSAGE_TIMESTAMP);
                            String userFacebookId = new String(message
                                    .getString(DuzooConstants.PARSE_MESSAGE_FACEBOOK_ID));
                            if (userFacebookId.contentEquals(DuzooPreferenceManager
                                    .getKey(DuzooConstants.KEY_FACEBOOK_ID)))
                                db.createMessage(userName, objectId, content, timeStamp, type, 1);
                            else
                                db.createMessage(userName, objectId, content, timeStamp, type, 0);
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();

                        }
                    }
                }
            }
        });
    }

    public static void updatePostVote(String postId, final int vote,currentActivity activity) throws NullPointerException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post")
                .whereEqualTo("objectId", postId);
        if (vote == 1)
            showLoadToast("Upvoting post ...",activity);
        else
            showLoadToast("Downvoting post ...",activity);

        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> posts, ParseException e) {
                    if (vote == 1) {
                        posts.get(0).put("upvotes", posts.get(0).getInt("upvotes") + 1);
                        posts.get(0).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (loadToast != null) {
                                    if (e == null)
                                        loadToast.success();
                                    else
                                        loadToast.error();
                                }
                            }
                        });
                    } else {
                        posts.get(0).put("downvotes", posts.get(0).getInt("downvotes") + 1);
                        posts.get(0).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (loadToast != null) {
                                    if (e == null)
                                        loadToast.success();
                                    else
                                        loadToast.error();
                                }
                            }
                        });
                    }
                }
            });
        } catch (NullPointerException ex) {
            ex.printStackTrace();

        }
    }

    private static void showLoadToast(String message,currentActivity activity) {
        if(activity == currentActivity.duzoo)
            loadToast = new LoadToast(UIController.duzooActivity);
        else if(activity == currentActivity.home)
            loadToast = new LoadToast(UIController.homeViewPagerActivity);
        else if(activity == currentActivity.comments)
            loadToast = new LoadToast(UIController.commentsActivity);
        loadToast.setText(message);
        loadToast.show();
        loadToast.setTranslationY(100); // y offset in pixels
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
    }

    public static void createUserOnParse(String name, String id, String url)
            throws NullPointerException {

        ParseUser user = new ParseUser();
        user.put(DuzooConstants.PARSE_USER_NAME, name);
        user.setUsername(id);
        user.setPassword(id);
        user.put(DuzooConstants.PARSE_USER_IMAGE, url);
        user.put(DuzooConstants.PARSE_USER_FACEBOOK_ID, id);

        DuzooPreferenceManager.putKey(DuzooConstants.KEY_FACEBOOK_ID, id);
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_USER_NAME, name);
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_USER_IMAGE, url);
        DuzooPreferenceManager.putKey(DuzooConstants.KEY_SIGNED_UP, true);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    getMessages();
            }
        });

    }

    public static void createPost(String content, boolean imageAttached, Bitmap bitmap) {
        showLoadToast("Adding post ...",currentActivity.home);
        final ParseObject post = new ParseObject("Post");
        post.put(DuzooConstants.PARSE_POST_CONTENT, content);
        post.put(DuzooConstants.PARSE_POST_USER_NAME,
                DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME));
        post.put(DuzooConstants.PARSE_POST_USER_IMAGE,
                DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_IMAGE));
        post.put(DuzooConstants.PARSE_POST_UPVOTES, 0);
        post.put(DuzooConstants.PARSE_POST_DOWNVOTES, 0);
        post.put(DuzooConstants.PARSE_POST_IS_FLAGGED, false);
        post.put(DuzooConstants.PARSE_POST_FACEBOOK_ID,
                DuzooPreferenceManager.getKey(DuzooConstants.KEY_FACEBOOK_ID));
        post.put(DuzooConstants.PARSE_POST_INTEREST_TYPE,
                DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));
        post.put(DuzooConstants.PARSE_POST_TIMESTAMP, System.currentTimeMillis());
        post.put(DuzooConstants.PARSE_POST_COMMENT_COUNT, 0);
        post.put(DuzooConstants.PARSE_POST_HAS_MEDIA, imageAttached);
        if (imageAttached) {
            final ParseFile image = new ParseFile(Util.convertBitmapToString(bitmap).getBytes());
            image.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        post.put(DuzooConstants.PARSE_POST_IMAGE, image);
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (loadToast != null) {
                                    if (e == null)
                                        loadToast.success();
                                    else
                                        loadToast.error();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (loadToast != null) {
                        if (e == null)
                            loadToast.success();
                        else
                            loadToast.error();
                    }
                }
            });
        }
    }

    public static int createComment(String content) {

        showLoadToast("Adding comment ...",currentActivity.comments);
        if (DuzooActivity.isNetworkAvailable()) {
            ParseObject comment = new ParseObject("Comment");
            comment.put(DuzooConstants.PARSE_COMMENT_CONTENT, content);
            comment.put(DuzooConstants.PARSE_COMMENT_POST_ID,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID));
            comment.put(DuzooConstants.PARSE_COMMENT_USER_NAME,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME));
            comment.put(DuzooConstants.PARSE_COMMENT_USER_IMAGE,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_IMAGE));
            comment.put(DuzooConstants.PARSE_COMMENT_TIMESTAMP,
                    System.currentTimeMillis());
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                        loadToast.success();
                    else
                        loadToast.error();
                }
            });
            db.createComment(DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME),
                    comment.getObjectId(), content,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID),
                    System.currentTimeMillis());
            updatePostComments(DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID));
            getComments();
            return 1;
        } else if (loadToast != null)
            loadToast.error();
        return 0;
    }

    public static void createMessage(String content) {
        if (DuzooActivity.isNetworkAvailable()) {
            showLoadToast("Sending message ...",currentActivity.home);
            ParseObject message = new ParseObject("Message");
            message.put(DuzooConstants.PARSE_MESSAGE_CONTENT, content);
            message.put(DuzooConstants.PARSE_MESSAGE_INTEREST_TYPE,
                    DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));
            message.put(DuzooConstants.PARSE_MESSAGE_USER_NAME,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME));
            long timestamp = System.currentTimeMillis();
            message.put(DuzooConstants.PARSE_MESSAGE_TIMESTAMP, timestamp);
            message.put(DuzooConstants.PARSE_MESSAGE_FACEBOOK_ID,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_FACEBOOK_ID));
            message.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (loadToast != null) {
                        if (e == null)
                            loadToast.success();
                        else
                            loadToast.error();
                    }
                }
            });
            if (loadToast != null)
                loadToast.success();
            db.createMessage(DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME),
                    message.getObjectId(), content, timestamp,
                    DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE), 1);

        } else if (loadToast != null)
            loadToast.error();
    }

    public static void updatePostComments(String postId) throws NullPointerException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post")
                .whereEqualTo("objectId", postId);
        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> posts, ParseException e) {
                    posts.get(0).put(DuzooConstants.PARSE_POST_COMMENT_COUNT,
                            posts.get(0).getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT) + 1);
                    posts.get(0).saveInBackground(new SaveCallback() {
                                                      @Override
                                                      public void done(ParseException e) {
                                                      }
                                                  }
                    );
                }
            });
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
