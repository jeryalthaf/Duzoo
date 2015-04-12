
package com.duzoo.android.datasource;

import android.content.Context;
import android.widget.Toast;

import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.util.ImageUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by RRaju on 3/27/2015.
 */
public class ParseLink {
    static Context mContext;
    DataSource     db;

    public ParseLink(Context context) {
        mContext = context;
        db = new DataSource(mContext);
        db.open();
    }

    public void getInterests() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interest");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> interests, ParseException e) {
                for (ParseObject interest : interests) {
                    String name = new String(interest.getString("interestName"));
                    int type = interest.getInt("interestType");
                    int followers = interest.getInt("followersCount");
                    String image = ImageUtil.convertParseFileToString(interest
                            .getParseFile("interestImage"));
                    db.createInterest(type, name, image, followers);
                }
            }
        });
    }

    public void getPosts() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                for (ParseObject post : posts) {
                    String content = new String(post.getString("content"));
                    String objectId = new String(post.getObjectId());
                    int upVotes = post.getInt("upvotes");
                    int downVotes = post.getInt("downvotes");
                    String userName = new String(post.getString("userName"));
                    String createdAt = post.getCreatedAt().toString();
                    db.createPost(userName, objectId, content, createdAt, upVotes, downVotes);
                }
            }
        });
    }

    public static void updatePostVote(String postId, final int vote) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post")
                .whereEqualTo("objectId", postId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                if (vote == 1) {
                    posts.get(0).put("upvotes", posts.get(0).getInt("upvotes") + 1);
                    posts.get(0).saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                Toast.makeText(mContext, "Post upvoted", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "Error when upvoting the post, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    posts.get(0).put("downvotes", posts.get(0).getInt("downvotes") + 1);
                    posts.get(0).saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                Toast.makeText(mContext, "Post downvoted", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "Error when downvoting the post, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public static void createUserOnParse(String name,String id,String url) {

        ParseUser user = new ParseUser();
        user.put("name", name);
        user.setUsername(id);
        user.setPassword(id);
        user.put("image_url", url);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                    Toast.makeText(mContext,"Parse user created",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext,"Parse user creation failed",Toast.LENGTH_SHORT).show();
            }
        });
        DuzooPreferenceManager.putKey("username", id);
        DuzooPreferenceManager.putKey("password", id);
        DuzooPreferenceManager.putKey("name", name);
        DuzooPreferenceManager.putKey("image", url);
        DuzooPreferenceManager.putKey("signed_up",true);

    }

}
