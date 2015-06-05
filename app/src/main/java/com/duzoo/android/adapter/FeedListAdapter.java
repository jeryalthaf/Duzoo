
package com.duzoo.android.adapter;

/**
 * Created by RRaju on 2/7/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.application.UIController;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedListAdapter extends BaseAdapter {

    Context mContext;
    List<ParseObject> posts;
    LayoutInflater inflater;
    Activity activity;
    boolean favFragment;

    public FeedListAdapter(List<ParseObject> posts, Activity parentActivity) {
        mContext = parentActivity;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.posts = posts;
        activity = parentActivity;
        initfavFragment();
    }

    private void initfavFragment() {
        for (ParseObject post : posts) {
            if (!post.getBoolean(DuzooConstants.PARSE_POST_FAVORITE)) {
                favFragment = false;
                return;
            }
        }
        favFragment = true;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ParseObject post = posts.get(position);

        int vote = 0;
        boolean fav = false;

        try {
            vote = post.getInt(DuzooConstants.PARSE_POST_MY_VOTE);
            fav = post.getBoolean(DuzooConstants.PARSE_POST_FAVORITE);
        } catch (Exception ex) {
            vote = 0;
            fav = false;
        }
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_feed_list_item, null);


        TextView mName = (TextView) convertView.findViewById(R.id.home_post_name);
        TextView mContent = (TextView) convertView.findViewById(R.id.home_post_content);
        TextView voteStats = (TextView) convertView.findViewById(R.id.home_post_count);
        TextView mCommentCount = (TextView) convertView.findViewById(R.id.home_comment_count);

        ImageView mMedia = (ImageView) convertView.findViewById(R.id.home_post_media);
        ImageView mFav = (ImageView) convertView.findViewById(R.id.home_favorite);
        ImageView mDel = (ImageView) convertView.findViewById(R.id.home_delete);
        ImageView mWhatsApp = (ImageView) convertView.findViewById(R.id.home_share_whatsapp);
        ImageView uVote = (ImageView) convertView.findViewById(R.id.home_upvote);
        ImageView dVote = (ImageView) convertView.findViewById(R.id.home_downvote);

        CircleImageView mPic = (CircleImageView) convertView.findViewById(R.id.home_post_image);

        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryUserFetch(post);
            }
        });
        if (vote == 1) {
            uVote.setImageResource(R.drawable.up_inactive);
            dVote.setImageResource(R.drawable.down_active);
        } else if (vote == -1) {
            dVote.setImageResource(R.drawable.down_inactive);
            uVote.setImageResource(R.drawable.up_active);
        } else {
            dVote.setImageResource(R.drawable.down_active);
            uVote.setImageResource(R.drawable.up_active);
        }

        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askConfirmDialogAndDelete(post);
            }
        });
        if (fav)
            mFav.setImageResource(R.drawable.star_active);
        else
            mFav.setImageResource(R.drawable.star_inactive);

        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFav(post);
            }
        });
        uVote.setClickable(true);
        uVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(1, post);
            }
        });
        dVote.setClickable(true);
        dVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(-1, post);
            }
        });

        mWhatsApp.setClickable(true);
        mWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String text =
                        post.getString(DuzooConstants.PARSE_POST_CONTENT) + " via"
                                + " https://goo.gl/oGcacI";
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setPackage("com.whatsapp");
                try {
                    mContext.startActivity(sendIntent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (post.getBoolean(DuzooConstants.PARSE_POST_HAS_MEDIA)) {
            mMedia.setVisibility(View.VISIBLE);
            String imageLink = post.getParseFile(DuzooConstants.PARSE_POST_IMAGE).getUrl();
            Glide.with(mContext).load(imageLink).placeholder(R.drawable.placeholder).into(mMedia);
        } else
            mMedia.setVisibility(View.GONE);
        mName.setText(post.getString(DuzooConstants.PARSE_POST_USER_NAME));
        if (post.getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT) > 1)
            mCommentCount.setText(post.getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT) + " comments");
        else
            mCommentCount.setText(post.getInt(DuzooConstants.PARSE_POST_COMMENT_COUNT) + " comment");
        mCommentCount.setClickable(true);
        mCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DuzooPreferenceManager.putKey(DuzooConstants.KEY_POST_ID, post.getObjectId());
                UIController.switchToCommentsFragment();
            }
        });
        mContent.setText(post.getString(DuzooConstants.PARSE_POST_CONTENT));
        int score = post.getInt(DuzooConstants.PARSE_POST_UPVOTES) - post.getInt(DuzooConstants.PARSE_POST_DOWNVOTES);
        voteStats.setText(score + "");
        Glide.with(mContext).load(post.getString(DuzooConstants.PARSE_POST_USER_IMAGE)).error(R.drawable.user)
                .into(mPic);
        return convertView;
    }

    private void tryUserFetch(ParseObject post) {

        ParseQuery query = ParseQuery.getQuery("_User");
        query.whereEqualTo(DuzooConstants.PARSE_POST_FACEBOOK_ID, post.getString(DuzooConstants.PARSE_POST_FACEBOOK_ID));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String name = parseObject.getObjectId();
                    //          Toast.makeText(mContext,name,Toast.LENGTH_SHORT).show();
                } else {
                    String message = e.getMessage().toString();
                    //        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void askConfirmDialogAndDelete(final ParseObject post) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Confirm");
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this post ! This is irreversible")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        post.put(DuzooConstants.PARSE_POST_DELETED, true);
                        post.pinInBackground();
                        posts.remove(post);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void toggleFav(final ParseObject parseObject) {

        boolean fav = parseObject.getBoolean(DuzooConstants.PARSE_POST_FAVORITE);
        if (!fav) {
            parseObject.put(DuzooConstants.PARSE_POST_FAVORITE, true);
            parseObject.pinInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                        Toast.makeText(mContext, "Post added to favorites", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
        } else {
            parseObject.put(DuzooConstants.PARSE_POST_FAVORITE, false);
            parseObject.pinInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                        Toast.makeText(mContext, "Post removed from favorites", Toast.LENGTH_SHORT).show();
                    if(favFragment)
                        posts.remove(parseObject);
                    notifyDataSetChanged();

                }
            });
        }
    }

    private void checkAndVote(final int vote, final ParseObject post) {
        if (DuzooActivity.isNetworkAvailable()) {
            int status = post.getInt(DuzooConstants.PARSE_POST_MY_VOTE);
            if (status == 1 && vote == 1)
                Toast.makeText(mContext, "You have already upvoted this post", Toast.LENGTH_SHORT)
                        .show();
            else if (status == -1 && vote == -1)
                Toast.makeText(mContext, "You have already downvoted this post", Toast.LENGTH_SHORT)
                        .show();
            else {
                post.put(DuzooConstants.PARSE_POST_MY_VOTE, vote);
                if (vote == 1)
                    post.put(DuzooConstants.PARSE_POST_UPVOTES, post.getInt(DuzooConstants.PARSE_POST_UPVOTES) + 1);
                else if (vote == -1)
                    post.put(DuzooConstants.PARSE_POST_DOWNVOTES, post.getInt(DuzooConstants.PARSE_POST_DOWNVOTES) + 1);

                post.pinInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            if (vote == 1)
                                Toast.makeText(mContext, "Post upvoted", Toast.LENGTH_SHORT)
                                        .show();
                            else
                                Toast.makeText(mContext, "Post downvoted", Toast.LENGTH_SHORT)
                                        .show();
                            notifyDataSetChanged();
                            ParseLink.updatePostVote(post.getObjectId(), vote);
                        }
                    }
                });

            }
        } else
            Toast.makeText(mContext, "Sorry, no internet connection available", Toast.LENGTH_SHORT)
                    .show();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
